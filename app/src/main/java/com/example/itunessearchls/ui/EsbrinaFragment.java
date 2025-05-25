package com.example.itunessearchls.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.itunessearchls.R;
import com.example.itunessearchls.favorite.FavoritesManager;
import com.example.itunessearchls.model.Song;
import com.example.itunessearchls.model.iTunesResponse;
import com.example.itunessearchls.api.iTunesApiClient;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EsbrinaFragment extends Fragment {

    private Button btnSubmit;
    private ImageButton btnPlay;
    private EditText etGuess;
    private TextView tvGuessedCount, tvTotalCount, tvSongProgress;
    private MediaPlayer mediaPlayer;
    private List<Song> favoriteSongs;
    private int currentSongIndex = 0;
    private int guessedCount = 0;
    private int attempts = 0;
    private static final int MAX_ATTEMPTS = 3;
    private boolean readyForNext = false;

    public EsbrinaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_esbrina, container, false);

        btnPlay = view.findViewById(R.id.btn_play);
        btnSubmit = view.findViewById(R.id.btn_submit);
        etGuess = view.findViewById(R.id.et_guess);
        tvGuessedCount = view.findViewById(R.id.tv_songs_guessed);
        tvTotalCount = view.findViewById(R.id.tv_songs_total);
        tvSongProgress = view.findViewById(R.id.tv_song_progress);

        mediaPlayer = new MediaPlayer();

        btnPlay.setOnClickListener(v -> playPreview());
        btnSubmit.setOnClickListener(v -> checkGuess(view));

        loadFavoriteSongs();

        return view;
    }

    private void loadFavoriteSongs() {
        Set<String> favIds = FavoritesManager.getFavorites(requireContext());
        if (favIds.isEmpty()) return;

        String ids = String.join(",", favIds);

        iTunesApiClient.getApiService().lookupMultipleSongs(ids)
                .enqueue(new Callback<iTunesResponse>() {
                    @Override
                    public void onResponse(Call<iTunesResponse> call, Response<iTunesResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            favoriteSongs = new ArrayList<>(response.body().getResults());
                            Collections.shuffle(favoriteSongs);
                            tvTotalCount.setText("Total de cançons: " + favoriteSongs.size());
                            tvGuessedCount.setText("Cançons endevinades: 0");
                            tvSongProgress.setText("Cançó 1 de " + favoriteSongs.size());
                        }
                    }

                    @Override
                    public void onFailure(Call<iTunesResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private void playPreview() {
        if (favoriteSongs == null || favoriteSongs.isEmpty()) return;

        if (readyForNext) {
            nextSong();
            readyForNext = false;
        }

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            return;
        }

        Song currentSong = favoriteSongs.get(currentSongIndex);
        String url = currentSong.getPreviewUrl();

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
            attempts = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkGuess(View view) {
        if (favoriteSongs == null || favoriteSongs.isEmpty()) return;

        Song currentSong = favoriteSongs.get(currentSongIndex);
        String guess = etGuess.getText().toString().trim().toLowerCase();

        if (guess.equals(currentSong.getTrackName().toLowerCase())) {
            guessedCount++;
            tvGuessedCount.setText("Cançons endevinades: " + guessedCount);
            Snackbar.make(view, "Resposta correcta!", Snackbar.LENGTH_SHORT).show();
            readyForNext = true;
        } else {
            attempts++;
            if (attempts >= MAX_ATTEMPTS) {
                Snackbar.make(view, "Resposta incorrecta. Fi d'intents.", Snackbar.LENGTH_SHORT).show();
                Snackbar.make(view, "Era: " + currentSong.getTrackName() + " de " + currentSong.getArtistName(), Snackbar.LENGTH_LONG).show();
                readyForNext = true;
            } else {
                Snackbar.make(view, "Incorrecte. Torna-ho a intentar.", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void nextSong() {
        etGuess.setText("");
        currentSongIndex++;
        if (currentSongIndex >= favoriteSongs.size()) {
            currentSongIndex = 0;
            Collections.shuffle(favoriteSongs);
        }
        tvSongProgress.setText("Cançó " + (currentSongIndex + 1) + " de " + favoriteSongs.size());
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
