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
import com.example.itunessearchls.model.Album;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private List<Album> albums;
    private boolean isVerticalLayout;

    public AlbumAdapter(List<Album> albums, boolean isVerticalLayout) {
        this.albums = albums;
        this.isVerticalLayout = isVerticalLayout;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }

    public void addAlbums(List<Album> newAlbums) {
        int start = albums.size();
        albums.addAll(newAlbums);
        notifyItemRangeInserted(start, newAlbums.size());
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = isVerticalLayout ? R.layout.item_album_vertical : R.layout.item_album;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        Album album = albums.get(position);
        holder.albumTitle.setText(album.getCollectionName());
        holder.albumArtist.setText(album.getArtistName());

        if (album.getCollectionPrice() != null) {
            holder.albumPrice.setText(album.getCollectionPrice() + " €");
        } else {
            holder.albumPrice.setText("—");
        }

        Glide.with(holder.itemView.getContext())
                .load(album.getArtworkUrl100())
                .into(holder.albumImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AlbumSongsActivity.class);
            intent.putExtra("collection_id", albums.get(position).getCollectionId());
            v.getContext().startActivity(intent);
        });



    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView albumImage;
        TextView albumTitle, albumArtist, albumPrice;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.album_art);
            albumTitle = itemView.findViewById(R.id.album_title);
            albumArtist = itemView.findViewById(R.id.album_artist);
            albumPrice = itemView.findViewById(R.id.album_price);
        }
    }
}
