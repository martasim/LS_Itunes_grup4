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

    import nl.dionsegijn.konfetti.core.Party;
    import nl.dionsegijn.konfetti.core.Rotation;
    import nl.dionsegijn.konfetti.core.models.Shape;
    import nl.dionsegijn.konfetti.core.emitter.Emitter;
    import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
    import nl.dionsegijn.konfetti.xml.KonfettiView;

    import java.util.Arrays;
    import java.util.concurrent.TimeUnit;

    import java.util.Arrays;
    import java.util.concurrent.TimeUnit;


    import com.example.itunessearchls.favorite.FavoritesManager;
    import com.example.itunessearchls.model.Song;
    import com.example.itunessearchls.model.iTunesResponse;

    import nl.dionsegijn.konfetti.core.Party;
    import nl.dionsegijn.konfetti.core.emitter.Emitter;
    import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
    import nl.dionsegijn.konfetti.core.models.Shape;
    import nl.dionsegijn.konfetti.xml.KonfettiView;


    import com.example.itunessearchls.api.iTunesApiClient;
    import com.google.android.material.snackbar.Snackbar;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Collections;
    import java.util.List;
    import java.util.Set;
    import java.util.concurrent.TimeUnit;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;
    import nl.dionsegijn.konfetti.core.models.Size;

    import nl.dionsegijn.konfetti.core.Party;
    import nl.dionsegijn.konfetti.core.models.Shape;
    import nl.dionsegijn.konfetti.core.Position;
    import java.util.Arrays;



    public class EsbrinaFragment extends Fragment {

        private Button btnSubmit;
        private ImageButton btnPlay;
        private EditText etGuess;
        private TextView tvGuessedCount, tvTotalCount, tvSongProgress;
        private MediaPlayer mediaPlayer;
        private List<Song> favoriteSongs;
        private int currentSongIndex = 0;
        private int guessedCount = 0;
        private Button btnRetry;
        private KonfettiView konfettiView;

        private int attempts = 0;
        private TextView tvErrorPercentage;
        private int totalErrors = 0;


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
            btnRetry = view.findViewById(R.id.btn_retry);
            btnRetry.setOnClickListener(v -> restartGame());
            konfettiView = view.findViewById(R.id.konfettiView);
            konfettiView.bringToFront();

            tvErrorPercentage = view.findViewById(R.id.tv_error_percentage);


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
                launchConfetti();
                moveToNextOrEnd();
            } else {
                attempts++;
                totalErrors++;
                updateErrorPercentage();

                if (attempts >= MAX_ATTEMPTS) {
                    Snackbar.make(view, "Resposta incorrecta. Fi d'intents.", Snackbar.LENGTH_SHORT).show();
                    Snackbar.make(view, "Era: " + currentSong.getTrackName() + " de " + currentSong.getArtistName(), Snackbar.LENGTH_LONG).show();
                    moveToNextOrEnd();
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

        private void updateErrorPercentage() {
            int totalPossibleAttempts = favoriteSongs.size() * MAX_ATTEMPTS;
            int percentage = (int) ((totalErrors / (float) totalPossibleAttempts) * 100);
            tvErrorPercentage.setText("Errors: " + totalErrors + " (" + percentage + "%)");
        }


        private void moveToNextOrEnd() {
            attempts = 0;
            etGuess.setText("");
            currentSongIndex++;

            if (currentSongIndex >= favoriteSongs.size()) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.reset();
                Snackbar.make(requireView(), "Has completat el joc!", Snackbar.LENGTH_LONG).show();
                etGuess.setEnabled(false);
                btnSubmit.setEnabled(false);
                btnPlay.setEnabled(false);
                tvSongProgress.setText("Joc completat!");
                btnRetry.setVisibility(View.VISIBLE);
            } else {
                tvSongProgress.setText("Cançó " + (currentSongIndex + 1) + " de " + favoriteSongs.size());
                mediaPlayer.reset();
            }
        }


        private void restartGame() {
            guessedCount = 0;
            totalErrors = 0;
            attempts = 0;
            currentSongIndex = 0;

            Collections.shuffle(favoriteSongs);

            etGuess.setEnabled(true);
            btnSubmit.setEnabled(true);
            btnPlay.setEnabled(true);
            btnRetry.setVisibility(View.GONE);

            tvGuessedCount.setText("Cançons endevinades: 0");
            tvSongProgress.setText("Cançó 1 de " + favoriteSongs.size());
            updateErrorPercentage();
        }

        private void launchConfetti() {
            Party p0 = makeParty(0f);
            Party p1 = makeParty(0.25f);
            Party p2 = makeParty(0.5f);
            Party p3 = makeParty(0.75f);
            Party p4 = makeParty(1f);

            konfettiView.start(p0, p1, p2, p3, p4);
        }

        private Party makeParty(float relativeX) {


            EmitterConfig emitter = new Emitter(450, TimeUnit.MILLISECONDS).max(120);

            return new Party(
                    270,
                    120,
                    12f, 18f, 0.98f,
                    Collections.singletonList(new Size(18, 4f, 1.5f)),
                    Arrays.asList(
                            0xFFF44336, 0xFFFFC107, 0xFF4CAF50,
                            0xFF03A9F4, 0xFFE91E63, 0xFFFF5722
                    ),
                    Arrays.asList(Shape.Circle.INSTANCE, Shape.Square.INSTANCE),
                    3500L,
                    false,
                    new Position.Relative(relativeX, 0f),
                    0,
                    new Rotation(),
                    emitter
            );
        }








    }

