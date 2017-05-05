package com.jenjfood.jfood.services;

/**
 * Created by root on 29/05/16.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import com.jenjfood.jfood.MainActivity;
import com.jenjfood.jfood.R;
import com.jenjfood.jfood.Settings;
import com.jenjfood.jfood.utils.Utils;

public class DiaryService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // For our recurring task, we'll just display a message
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        long[] pattern = {500,500,500,500,500,500,500,500,500,500};
        int color = getColor(context, R.color.colorPrimary);

        Boolean enabledAlarm = Boolean.valueOf(Utils.readSharedSetting(context, Settings.PREF_USER_DIARY_NOTIFICATIONS, "true"));
        if(enabledAlarm){
            NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_stat_maps_local_restaurant)
                    .setLargeIcon((((BitmapDrawable)context.getResources()
                            .getDrawable(R.drawable.logo_3)).getBitmap()))
                    .setContentTitle("Jfood")
                    .setColor(color)
                    .setContentText("Recordatorio")
                    .setContentInfo("")
                    .setTicker("Jfood")
                    .setAutoCancel(true)
                    .setSound(sound)
                    //.setStyle(style)
                    .setVibrate(pattern);
            Intent notIintent = new Intent(context,MainActivity.class);
            PendingIntent contIntent = PendingIntent.getActivity(context,0,notIintent,0);
            mBuilder.setContentIntent(contIntent);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0,mBuilder.build());
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