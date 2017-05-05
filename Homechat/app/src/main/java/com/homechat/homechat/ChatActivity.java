package com.homechat.homechat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.homechat.homechat.Adapters.MessageAdapter;
import com.homechat.homechat.Objects.Message;
import com.homechat.homechat.Objects.User;
import com.homechat.homechat.Utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private static final int WRITE_EXTERNAL_STORAGE = 0;
    private static final int CAMERA = 0;

    static final int REQUEST_IMAGE_SELECTED = 2;
    static final int REQUEST_IMAGE_CAPTURE = 3;

    List<Message> items;
    MessageAdapter adapter;
    RecyclerView recycler;
    RecyclerView.LayoutManager lManager;
    String encodedImage = "NO IMAGE";

    @Bind(R.id.image_to_send) ImageView imageToSend;
    @Bind(R.id.image_to_send_container) RelativeLayout imageToSendContainer;
    @Bind(R.id.user_image) ImageView userImage;
    @Bind(R.id.user_image_toolbar) ImageView userImageToolbar;

    DatabaseReference chats;
    Query userRef;

    @Bind(R.id.user_name) TextView userName;
    @Bind(R.id.user_email) TextView userEmail;
    @Bind(R.id.user_description) TextView userDesc;
    @Bind(R.id.title) TextView Title;
    @Bind(R.id.subtitle) TextView Subtitle;
    @Bind(R.id.MessageText) TextView MessageText;

    @Bind(R.id.send) FloatingActionButton Send;

    String keyMessageGenerated;
    FirebaseDatabase database;
    Bundle bundle;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        items = new ArrayList<>();
        bundle = getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users").orderByKey().equalTo(bundle.get("USER_KEY").toString());
        chats = database.getReference("chats");
        chats.keepSynced(true);

        final Query messagesRef = chats.child(bundle.get("CHAT_KEY").toString()).child("messages").orderByChild("date");
        messagesRef.keepSynced(true);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot usersnapshot : snapshot.getChildren()) {
                    User user = usersnapshot.getValue(User.class);
                    String Bytes = user.getImage();
                    byte[] imageByteArray = Base64.decode(Bytes, Base64.DEFAULT);

                    Title.setText(user.getName());
                    Subtitle.setText("Online");

                    Glide.with(getApplicationContext())
                            .load(imageByteArray)
                            .asBitmap()
                            .placeholder(R.drawable.ic_action_action_account_circle_white)
                            .into(userImage);

                    Glide.with(getApplicationContext())
                            .load(imageByteArray)
                            .asBitmap()
                            .placeholder(R.drawable.ic_action_action_account_circle_white)
                            .into(userImageToolbar);

                    userImageToolbar.setVisibility(View.GONE);

                    userName.setText(user.getName());
                    userEmail.setText(user.getEmail());
                    userDesc.setText(user.getDesc());

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("CHAT DATA", "Failed to read value.", error.toException());
            }
        });

        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                recycler = (RecyclerView) findViewById(R.id.recycler);
                recycler.setHasFixedSize(true);
                recycler.setNestedScrollingEnabled(false);
                //String key, String name, String date, String previewMessage, String image
                items = new ArrayList<>();
                for (DataSnapshot msgsnapshot : snapshot.getChildren()) {
                    Message message = msgsnapshot.getValue(Message.class);
                    items.add(new Message(msgsnapshot.getKey(), message.getDate(), message.getFrom(), message.getMessage(), message.getImage()));
                    adapter = new MessageAdapter(items, getApplicationContext());
                    lManager = new LinearLayoutManager(getApplicationContext());
                    recycler.setLayoutManager(lManager);
                    recycler.setAdapter(adapter);
                    Integer i = items.size();
                    recycler.scrollToPosition(i - 1);
                }
                final NestedScrollView scrollview = (NestedScrollView) findViewById(R.id.message_container);
                final boolean[] scrolled = {false};
                scrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

                    @Override
                    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        if (scrollY >= 440 && !scrolled[0]) {
                            scrolled[0] = true;
                            userImageToolbar.setVisibility(View.VISIBLE);
                        }
                        if(scrollY < 440){
                            scrolled[0] = false;
                            userImageToolbar.setVisibility(View.GONE);
                        }
                    }
                });
                scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("CHAT DATA", "Failed to read value.", error.toException());
            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long tsLong = System.currentTimeMillis() / 1000;
                if (!Objects.equals(MessageText.getText().toString(), "") || !Objects.equals(encodedImage, "NO IMAGE")) {
                    keyMessageGenerated = chats.child(bundle.get("CHAT_KEY").toString()).child("messages").push().getKey();
                    String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    Message msg = new Message(keyMessageGenerated, tsLong, currentUserEmail, MessageText.getText().toString(), encodedImage);
                    chats.child(bundle.get("CHAT_KEY").toString()).child("messages").child(keyMessageGenerated).setValue(msg);

                    MessageText.setText("");
                    MessageText.setImeActionLabel("Custom text", KeyEvent.KEYCODE_ENTER);
                    encodedImage = "NO IMAGE";
                    imageToSendContainer.setVisibility(View.GONE);

                    if (findViewById(R.id.MessageText).isFocusable()) {
                        findViewById(R.id.MessageText).requestFocus();
                    }
                }
            }
        });
        //imageToSend = (ImageView) findViewById(R.id.image_to_send);
    }

    @Override
    protected void onDestroy() {
        /*if(items.size() <= 0){
            database.getReference().child("users").child(bundle.get("USER_KEY").toString()).child("chats").child(bundle.get("CHAT_KEY").toString()).removeValue();
            database.getReference().child("users").child(Utils.readSharedSetting(ChatActivity.this, Login.USER_UID, "false")).child("chats").child(bundle.get("CHAT_KEY").toString()).removeValue();
        }*/
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.send_image) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE_SELECTED);
        } else if (id == R.id.send_photo) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.i(TAG, ex.getMessage());
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_SELECTED && data != null) {
            String realPath;
            realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
            File imgFile = new File(realPath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageToSendContainer.setVisibility(View.VISIBLE);
                imageToSend.setImageBitmap(myBitmap);

                Bitmap bm = BitmapFactory.decodeFile(realPath);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream); //bm is the bitmap object
                byte[] b = byteArrayOutputStream.toByteArray();
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap mImageBitmap;
                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                imageToSendContainer.setVisibility(View.VISIBLE);

                final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                        PixelFormat.TRANSLUCENT);

                WindowManager wm = (WindowManager) getApplicationContext()
                        .getSystemService(Context.WINDOW_SERVICE);

                View mTopView = getLayoutInflater().inflate(R.layout.send_image, null);
                getWindow().setAttributes(params);
                wm.addView(mTopView, params);

                ImageView imageTosend = (ImageView) mTopView.findViewById(R.id.image_to_send);

                imageTosend.setImageBitmap(mImageBitmap);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static class RealPathUtil {

        @SuppressLint("NewApi")
        public static String getRealPathFromURI_API19(Context context, Uri uri) {
            String filePath = "";
            String wholeID = DocumentsContract.getDocumentId(uri);

            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        }
    }
}
