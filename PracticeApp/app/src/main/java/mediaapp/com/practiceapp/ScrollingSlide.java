package mediaapp.com.practiceapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

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

public class ScrollingSlide extends AppCompatActivity {

    NetworkUtils utils = new NetworkUtils(ScrollingSlide.this);

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.drawable.mdvalley,R.drawable.mdvalley,R.drawable.mdvalley,R.drawable.mdvalley};
    private static final String[] URLIMAGES = {"http://pbs.twimg.com/media/CeQMoSvUYAAp4Pt.jpg","http://pbs.twimg.com/media/CeQHajRVAAA8P17.jpg","http://pbs.twimg.com/media/CeQGK0rVAAAN8rH.jpg","http://pbs.twimg.com/media/CeQF4eLUYAAIxZk.jpg"};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private ArrayList<String> ImagesUrlArray = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_slide);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if(utils.isConnectingToInternet()){
            new DataFetcherTask().execute();
        }
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
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObjectTweet = jsonArray.getJSONObject(i);
                    String image = jsonObjectTweet.getString("imagen");
                    if(image == "null"){
                        Log.i("URL IMAGE", image);
                    }else{
                        ImagesUrlArray.add(image);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            init();
        }
    }

    private void init() {

        /*  ADD STATIC RESOURCE DRAWABLE
        for(int i=0;i<IMAGES.length;i++){
            ImagesArray.add(IMAGES[i]);
        }*/

        /*ADD DINAMICALY RESOCURCE URL FROM WEB SERVICE
        for(int i=0;i<URLIMAGES.length;i++){
            ImagesUrlArray.add(URLIMAGES[i]);
        }*/

        mPager = (ViewPager) findViewById(R.id.pager);


        mPager.setAdapter(new ImageAdapter(ScrollingSlide.this,ImagesUrlArray));


        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =IMAGES.length;

        // Auto start of viewpager
        /*final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);*/

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
                //Log.i("POSITION",""+arg1);
            }

            @Override
            public void onPageScrollStateChanged(int pos) {
            }
        });

    }
}
