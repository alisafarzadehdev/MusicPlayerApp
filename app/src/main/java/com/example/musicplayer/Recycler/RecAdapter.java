package com.example.musicplayer.Recycler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.MusicPlayActivity;
import com.example.musicplayer.R;

import java.util.List;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.Holder> {

    public static List<ItemMusic> itemMusicList;
    Context con;
    public RecAdapter(Context context , List<ItemMusic> itemMusicList) {
        this.itemMusicList = itemMusicList;
        this.con = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutitem,null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new Holder(view);
    }

    public void search(List<ItemMusic> List)
    {
        itemMusicList.clear();
        itemMusicList.addAll(List);
        notifyDataSetChanged();
    }
    public void animateTo(List<ItemMusic> memberDatas) {
        applyAndAnimateRemovals(memberDatas);
        applyAndAnimateAdditions(memberDatas);
        applyAndAnimateMovedItems(memberDatas);
    }
    private void applyAndAnimateRemovals(List<ItemMusic> newDatas) {
        for (int i = itemMusicList.size() - 1; i >= 0; i--) {
            final ItemMusic data = itemMusicList.get(i);
            if (!newDatas.contains(data)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<ItemMusic> newDatas) {
        for (int i = 0, count = newDatas.size(); i < count; i++) {
            final ItemMusic data = newDatas.get(i);
            if (!itemMusicList.contains(data)) {
                addItem(i, data);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<ItemMusic> newDatas) {
        for (int toPosition = newDatas.size() - 1; toPosition >= 0; toPosition--) {
            final ItemMusic data = newDatas.get(toPosition);
            final int fromPosition = itemMusicList.indexOf(data);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    public ItemMusic removeItem(int position) {
        final ItemMusic data = itemMusicList.remove(position);
        notifyItemRemoved(position);
        return data;
    }

    public void addItem(int position, ItemMusic data) {
        itemMusicList.add(position, data);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ItemMusic data = itemMusicList.remove(fromPosition);
        itemMusicList.add(toPosition, data);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final int pos = position;
        holder.singer.setText(itemMusicList.get(position).getSinger());
        holder.song.setText(itemMusicList.get(position).getSong());
        //holder.time.setText(itemMusicList.get(position).getTimemudic());
        //holder.img.setImageBitmap(itemMusicList.get(position).getBmp());
        Glide.with(con).load(itemMusicList.get(position).getBmp()).into(holder.img);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(con, MusicPlayActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("posid",pos+1);
                con.startActivity(i);
            }
        });
    }

    public void setdatas(List<ItemMusic> musics)
    {
        itemMusicList.addAll(musics);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemMusicList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        public TextView singer,song;
        public ImageView img;
        ConstraintLayout constraintLayout;
        public Holder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgcover);
            singer = itemView.findViewById(R.id.singertextview);
            song = itemView.findViewById(R.id.songtextview);
            constraintLayout = itemView.findViewById(R.id.constraint);
        }
    }
}
