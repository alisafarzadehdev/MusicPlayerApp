package com.example.musicplayer.Utill;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

import com.example.musicplayer.Recycler.ItemMusic;
import com.example.musicplayer.R;

import java.util.ArrayList;
import java.util.List;

public class GetMusic {
    static Cursor c=null;

    public static List<ItemMusic> getmusic(int p, int sample , int dencity)
    {
        Bitmap bmp = null;

        List<ItemMusic> itemMusicList=new ArrayList<>();
        if (c==null)
        {
            c=getdatafromcard();
        }
        c.moveToPosition(p);
        String track,artist,duraction,pathahang;

        track=c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));
        duraction=c.getString(c.getColumnIndex(MediaStore.Audio.Media.DURATION));
        artist=c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        pathahang = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));

       try {
           MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
           mediaMetadataRetriever.setDataSource(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA)));
           byte[] b = mediaMetadataRetriever.getEmbeddedPicture();
           BitmapFactory.Options ops = new BitmapFactory.Options();
           ops.inDensity = sample;
           ops.inSampleSize = dencity;
           bmp  = BitmapFactory.decodeByteArray(b,0,b.length,ops);
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
       if (bmp==null)
       {
           bmp = BitmapFactory.decodeResource(G.con.getResources(), R.drawable.icplaynotfound);
       }

        itemMusicList.add(new ItemMusic(artist,track,duraction,bmp,pathahang));

        return itemMusicList;
    }

    public static Cursor getdatafromcard()
    {
        ContentResolver resolver =  G.con.getContentResolver();
        String[] projection={
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };

        Cursor cursorinternal=null;
        cursorinternal = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,null,null,null);


        return cursorinternal;
    }
    public static Cursor getdatafrominternal()
    {
        ContentResolver resolver =  G.con.getContentResolver();
        String[] projection={
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };

        Cursor cursorinternal=null;
        cursorinternal = resolver.query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,projection,null,null,null);


        return cursorinternal;
    }


}
