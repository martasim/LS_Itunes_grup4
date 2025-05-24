package com.example.itunessearchls.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.itunessearchls.R;
import com.example.itunessearchls.model.Song;
import com.example.itunessearchls.favorite.FavoritesManager;

public class SongDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvArtist, tvAlbum, tvReleaseDate, tvGenre, tvPrice;
    private ImageView ivArtwork;
    private ToggleButton btnFavorite;
    private Song song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        tvTitle = findViewById(R.id.tv_song_title);
        tvArtist = findViewById(R.id.tv_song_artist);
        tvAlbum = findViewById(R.id.tv_song_album);
        tvReleaseDate = findViewById(R.id.tv_song_release_date);
        tvGenre = findViewById(R.id.tv_song_genre);
        tvPrice = findViewById(R.id.tv_song_price);
        ivArtwork = findViewById(R.id.iv_song_artwork);
        btnFavorite = findViewById(R.id.btn_favorite);

        song = (Song) getIntent().getSerializableExtra("song");

        if (song != null) {
            tvTitle.setText(song.getTrackName());
            tvArtist.setText(song.getArtistName());
            tvAlbum.setText(song.getCollectionName());
            tvReleaseDate.setText(song.getReleaseDate().split("T")[0]);
            tvGenre.setText(song.getPrimaryGenreName());

            if (song.getTrackPrice() > 0) {
                tvPrice.setText(song.getTrackPrice() + " €");
            } else {
                tvPrice.setText("—");
            }

            Glide.with(this)
                    .load(song.getArtworkUrl100())
                    .into(ivArtwork);

            btnFavorite.setChecked(FavoritesManager.isFavorite(this, song.getTrackId()));
        }

        btnFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (song == null) return;
            if (isChecked) {
                FavoritesManager.addFavorite(this, song.getTrackId());
            } else {
                FavoritesManager.removeFavorite(this, song.getTrackId());
            }
        });
    }
}
