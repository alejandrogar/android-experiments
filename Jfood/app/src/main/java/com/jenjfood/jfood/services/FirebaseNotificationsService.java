package com.jenjfood.jfood.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.renderscript.RenderScript;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jenjfood.jfood.Categories;
import com.jenjfood.jfood.Favorites;
import com.jenjfood.jfood.MainActivity;
import com.jenjfood.jfood.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

public class FirebaseNotificationsService extends FirebaseMessagingService {
    private static final String TAG = FirebaseNotificationsService.class.getSimpleName();
    RemoteMessage message;

    @Override public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        message = remoteMessage;
        //Objects.equals(remoteMessage.getFrom(), "/topics/news")

        if (Objects.equals(remoteMessage.getData().get("TYPE"), "SIMPLE"))
            sendSimpleNotification(remoteMessage.getNotification(), remoteMessage.getData());
        else {
            new sendNotification(getApplicationContext()).execute(remoteMessage.getFrom());
        }
    }
    private class sendNotification extends AsyncTask< String, Void, Bitmap > {
        Context ctx;
        public sendNotification(Context context) {
            super();
            this.ctx = context;
        }
        @Override protected Bitmap doInBackground(String...params) {
            InputStream in ;
            try {
                URL url = new URL(message.getData().get("URL"));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect(); in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream( in );
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            try {
                long[] pattern = {
                        500, 500, 500
                };
                android.support.v7.app.NotificationCompat.BigPictureStyle style = new android.support.v7.app.NotificationCompat.BigPictureStyle().bigPicture(result);
                style.setSummaryText(message.getNotification().getBody());
                Intent intent = new Intent(ctx, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                int color = Color.parseColor("#853C52");
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx)
                        .setSmallIcon(getNotificationIcon())
                        .setContentTitle(message.getNotification().getTitle() +"jeje")
                        .setContentText(message.getNotification().getBody())
                        .setContentInfo("")
                        .setTicker("Jfood")
                        .setAutoCancel(true)
                        .setColor(color)
                        .setSound(defaultSoundUri)
                        .setStyle(style)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notificationBuilder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendSimpleNotification(RemoteMessage.Notification notification, Map< String, String > data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int color = Color.parseColor("#853C52");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(message.getNotification().getTitle())
                .setContentText(message.getNotification().getBody())
                .setContentInfo("11")
                .setTicker("Jfood")
                .setAutoCancel(true)
                .setColor(color)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSound(defaultSoundUri)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_stat_ic_notification : R.drawable.ic_stat_ic_notification;
    }
}