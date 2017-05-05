package com.devexweb.materialconcept;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.devexweb.materialconcept.Fragments.Activity;
import com.devexweb.materialconcept.Fragments.Diary;
import com.devexweb.materialconcept.Fragments.News;
import com.devexweb.materialconcept.Fragments.People;
import com.devexweb.materialconcept.Fragments.Places;
import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity {

    @Bind(R.id.bottomBar) BottomBar bottomBar;
    @Bind(R.id.title)  TextView title;

    FragNavController fragNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            title.setText(getSupportActionBar().getTitle());
            getSupportActionBar().setTitle("");
        }

        List<Fragment> fragments = new ArrayList<>(5);

        fragments.add(Activity.newInstance("PARAM1"));
        fragments.add(News.newInstance("PARAM2"));
        fragments.add(People.newInstance("PARAM3"));
        fragments.add(Diary.newInstance("PARAM4"));
        fragments.add(Places.newInstance("PARAM5"));

        fragNavController = new FragNavController(getSupportFragmentManager(), R.id.container,fragments);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                if (tabId == R.id.tab_activity) {
                    fragNavController.switchTab(fragNavController.TAB1);
                }else if (tabId == R.id.tab_news) {
                    fragNavController.switchTab(fragNavController.TAB2);
                }else if(tabId == R.id.tab_people){
                    fragNavController.switchTab(fragNavController.TAB3);
                }else if(tabId == R.id.tab_diary){
                    fragNavController.switchTab(fragNavController.TAB4);
                }else if(tabId == R.id.tab_places){
                    Intent intent = new Intent(Home.this, Locations.class);
                    startActivity(intent);
                    //fragNavController.switchTab(fragNavController.TAB5);
                }
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_news) {
                }
            }
        });
    }
}
