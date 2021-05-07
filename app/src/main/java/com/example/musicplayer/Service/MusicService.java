package com.example.musicplayer.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.musicplayer.R;
import com.example.musicplayer.Utill.GetMusic;

public class MusicService extends Service implements ServiceConnection {

    MusicServiceBinder binder = new MusicServiceBinder();
    MediaPlayer mp;
    String songname,singername;

    public final String NEXT = "com.example.musicplayer.NEXT";
    public final String PREVIOUS = "com.example.musicplayer.PREVIOUS";
    public final String PLAY = "com.ex ample.musicplayer.PLAY";
    public final String EXIT = "com.example.musicplayer.EXIT";
    public final String LOW = "com.example.musicplayer.LOW";
    public final String HIGH = "com.example.musicplayer.HIGH";
    Intent intentsendBroadCast = new Intent("intentsendBroadCastAlisafarzadeh");
    int numbersound = 0;
    Notification notification;


    @Override
    public IBinder onBind(Intent intent) {
        //path = ;
        numbersound = intent.getIntExtra("Number",0);
        mp = MediaPlayer.create(this,Uri.parse(intent.getStringExtra("music")));
        songname = GetMusic.getmusic(numbersound,0,0).get(0).getSong();
        singername = GetMusic.getmusic(numbersound,0,0).get(0).getSinger();
        return binder;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, intent.getAction()+"", Toast.LENGTH_SHORT).show();
        switch (intent.getAction())
        {
            case PLAY:
                try {
                    if (mp.isPlaying()) {
                        mp.pause();
                    } else {
                        mp.start();
                    }
                }catch (Exception e)
                {

                }


                break;

            case NEXT:
                Log.d("ddvc","next");
                next();
                //Toast.makeText(this, "next", Toast.LENGTH_SHORT).show();
                break;

            case PREVIOUS:
                Log.d("ddvc","prev");
                previous();
                break;

            case EXIT:
                stopSelf();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    stopForeground(true);
                }

                Log.d("ddvc","dismis");
                break;

            case LOW:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;

            case HIGH:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;

             default:

                 break;
        }



        Intent PreviousIntent = new Intent(this, MusicService.class);
        PreviousIntent.setAction(PREVIOUS);
        PendingIntent ppre = PendingIntent.getService(this, 0, PreviousIntent, 0);

        Intent PlayIntent = new Intent(this, MusicService.class);
        PlayIntent.setAction(PLAY);
        PendingIntent pplay = PendingIntent.getService(this, 0, PlayIntent, 0);

        Intent NextIntent = new Intent(this, MusicService.class);
        NextIntent.setAction(NEXT);
        PendingIntent pnext = PendingIntent.getService(this, 0, NextIntent, 0);

        Intent ExitIntent = new Intent(this, MusicService.class);
        ExitIntent.setAction(EXIT);
        PendingIntent eintent = PendingIntent.getService(this, 0, ExitIntent, 0);

        Intent highIntent = new Intent(this, MusicService.class);
        highIntent.setAction(HIGH);
        PendingIntent highintent = PendingIntent.getService(this, 0, highIntent, 0);

        Intent lowIntent = new Intent(this, MusicService.class);
        lowIntent.setAction(LOW);
        PendingIntent lowintent = PendingIntent.getService(this, 0, lowIntent, 0);


        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.notifi);
        views.setOnClickPendingIntent(R.id.previewnotifi,ppre);
        views.setOnClickPendingIntent(R.id.playnotifi,pplay);
        views.setOnClickPendingIntent(R.id.nextnotifi,pnext);
        views.setOnClickPendingIntent(R.id.btnclose,eintent);
        views.setOnClickPendingIntent(R.id.backsongnotifi,lowintent);
        views.setOnClickPendingIntent(R.id.nextsongnotifi,highintent);
        views.setTextViewText(R.id.textviessongnotifi,songname);
        views.setTextViewText(R.id.textviessingernotifi,singername);
        //PendingIntent p = PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID = "com.example.musicplayer";
            String channelName = "My Background Service";
            NotificationChannel chan = null;
            chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.iconn)
                    .setContentTitle("App is running in background")
                    .setCustomBigContentView(views)
                    .setOngoing(true)
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(2, notification);
        }
        else
        {
            compat = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.playyy)
                    .setCustomBigContentView(views)
                    .setOngoing(true)
                    .build();

            startForeground(101,compat);
        }

        return START_STICKY;
    }

    Notification compat;


    @Override
    public void onDestroy() {
        if (mp!=null) {
            mp.reset();
            mp.release();
        }
        super.onDestroy();
    }


    int i = 0;
    public void next()
    {
        mp.release();
        mp = null;
        numbersound = numbersound + 1;
        mp = MediaPlayer.create(this,Uri.parse(GetMusic.getmusic(numbersound,0,0).get(0).getPath()));
        songname = GetMusic.getmusic(numbersound,0,0).get(0).getSong();
        singername =GetMusic.getmusic(numbersound,0,0).get(0).getSinger();
        mp.start();
        intentsendBroadCast.putExtra("numberSond",numbersound);
        sendBroadcast(intentsendBroadCast);
    }
    public void previous()
    {
        mp.release();
        mp = null;
        numbersound = numbersound - 1;
        mp = MediaPlayer.create(this,Uri.parse(GetMusic.getmusic(numbersound,0,0).get(0).getPath()));
        songname = GetMusic.getmusic(numbersound,0,0).get(0).getSong();
        singername = GetMusic.getmusic(numbersound,0,0).get(0).getSinger();
        mp.start();
        intentsendBroadCast.putExtra("numberSond",numbersound);
        sendBroadcast(intentsendBroadCast);
        Toast.makeText(this, ""+songname,Toast.LENGTH_SHORT).show();
    }



    public MediaPlayer getMediaPlayer()
    {
        return mp;
    }
    public int getnumbersong()
    {
        return numbersound;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        try {
            Toast.makeText(this, service.getInterfaceDescriptor()+"", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(this, name.getClassName()+"", Toast.LENGTH_SHORT).show();

    }


    public class MusicServiceBinder extends Binder
    {
        public MusicService getService()
        {
            return MusicService.this;
        }

        public int getNumber()
        {
            return numbersound;
        }
    }





    @Override
    public void onCreate(){
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
            String channelName = "My Background Service";
            NotificationChannel chan = null;
            chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.iconn)
                    .setContentTitle("App is running in background")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(2, notification);
        }
        else
            startForeground(1, new Notification());
    }

}
