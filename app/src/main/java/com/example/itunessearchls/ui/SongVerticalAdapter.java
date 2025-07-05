package com.example.itunessearchls.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.itunessearchls.R;
import com.example.itunessearchls.favorite.FavoritesManager;
import com.example.itunessearchls.model.Song;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SongVerticalAdapter extends RecyclerView.Adapter<SongVerticalAdapter.SongViewHolder> {

    private List<Song> songList;
    private boolean isFromFavorites;
    private Runnable onFavoritesChanged;

    public SongVerticalAdapter(List<Song> songList, boolean isFromFavorites, Runnable onFavoritesChanged) {
        this.songList = songList;
        this.isFromFavorites = isFromFavorites;
        this.onFavoritesChanged = onFavoritesChanged;
    }

    public void setSongs(List<Song> songs) {
        this.songList = songs;
        notifyDataSetChanged();
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_vertical, parent, false);
        return new SongViewHolder(view);
    }
    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = songList.get(position);
        Context context = holder.itemView.getContext();

        holder.tvTitle.setText(song.getTrackName());
        holder.tvArtist.setText(song.getArtistName());

        Picasso.get().load(song.getArtworkUrl100()).into(holder.ivArtwork);

        holder.btnFavorite.setOnCheckedChangeListener(null);

        boolean isFav = FavoritesManager.isFavorite(context, song.getTrackId());
        holder.btnFavorite.setChecked(isFav);

        holder.btnFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                FavoritesManager.addFavorite(context, song.getTrackId());
            } else {
                FavoritesManager.removeFavorite(context, song.getTrackId());
                if (isFromFavorites) {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && pos < songList.size()) {
                        songList.remove(pos);
                        notifyItemRemoved(pos);
                    }
                }
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SongDetailActivity.class);
            intent.putExtra("song", song);

            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, 101);
            } else {
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return songList != null ? songList.size() : 0;
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView ivArtwork;
        TextView tvTitle, tvArtist;
        ToggleButton btnFavorite;

        public SongViewHolder(View itemView) {
            super(itemView);
            ivArtwork = itemView.findViewById(R.id.iv_artwork);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvArtist = itemView.findViewById(R.id.tv_artist);
            btnFavorite = itemView.findViewById(R.id.btn_favorite);
        }
    }
}
