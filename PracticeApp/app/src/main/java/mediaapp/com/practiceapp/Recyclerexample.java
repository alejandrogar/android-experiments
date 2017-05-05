package mediaapp.com.practiceapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Recyclerexample extends AppCompatActivity{

    NetworkUtils utils = new NetworkUtils(Recyclerexample.this);

    private RecyclerView recycler;
    private TweetAdapter adapter;
    private RecyclerView.LayoutManager lManager;

    List<Tweet> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerexample);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(utils.isConnectingToInternet()){
            new DataFetcherTask().execute();
        }

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.TweetList);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

    }

    class DataFetcherTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String serverData = null;// String object to store fetched data from server
            // Http Request Code start
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://estudiaen.jalisco.gob.mx/appservices/tweets/tweets_get.php");
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                serverData = EntityUtils.toString(httpEntity);
                //Log.d("response", serverData);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(serverData);
                JSONArray jsonArray = jsonObject.getJSONArray("tweets");
                items.add(new Tweet("http://mycolorscreen.com/wp-content/uploads/wallpapers_2012/339551/image_new-103.jpg","Material Design CardView","Mon Mar 21 03:15:08 + 0000 2016",40));
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObjectTweet = jsonArray.getJSONObject(i);

                    String tweetText = jsonObjectTweet.getString("text");
                    String tweetDate = jsonObjectTweet.getString("fecha");
                    String image = jsonObjectTweet.getString("imagen");
                    if(tweetText.indexOf("&quot;") > 0 ){
                        tweetText = tweetText.replaceAll("&quot;", "\"");
                    }
                    Log.i("URL IMAGE", image);
                    items.add(new Tweet(image, tweetText, tweetDate,i));
                    //Log.i("URL IMAGE",image);

                }
                adapter = new TweetAdapter(items,getApplicationContext());

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new TweetAdapter(items,getApplicationContext());

            recycler.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
