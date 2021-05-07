package com.example.musicplayer.Utill;

import android.app.Application;
import android.content.Context;

public class G extends Application {

    static Context con;
    @Override
    public void onCreate() {
        super.onCreate();

        con = getApplicationContext();
    }
}
