package com.example.musicplayer.Utill;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.logging.Handler;

public class UtilsSeeck {

    SeekBar _seekBar;
    MediaPlayer _mp;
    String pathmusic;
    public UtilsSeeck(Activity act , Context con, final SeekBar seekBar, MediaPlayer mp , String path) {
        this._seekBar = seekBar;
        this._mp = mp;
        pathmusic = path;
        this._mp = MediaPlayer.create(con, Uri.parse(path));

        seekBar.setMax(mp.getDuration());
        final android.os.Handler handler = new android.os.Handler();
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _seekBar.setProgress(_mp.getCurrentPosition()/1000);
                handler.postDelayed(this,1000);
            }
        });
    }
    public void startt()
    {
        Log.d("start","start"+"");
        _mp.start();
    }
    public void pausee()
    {
        _mp.pause();
        Log.d("stop","stop"+"");

    }

    public void plays()
    {

    }




}
