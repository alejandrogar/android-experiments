package com.jenjfood.jfood;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.google.firebase.database.FirebaseDatabase;
import com.jenjfood.jfood.utils.Utils;

import java.util.Calendar;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    private SwitchCompat notificationsSwitch;
    private SwitchCompat notificationsDiarySwitch;

    RelativeLayout layout;
    RelativeLayout privacy;
    RelativeLayout clear_data;

    public static final String PREF_USER_NOTIFICATIONS = "active_notifications";
    public static final String PREF_USER_DIARY_NOTIFICATIONS = "active_diary_notifications";

    boolean isActiveNotification;
    boolean isActiveDiaryNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        layout = (RelativeLayout) findViewById(R.id.container);
        privacy = (RelativeLayout) findViewById(R.id.PrivacyContainer);
        clear_data = (RelativeLayout) findViewById(R.id.ClearDataContainer);
        privacy.setOnClickListener(this);
        clear_data.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.PrivacyContainer){
            Intent privacyIntent = new Intent(Settings.this, PrivacyPolicy.class);
            startActivity(privacyIntent);
        }else if(id == R.id.ClearDataContainer){
            Utils.saveSharedSetting(Settings.this, MainActivity.PREF_USER_VIEW_RECIPE, "card");
            Utils.saveSharedSetting(Settings.this, Settings.PREF_USER_NOTIFICATIONS, "true");
            Utils.saveSharedSetting(Settings.this, Settings.PREF_USER_DIARY_NOTIFICATIONS, "false");
            String[] favorites = new String[0];
            Utils.saveArray(favorites,"favorites",this);
        }
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}
