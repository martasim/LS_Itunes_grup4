package com.example.itunessearchls.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itunessearchls.R;
import com.example.itunessearchls.model.Song;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<Song> songList;

    public SongAdapter(List<Song> songList) {
        this.songList = songList;
    }

    public void setSongs(List<Song> songs) {
        this.songList = songs;
        notifyDataSetChanged();
    }

    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(itemView);
    }

    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = songList.get(position);

        holder.tvTitle.setText(song.getTrackName());
        holder.tvArtist.setText(song.getArtistName());

        Glide.with(holder.itemView.getContext())
                .load(song.getArtworkUrl100())
                .into(holder.ivArtwork);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SongDetailActivity.class);
            intent.putExtra("song", song);
            v.getContext().startActivity(intent);
        });
    }

    public int getItemCount() {
        return songList != null ? songList.size() : 0;
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView ivArtwork;
        TextView tvTitle, tvArtist;

        public SongViewHolder(View itemView) {
            super(itemView);
            ivArtwork = itemView.findViewById(R.id.iv_artwork);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvArtist = itemView.findViewById(R.id.tv_artist);
        }
    }
}
