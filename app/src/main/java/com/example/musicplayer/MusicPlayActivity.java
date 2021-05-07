package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.musicplayer.Service.MusicService;
import com.example.musicplayer.Utill.GetMusic;
import com.example.musicplayer.Recycler.ItemMusic;

import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MusicPlayActivity extends AppCompatActivity implements ServiceConnection {

    int numbersound = 0;
    ImageView playpause;
    LottieAnimationView effect;
    boolean playorpause = false;
    TextView end, start, singer, song;
    ImageView img, next, prevooius, imglayout;
    SeekBar seekBar;
    List<ItemMusic> m;
    Boolean b = false;
    Intent ibind;
    public final String PLAY = "com.ex ample.musicplayer.PLAY";
    Recier receive ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        init();
        receive = new Recier(new Handler());
        numbersound = getIntent().getIntExtra("posid", 0);
        m = GetMusic.getmusic(numbersound, 0, 0);
        end.setText(m.get(0).getTimemudic());
        start.setText("00:00");
        singer.setText(m.get(0).getSinger());
        song.setText(m.get(0).getSong());
        Bitmap bmp = m.get(0).getBmp();


        Glide.with(this).asDrawable()
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(55)))
                .load(bmp).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                imglayout.setBackground(resource);

            }
        });
        Glide.with(this).applyDefaultRequestOptions(RequestOptions.bitmapTransform(new CircleCrop())).load(bmp).into(img);


        try {
            ibind = new Intent(MusicPlayActivity.this, MusicService.class);
            stopService(ibind);
            unbindService(MusicPlayActivity.this);
        } catch (Exception e) {

        }

        ibind = new Intent(MusicPlayActivity.this, MusicService.class);
        ibind.putExtra("music", m.get(0).getPath());
        ibind.setAction("com.example.musicplayer.PLAY");
        ibind.putExtra("Number", numbersound);
        bindService(ibind, MusicPlayActivity.this, BIND_AUTO_CREATE);

        registerReceiver(receive,new IntentFilter("intentsendBroadCastAlisafarzadeh"));



        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playorpause) {
                    playpause.setImageResource(R.drawable.playyy);
                    effect.pauseAnimation();
                    playorpause = false;
                    Log.d("rrr", "pause");
                    mp.pause();
                    img.clearAnimation();

                } else {

                    setanimationimg(img);
                    effect.playAnimation();
                    playpause.setImageResource(R.drawable.pauseee);
                    playorpause = true;
                    ibind.setAction(PLAY);
                    startService(ibind);
                }
            }
        });

        final Handler h = new Handler();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    seekBar.setProgress(mp.getCurrentPosition());
                    counttime(start, mp);
                    endtime(end, mp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                h.postDelayed(this, 1000);
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mp.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


    }

    public void setanimationimg(View v) {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setDuration(8000);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        v.startAnimation(rotateAnimation);
    }


    public void init() {
        end = findViewById(R.id.enttime);
        start = findViewById(R.id.starttime);
        singer = findViewById(R.id.singertextplay);
        song = findViewById(R.id.songtextplay);
        img = findViewById(R.id.imageView);
        next = findViewById(R.id.imgnext);
        prevooius = findViewById(R.id.imgpreviuos);
        seekBar = findViewById(R.id.seekBar);
        imglayout = findViewById(R.id.imgbacks);
        playpause = findViewById(R.id.imgview);
        effect = findViewById(R.id.soundeffect);
    }

    private void counttime(TextView tv, MediaPlayer mp) {
        if (mp != null) {
            long min = TimeUnit.MILLISECONDS.toMinutes(mp.getCurrentPosition());
            long sec = TimeUnit.MILLISECONDS.toSeconds(mp.getCurrentPosition()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mp.getCurrentPosition()));
            tv.setText(String.format("%02d : %02d", min, sec));

        }
    }

    private void endtime(TextView tv, MediaPlayer mp) {
        if (mp != null) {
            long min = TimeUnit.MILLISECONDS.toMinutes(mp.getDuration());
            long sec = TimeUnit.MILLISECONDS.toSeconds(mp.getDuration()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mp.getDuration()));
            tv.setText(String.format("%02d : %02d", min, sec));
        }
    }


    MediaPlayer mp;
    String musicurl;

    MusicService.MusicServiceBinder mbinder;
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        try {
            mbinder = (MusicService.MusicServiceBinder) service;
            MusicService musicService = mbinder.getService();
            mp = musicService.getMediaPlayer();
            seekBar.setMax(mp.getDuration());

        } catch (Exception e) {
        }

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Toast.makeText(this, "onServiceDisconnected", Toast.LENGTH_SHORT).show();
    }




    public class Recier extends BroadcastReceiver
    {
        private final Handler handler; // Handler used to execute code on the UI thread

        public Recier(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            numbersound =intent.getExtras().getInt("numberSond");
            m = GetMusic.getmusic(numbersound, 0, 0);
            end.setText(m.get(0).getTimemudic());
            start.setText("00:00");
            singer.setText(m.get(0).getSinger());
            song.setText(m.get(0).getSong());
            Bitmap bmp = m.get(0).getBmp();

            mp = mbinder.getService().getMediaPlayer();



            try {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    seekBar.setProgress(mp.getCurrentPosition());
                    counttime(start, mp);
                    endtime(end, mp);
                }
            },1000);

            }catch (Exception e)
            {
                e.getMessage();
            }


            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mp.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

        }
    }




    @Override
    protected void onPause() {
        try {
            unbindService(MusicPlayActivity.this);
        }catch (Exception e)
        {

        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receive);
        super.onDestroy();
    }
}
