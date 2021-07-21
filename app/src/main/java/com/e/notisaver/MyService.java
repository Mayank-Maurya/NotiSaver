package com.e.notisaver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MyService extends NotificationListenerService {
    Context context;

    String title = "";
    String text = "";


    @Override
    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setPriority(Notification.PRIORITY_MIN);
        Notification notification = notificationBuilder.setOngoing(false)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

        startForeground(2, notification);
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {
            Bundle extras = sbn.getNotification().extras;
            title = extras.getString("android.title");
            text = extras.getCharSequence("android.text").toString();
            Log.i("Title", title);
            Log.i("Text", text);


            if(!(text.toLowerCase().contentEquals("incoming call") || text.toLowerCase().contentEquals("current call")
            || text.toLowerCase().contentEquals("ended call")))
            {
                Intent dialogIntent = new Intent(this, DialogActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                dialogIntent.putExtra("title", title);
                dialogIntent.putExtra("text", text);
                startActivity(dialogIntent);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, BReceiver.class);
        this.sendBroadcast(broadcastIntent);
    }


}
