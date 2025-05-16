package com.example.itunessearchls;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itunessearchls.R;
import com.example.itunessearchls.api.iTunesApiClient;
import com.example.itunessearchls.model.Song;
import com.example.itunessearchls.model.iTunesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "API_TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        iTunesApiClient.getApiService().searchSongs("drake", "music", "musicTrack", 15)
                .enqueue(new Callback<iTunesResponse>() {
                    @Override
                    public void onResponse(Call<iTunesResponse> call, Response<iTunesResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Song> songs = response.body().getResults();
                            for (Song song : songs) {
                                Log.d(TAG, "🎵 " + song.getTrackName() + " - " + song.getArtistName());
                            }
                        } else {
                            Log.e(TAG, "Error en la respuesta");
                        }
                    }

                    @Override
                    public void onFailure(Call<iTunesResponse> call, Throwable t) {
                        Log.e(TAG, "Error en la llamada: " + t.getMessage());
                    }
                });
    }
}
