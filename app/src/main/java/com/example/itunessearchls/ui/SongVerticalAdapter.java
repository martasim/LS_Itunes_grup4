package com.example.itunessearchls.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.itunessearchls.R;
import com.example.itunessearchls.model.Song;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SongVerticalAdapter extends RecyclerView.Adapter<SongVerticalAdapter.SongViewHolder> {

    private List<Song> songList;

    public SongVerticalAdapter(List<Song> songList) {
        this.songList = songList;
    }

    public void setSongs(List<Song> songs) {
        this.songList = songs;
        notifyDataSetChanged();
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song_vertical, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.tvTitle.setText(song.getTrackName());
        holder.tvArtist.setText(song.getArtistName());

        Picasso.get()
                .load(song.getArtworkUrl100())
                .into(holder.ivArtwork);
    }

    @Override
    public int getItemCount() {
        if (songList == null) {
            return 0;
        }
        return songList.size();
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
