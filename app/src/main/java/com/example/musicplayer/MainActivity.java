package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.musicplayer.Recycler.ItemMusic;
import com.example.musicplayer.Recycler.RecAdapter;
import com.example.musicplayer.Utill.GetMusic;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    RecAdapter adapter;
    List<ItemMusic> itemMusic= new ArrayList<>();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cursor ci = GetMusic.getdatafrominternal();
        Cursor cc = GetMusic.getdatafromcard();
        Toast.makeText(this, ci.getCount()+"", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, cc.getCount()+"", Toast.LENGTH_SHORT).show();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        adapter = new RecAdapter(getApplicationContext(), itemMusic);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setdata();



        Log.d("tagggd","ol");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void setdata()
    {

        Handler h = new Handler(Looper.getMainLooper())
        {
            {
                int count = GetMusic.getdatafromcard().getCount();
                for (int i = 0; i <= count; i += 4) {
                    for (int j = 1; j <= 4; j++) {
                        try {
                            itemMusic.addAll(GetMusic.getmusic(j + i, 300, 15));
                            Log.d("BBBNumber", " " + (j + i));
                        }
                        catch (Exception e) {
                            // Toast.makeText(MainActivity.this, e.toString()+"\n"+e.getMessage()+":\n"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        };

        /*
        Dexter.withActivity(MainActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Handler h = new Handler(Looper.getMainLooper())
                        {
                            {
                                int count = GetMusic.getdatafromcard().getCount();
                                for (int i = 0; i <= count; i += 4) {
                                    for (int j = 1; j <= 4; j++) {
                                        try {
                                            itemMusic.addAll(GetMusic.getmusic(j + i, 300, 15));
                                            Log.d("BBBNumber", " " + (j + i));
                                        }
                                        catch (Exception e) {
                                           // Toast.makeText(MainActivity.this, e.toString()+"\n"+e.getMessage()+":\n"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                        };
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.w, menu);
        final MenuItem searchItem = menu.findItem(R.id.searchid);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        {

        }
        return true;
    }



    @Override
    public boolean onQueryTextChange(String newText) {
        List<ItemMusic> musicsearch = new ArrayList<>();
        for (ItemMusic it : itemMusic) {
            if (it.getSong().toUpperCase().contains(newText.toUpperCase())) {
                musicsearch.add(it);
            }
        }
        adapter.search(musicsearch);

        if (newText.isEmpty())
        {
            Log.d("ddd",newText+"");
            itemMusic.clear();
            setdata();
        }
        return true;
    }
}
