package mediaapp.com.practiceapp;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String PREF_USER_FIRST_TIME = "user_first_time";
    boolean isUserFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Explode explode = new Explode();
        explode.setDuration(300); // Duración en milisegundos
        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(explode);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        isUserFirstTime = Boolean.valueOf(Utils.readSharedSetting(MainActivity.this, PREF_USER_FIRST_TIME, "true"));

        Intent introIntent = new Intent(MainActivity.this, Main2Activity.class);
        introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);

        if (isUserFirstTime) {
            startActivity(introIntent);
        }

        TextView text = (TextView) findViewById(R.id.text1);
        text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(MainActivity.this, "TextView 1 was Clicked", Toast.LENGTH_SHORT).show();
            }

        });

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });

        Button buttonrecycler = (Button) findViewById(R.id.recyclerview);
        buttonrecycler.setOnClickListener(this);

        Button buttonslide = (Button) findViewById(R.id.SlideImage);
        buttonslide.setOnClickListener(this);

        Button ScrollingViewSlide = (Button) findViewById(R.id.scrollslideimage);
        ScrollingViewSlide.setOnClickListener(this);

        Button FirebseExample = (Button) findViewById(R.id.chatFirebse);
        FirebseExample.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.recyclerview){
            Intent startIntent = new Intent(getApplicationContext(), Recyclerexample.class);
            startActivity(startIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }else if(v.getId() == R.id.SlideImage){
            Intent startIntent = new Intent(getApplicationContext(), img_slider.class);
            startActivity(startIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }else if(v.getId() == R.id.scrollslideimage){
            Intent startIntent = new Intent(getApplicationContext(), ScrollingSlide.class);
            startActivity(startIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }else if(v.getId() == R.id.chatFirebse){
            Intent startIntent = new Intent(getApplicationContext(), Login.class);
            startActivity(startIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
    }
}
