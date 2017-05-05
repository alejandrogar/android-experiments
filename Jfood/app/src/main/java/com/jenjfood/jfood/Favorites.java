package com.jenjfood.jfood;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jenjfood.jfood.adapters.GridAdapter;
import com.jenjfood.jfood.adapters.RecipeAdapter;
import com.jenjfood.jfood.objects.Recipe;
import com.jenjfood.jfood.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Favorites extends AppCompatActivity {

    List<Recipe> items;
    RecipeAdapter adapter;
    RecyclerView recycler;
    RecyclerView.LayoutManager lManager;
    SwipeRefreshLayout refresh;
    ProgressBar loader;


    private GridView gridView;
    private GridAdapter adaptador;

    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        type = Utils.readSharedSetting(this, MainActivity.PREF_USER_VIEW_RECIPE, "true");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sendMessage("/topics/news", "Hola", "Axel come caca", "ic_stat_ic_notification", "SIMPLE");

        items = new ArrayList<>();

        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);

        gridView = (GridView) findViewById(R.id.grid);


        if(Objects.equals(type, "grid")){
            gridView.setNestedScrollingEnabled(true);
            recycler.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }else{
            recycler.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            lManager = new LinearLayoutManager(this);
            recycler.setLayoutManager(lManager);
            recycler.setNestedScrollingEnabled(false);
        }
        refresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        loader = (ProgressBar) findViewById(R.id.loader);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference filterRecipeRef = database.getReference("recipes/type");
        filterRecipeRef.keepSynced(true);

        ArrayList<String> favoritesList;
        favoritesList = new ArrayList<>(Arrays.asList(Utils.loadArray("favorites", this)));
        String[] favorites = new String[favoritesList.size()];
        favorites = favoritesList.toArray(favorites);
        for(int item =0;item<favorites.length;item++){
            Log.i("SEARCH FAV", favorites[item]);
            Query queryRef = filterRecipeRef.child("food").orderByChild("name").equalTo(favorites[item]);
            getData(queryRef);
            Query queryRefD = filterRecipeRef.child("drinks").orderByChild("name").equalTo(favorites[item]);
            getData(queryRefD);
            Query queryRefDs = filterRecipeRef.child("desserts").orderByChild("name").equalTo(favorites[item]);
            getData(queryRefDs);
        }

        refresh.setColorSchemeResources(R.color.colorPrimary);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items = new ArrayList<>();
                ArrayList<String> favoritesList;
                favoritesList = new ArrayList<>(Arrays.asList(Utils.loadArray("favorites", getApplicationContext())));
                String[] favorites = new String[favoritesList.size()];
                favorites = favoritesList.toArray(favorites);
                for(int item =0;item<favorites.length;item++){
                    Log.i("SEARCH FAV", favorites[item]);
                    Query queryRef = filterRecipeRef.child("food").orderByChild("name").equalTo(favorites[item]);
                    getData(queryRef);
                    Query queryRefD = filterRecipeRef.child("drinks").orderByChild("name").equalTo(favorites[item]);
                    getData(queryRefD);
                    Query queryRefDs = filterRecipeRef.child("desserts").orderByChild("name").equalTo(favorites[item]);
                    getData(queryRefDs);
                }
            }
        });
    }

    public void getData(Query filterRecipeRef){
        /*
            Leer los datos una sola vez
        */
        filterRecipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot recipesnapshot : snapshot.getChildren()) {
                    Recipe recipe = recipesnapshot.getValue(Recipe.class);
                    items.add(new Recipe(recipe.getType(),recipe.getName(),recipe.getDate(),recipe.getMainPicture(),recipe.getPortions(),recipe.getPTime(),recipe.getSteps(),recipe.getCategory(),recipe.getIngredients(),recipe.getgPictures(),recipe.getAuthor()));
                }
                if(Objects.equals(type, "grid")){
                    adaptador = new GridAdapter(items,Favorites.this);
                    gridView.setAdapter(adaptador);
                }else{
                    adapter = new RecipeAdapter(items, Favorites.this);
                    recycler.setAdapter(adapter);

                }
                refresh.setRefreshing(false);
                loader.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("JFOOD DATA", "getUser:onCancelled", databaseError.toException());
            }
        });
    }

    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    OkHttpClient mClient = new OkHttpClient();

    public void sendMessage(final String to, final String title, final String body, final String icon, final String type) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", body);
                    notification.put("title", title);
                    notification.put("icon", icon);

                    JSONObject data = new JSONObject();
                    data.put("TYPE", type);

                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("to", "/topics/news");

                    String result = postToFCM(root.toString());
                    Log.d("ANY TAG", "Result: " + result);
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                Log.i("RESULT", "--"+result+"--");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    int success, failure;
                    success = resultJson.getInt("success");
                    failure = resultJson.getInt("failure");
                    Toast.makeText(Favorites.this, "Message Success: " + success + "Message Failed: " + failure, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();

                    try {
                        JSONObject resultJson = new JSONObject(result);
                        int message_id;
                        message_id = resultJson.getInt("message_id");
                        Toast.makeText(Favorites.this, "Message Id: " + message_id, Toast.LENGTH_LONG).show();

                    }catch (JSONException E){
                        E.printStackTrace();
                        Toast.makeText(Favorites.this, "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }.execute();
    }

    String postToFCM(String bodyString) throws IOException {

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), bodyString);

        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "key=" + "AAAA15MMK4s:APA91bGVn-j9tuf2UWmzQ-wYaICXlkksQ-H_UyqGiR32wXgA3aHsHyHRCFqTTbLQ6V_0lZKRyXmvXsToZh4jYeg5y7jXlBZO4YldqTp-IQUAnOEuj2vrxIQRVkzoaSSKkDB85nt1Ug-cRMC_5YDS25KotxDwIhzCXw")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

}
