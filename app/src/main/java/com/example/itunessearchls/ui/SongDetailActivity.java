package com.example.itunessearchls.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.itunessearchls.R;
import com.example.itunessearchls.favorite.FavoritesManager;
import com.example.itunessearchls.model.Song;

public class SongDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvArtist, tvAlbum, tvReleaseDate, tvGenre, tvPrice;
    private ImageView ivArtwork;
    private ToggleButton btnFavorite;
    private Song song;

    private boolean wasFavoriteBefore;

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

            Glide.with(this).load(song.getArtworkUrl100()).into(ivArtwork);

            wasFavoriteBefore = FavoritesManager.isFavorite(this, song.getTrackId());
            btnFavorite.setChecked(wasFavoriteBefore);
        }

        updateFavoriteUI();

        btnFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (song == null) return;
            if (isChecked) {
                FavoritesManager.addFavorite(this, song.getTrackId());
            } else {
                FavoritesManager.removeFavorite(this, song.getTrackId());
            }
            updateFavoriteUI();
        });
    }

    private void updateFavoriteUI() {
        boolean isFav = FavoritesManager.isFavorite(this, song.getTrackId());
        btnFavorite.setChecked(isFav);
        String buttonText = isFav ? "★ FAVORITS" : "☆ AFEGIR A FAVORITS";
        btnFavorite.setText(buttonText);
        btnFavorite.setTextOn(buttonText);
        btnFavorite.setTextOff(buttonText);
    }

    @Override
    public void onBackPressed() {
        boolean isNowFavorite = FavoritesManager.isFavorite(this, song.getTrackId());
        Intent resultIntent = new Intent();
        resultIntent.putExtra("trackId", song.getTrackId());
        resultIntent.putExtra("isFavorite", isNowFavorite);
        setResult(RESULT_OK, resultIntent);
        super.onBackPressed();
    }
}
