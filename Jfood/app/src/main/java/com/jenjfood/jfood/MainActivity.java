package com.jenjfood.jfood;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.jenjfood.jfood.adapters.ListAdapterIconDialog;
import com.jenjfood.jfood.fragments.BebidasFragment;
import com.jenjfood.jfood.fragments.PlatillosFragment;
import com.jenjfood.jfood.fragments.PostresFragment;
import com.jenjfood.jfood.objects.Tip;
import com.jenjfood.jfood.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PlatillosFragment.OnFragmentInteractionListener, BebidasFragment.OnFragmentInteractionListener, PostresFragment.OnFragmentInteractionListener{

    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.ic_action_maps_local_restaurant_primary,
            R.drawable.ic_action_maps_local_bar_primary,
            R.drawable.ic_action_social_cake_primary
    };

    private int[] tabIconsUnselect = {
            R.drawable.ic_action_maps_restaurant_menu ,
            R.drawable.ic_action_maps_local_bar,
            R.drawable.ic_action_social_cake
    };

    private String[] subtitles = {
        "Platillos",
        "Bebidas",
        "Postres"
    };

    public static final int[] BackHeaders = {
        R.drawable.apple_stuartwebster,
        R.drawable.bread_dinnerseries,
        R.drawable.garlic_yourdon,
        R.drawable.market,
        R.drawable.stern_fruits,
        R.drawable.strwaberries
    };

    public static final String PREF_USER_FIRST_TIME = "user_first_time";
    public static final String PREF_USER_VIEW_RECIPE = "user_view_recipe";

    boolean isUserFirstTime;

    int categoryCount = 0;
    int favoritesCount = 0;
    int tipsCount = 0;

    TextView categories,favorites,tips,v_recipes;

    Query queryRef;
    Query TipsRefQuey;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("signInAnonymously", "signInAnonymously:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("signInAnonymously", "signInAnonymously", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("USER TOKE OR NOT", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("USER TOKE OR ", "onAuthStateChanged:signed_out");
                }
            }
        };

        Log.i("TOKEN",FirebaseInstanceId.getInstance().getToken());

        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder("925885016971" + "@gcm.googleapis.com")
                .setMessageId("123")
                .addData("TYPE", "SIMPLE")
                .addData("my_action","SAY_HELLO")
                .build());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isUserFirstTime = Boolean.valueOf(Utils.readSharedSetting(MainActivity.this, PREF_USER_FIRST_TIME, "true"));

        Intent introIntent = new Intent(MainActivity.this, Intro.class);
        introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);

        if (isUserFirstTime) {
            FirebaseMessaging.getInstance().subscribeToTopic("news");
            Utils.saveSharedSetting(MainActivity.this, MainActivity.PREF_USER_VIEW_RECIPE, "card");
            Utils.saveSharedSetting(MainActivity.this, Settings.PREF_USER_NOTIFICATIONS, "true");
            Utils.saveSharedSetting(MainActivity.this, Settings.PREF_USER_DIARY_NOTIFICATIONS, "false");
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            String[] favorites = new String[0];
            Utils.saveArray(favorites,"favorites",this);


            int PERMISSION_ALL = 1;
            String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION};

            if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
            }
            startActivity(introIntent);
        }

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d("FFIREBASEEEE", "Key: " + key + " Value: " + value);
            }
        }

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageView header_image = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.background_header_image);

        Random rand = new Random();

        int image = rand.nextInt((BackHeaders.length) - 1);

        Glide.with(this)
                .load(BackHeaders[image])
                .centerCrop()
                .crossFade()
                .into(header_image);

        categories = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.categories));
        favorites = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.favorites));
        tips = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.tips));
        v_recipes = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.videorecipes));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference categiesRef = database.getReference("recipes/categories");
        DatabaseReference TipsRef = database.getReference("recipes/tips");
        categiesRef.keepSynced(true);

        queryRef = categiesRef.orderByKey();
        TipsRefQuey = TipsRef.orderByKey();
        getData(queryRef, TipsRefQuey);

        setupTabIcons();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){ tabLayout.getTabAt(0).setIcon(tabIcons[0]);}
                if(tab.getPosition() == 1) tabLayout.getTabAt(1).setIcon(tabIcons[1]);
                if(tab.getPosition() == 2) tabLayout.getTabAt(2).setIcon(tabIcons[2]);
                viewPager.setCurrentItem(tab.getPosition());
                getSupportActionBar().setSubtitle(subtitles[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0) tabLayout.getTabAt(0).setIcon(tabIconsUnselect[0]);
                if(tab.getPosition() == 1) tabLayout.getTabAt(1).setIcon(tabIconsUnselect[1]);
                if(tab.getPosition() == 2) tabLayout.getTabAt(2).setIcon(tabIconsUnselect[2]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void signOut() {
        mAuth.signOut();
    }
    public void getData(Query queryRef, final Query TipsRefQuey){
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                ArrayList<String> favoritesList;
                favoritesList = new ArrayList<>(Arrays.asList(Utils.loadArray("favorites", getApplicationContext())));
                String[] favorites = new String[favoritesList.size()];
                favorites = favoritesList.toArray(favorites);

                categoryCount = (int) snapshot.getChildrenCount();
                favoritesCount = favorites.length;

                TipsRefQuey.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        tipsCount = (int) snapshot.getChildrenCount();
                        initializeCountDrawer(categoryCount+"",favoritesCount+"", tipsCount+"");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("JFOOD DATA", "getUser:onCancelled", databaseError.toException());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("JFOOD DATA", "getUser:onCancelled", databaseError.toException());
            }
        });
    }

    private void initializeCountDrawer(String categoriesCount,String favoritesCount, String tipsCount){
        //Gravity property aligns the text
        categories.setGravity(Gravity.CENTER_VERTICAL);
        categories.setTypeface(null, Typeface.BOLD);
        categories.setGravity(Gravity.CENTER_VERTICAL);
        categories.setTypeface(null,Typeface.BOLD);
        categories.setTextColor(getResources().getColor(R.color.TextIcons));
        categories.setText(categoriesCount);

        favorites.setGravity(Gravity.CENTER_VERTICAL);
        favorites.setTypeface(null, Typeface.BOLD);
        favorites.setGravity(Gravity.CENTER_VERTICAL);
        favorites.setTypeface(null,Typeface.BOLD);
        favorites.setTextColor(getResources().getColor(R.color.TextIcons));
        favorites.setText(favoritesCount);

        tips.setGravity(Gravity.CENTER_VERTICAL);
        tips.setTypeface(null, Typeface.BOLD);
        tips.setGravity(Gravity.CENTER_VERTICAL);
        tips.setTypeface(null,Typeface.BOLD);
        tips.setTextColor(getResources().getColor(R.color.TextIcons));
        tips.setText(tipsCount);

        v_recipes.setGravity(Gravity.CENTER_VERTICAL);
        v_recipes.setTypeface(null, Typeface.BOLD);
        v_recipes.setTextColor(getResources().getColor(R.color.TextIcons));
        v_recipes.setGravity(Gravity.CENTER_VERTICAL);
        v_recipes.setTypeface(null,Typeface.BOLD);
        v_recipes.setText("Próximamente");
    }

    private void setupTabIcons() {
        getSupportActionBar().setSubtitle(subtitles[0]);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIconsUnselect[1]);
        tabLayout.getTabAt(2).setIcon(tabIconsUnselect[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PlatillosFragment(), "PLATILLOS");
        adapter.addFragment(new BebidasFragment(), "BEBIDAS");
        adapter.addFragment(new PostresFragment(), "POSTRES");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return mFragmentTitleList.get(position);
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setBackgroundColor(Color.WHITE);
        getData(queryRef, TipsRefQuey);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent;

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_aspect){
            ChangeAspect();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = null;

        if (id == R.id.favorites) {
            intent = new Intent(this, Favorites.class);
        } else if (id == R.id.categories) {
            intent = new Intent(this, Categories.class);
        } else if (id == R.id.tips) {
            intent = new Intent(this, Tips.class);
        }else if (id == R.id.diary){
            intent = new Intent(this, Diary.class);
        } else if (id == R.id.videorecipes) {
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_info) {
            intent = new Intent(this, Intro.class);
        } else if(id == R.id.settings){
            intent = new Intent(this, Settings.class);
        } else if(id == R.id.contact){
            ContactModal();
        }

        if(intent != null){
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void ChangeAspect() {
        final String[] items = {
                "Lista", "Tarjetas","Grilla"
        };

        final Integer[] icons = new Integer[] {R.drawable.ic_action_action_view_list_primary, R.drawable.ic_action_action_card_view_primary, R.drawable.ic_action_action_dashboard};
        ListAdapter adapter = new ListAdapterIconDialog(this, items, icons);

        new AlertDialog.Builder(this)
                .setTitle("Cambiar aspecto")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item ) {
                        switch (item){
                            case 0:
                                Utils.saveSharedSetting(MainActivity.this, MainActivity.PREF_USER_VIEW_RECIPE, "list");
                                break;
                            case 1:
                                Utils.saveSharedSetting(MainActivity.this, MainActivity.PREF_USER_VIEW_RECIPE, "card");
                                break;
                            case 2:
                                Utils.saveSharedSetting(MainActivity.this, MainActivity.PREF_USER_VIEW_RECIPE, "grid");
                                break;
                        }
                        finish();
                        startActivity(getIntent());
                    }
                }).show();
    }

    private void ContactModal() {

        final Dialog dialog = new Dialog(MainActivity.this);

        dialog.setContentView(R.layout.contact_dialog_layout);
        dialog.setTitle(R.string.title_contact);
        //dialog.setTitle("Custom Dialog");

        TextView txt = (TextView) dialog.findViewById(R.id.text);
        //txt.setText("Put your dialog text here.");

        //ImageView image = (ImageView)dialog.findViewById(R.id.image);
        //image.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_info));

        //adding button click event
        Button dismissButton = (Button) dialog.findViewById(R.id.submit);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
