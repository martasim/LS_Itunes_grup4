package com.example.itunessearchls.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itunessearchls.R;
import com.example.itunessearchls.api.iTunesApiClient;
import com.example.itunessearchls.model.Song;
import com.example.itunessearchls.model.iTunesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumSongsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SongVerticalAdapter songAdapter;
    private int collectionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_songs);

        collectionId = getIntent().getIntExtra("collection_id", -1);
        if (collectionId == -1) {
            Toast.makeText(this, "Error: ID de álbum no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView = findViewById(R.id.rv_album_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songAdapter = new SongVerticalAdapter(new ArrayList<>(), false, null);

        recyclerView.setAdapter(songAdapter);

        fetchAlbumSongs(collectionId);
    }

    private void fetchAlbumSongs(int id) {
        iTunesApiClient.getApiService().lookupAlbum(id, "song")
                .enqueue(new Callback<iTunesResponse>() {
                    @Override
                    public void onResponse(Call<iTunesResponse> call, Response<iTunesResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Song> songs = response.body().getResults();
                            if (songs.size() > 1) {
                                songs.remove(0);
                                songAdapter.setSongs(songs);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<iTunesResponse> call, Throwable t) {
                        Log.e("AlbumSongsActivity", "Error al cargar canciones: " + t.getMessage());
                    }
                });
    }
}
