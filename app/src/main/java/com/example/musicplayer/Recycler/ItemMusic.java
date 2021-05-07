package com.example.musicplayer.Recycler;

import android.graphics.Bitmap;

public class ItemMusic {

    String singer,song,timemudic,path;
    Bitmap bmp;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ItemMusic(String singer, String song, String timemudic, Bitmap bmp,String path) {
        this.singer = singer;
        this.song = song;
        this.timemudic = timemudic;
        this.bmp = bmp;
        this.path = path;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getTimemudic() {
        return timemudic;
    }

    public void setTimemudic(String timemudic) {
        this.timemudic = timemudic;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }
}
