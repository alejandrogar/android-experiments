package com.jenjfood.jfood;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jenjfood.jfood.services.DiaryService;
import com.jenjfood.jfood.utils.Utils;

import java.util.Calendar;

public class Diary extends AppCompatActivity {

    public static final String PREF_USER_DIARY_HOUR = "diary_hour";
    public static final String PREF_USER_DIARY_MINUTE= "diary_minute";

    private PendingIntent pendingIntent;
    boolean isActiveDiaryNotification;
    CardView cardDescDiary;
    Button defineTime;
    int Hour;
    int Minute;
    RelativeLayout containerDefine;
    TextView time_set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent alarmIntent = new Intent(Diary.this, DiaryService.class);
        pendingIntent = PendingIntent.getBroadcast(Diary.this, 0, alarmIntent, 0);

        cardDescDiary = (CardView) findViewById(R.id.cardDescriptionDiary);
        defineTime = (Button) findViewById(R.id.define_time);
        containerDefine = (RelativeLayout) findViewById(R.id.setTimeContainer);
        time_set = (TextView) findViewById(R.id.time_set);

        setTimeOnTextView();

        isActiveDiaryNotification = Boolean.valueOf(Utils.readSharedSetting(Diary.this, Settings.PREF_USER_DIARY_NOTIFICATIONS, "true"));
        if(!isActiveDiaryNotification){
            cardDescDiary.setVisibility(View.VISIBLE);
            containerDefine.setVisibility(View.GONE);
        }else{
            cardDescDiary.setVisibility(View.GONE);
            containerDefine.setVisibility(View.VISIBLE);
        }

        defineTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Diary.this);
                dialog.setContentView(R.layout.time_picker_layout);
                dialog.setTitle("Definir hora");
                dialog.show();

                final TimePicker tp = (TimePicker)dialog.findViewById(R.id.time);
                /*tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        Hour = hourOfDay;
                        Minute = minute;
                    }
                });*/

                Button define = (Button)dialog.findViewById(R.id.define_time);
                define.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Hour = tp.getHour();
                            Minute = tp.getMinute();
                        }else {
                            Hour = tp.getCurrentHour();
                            Minute = tp.getCurrentMinute();
                        }
                        //Toast.makeText(Diary.this, "Alerta establecida Time = "+Hour+":"+Minute, Toast.LENGTH_SHORT).show();
                        Utils.saveSharedSetting(Diary.this, Diary.PREF_USER_DIARY_HOUR, String.valueOf(Hour));
                        Utils.saveSharedSetting(Diary.this, Diary.PREF_USER_DIARY_MINUTE, String.valueOf(Minute));
                        setTimeOnTextView();
                        setAlarm();
                    }
                });
            }
        });

        /*
        ArrayList<String> favoritesList;
            favoritesList = new ArrayList<>(Arrays.asList(Utils.loadArray("favorites", getApplicationContext())));
            for(int item =0;item<favoritesList.size();item++){
                if(Objects.equals(favoritesList.get(item), RecipeName)){
                    favoritesList.remove(item);
                }
            }
            String[] favorites = new String[favoritesList.size()];
            favorites = favoritesList.toArray(favorites);
            Utils.saveArray(favorites,"favorites",getApplicationContext());
            remove_fav.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            Toast.makeText(DetailActivity.this, "Añadido a favoritos", Toast.LENGTH_SHORT).show();
         */
    }

    private void setTimeOnTextView() {
        String hour = Utils.readSharedSetting(Diary.this, Diary.PREF_USER_DIARY_HOUR,"true");
        String minute = Utils.readSharedSetting(Diary.this, Diary.PREF_USER_DIARY_MINUTE,"true");

        time_set.setText(hour+":"+minute+" Hrs.");
    }

    public void setAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Hour);
        calendar.set(Calendar.MINUTE, Minute);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
