package com.example.itunessearchls.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itunessearchls.R;
import com.example.itunessearchls.model.Album;
import com.example.itunessearchls.model.Song;

import com.example.itunessearchls.api.iTunesApiClient;
import com.example.itunessearchls.model.iTunesResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import java.util.ArrayList;
import java.util.List;

public class CercaFragment extends Fragment {

    private RecyclerView rvSongs;
    private RecyclerView rvAlbums;
    private SongAdapter songAdapter;
    private AlbumAdapter albumAdapter;

    public CercaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cerca, container, false);


        rvSongs = view.findViewById(R.id.rv_songs);
        rvSongs.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        songAdapter = new SongAdapter(new ArrayList<Song>());
        rvSongs.setAdapter(songAdapter);
        songAdapter.notifyDataSetChanged();


        rvAlbums = view.findViewById(R.id.rv_albums);
        rvAlbums.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        albumAdapter = new AlbumAdapter(new ArrayList<>(), false);
        rvAlbums.setAdapter(albumAdapter);
        albumAdapter.notifyDataSetChanged();

        EditText etSearch = view.findViewById(R.id.et_search);
        TextView btnVeureTotes = view.findViewById(R.id.btn_veure_totes);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, android.view.KeyEvent event) {
                String term = etSearch.getText().toString().trim();
                Log.d("CercaFragment", "onEditorAction fired: " + actionId + " | term: " + term);

                if (!term.isEmpty()) {
                    searchSongs(term);
                    searchAlbums(term);
                    return true;
                }
                return false;
            }
        });




        btnVeureTotes.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TotalCanconsActivity.class);
            intent.putExtra("search_term", etSearch.getText().toString());
            startActivity(intent);
        });

        TextView btnVeureTotsAlbums = view.findViewById(R.id.btn_veure_tots_albums);

        btnVeureTotsAlbums.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TotalAlbumsActivity.class);
            intent.putExtra("search_term", etSearch.getText().toString().trim());
            startActivity(intent);
        });



        loadSongs();
        loadAlbums();


        return view;
    }

    private void loadSongs() {
        iTunesApiClient.getApiService().searchSongs("drake", "music", "musicTrack", 15)
                .enqueue(new Callback<iTunesResponse>() {
                    @Override
                    public void onResponse(Call<iTunesResponse> call, Response<iTunesResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            songAdapter.setSongs(response.body().getResults());
                        }
                    }

                    @Override
                    public void onFailure(Call<iTunesResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private void loadAlbums() {
        iTunesApiClient.getApiService().searchAlbumsSimple("drake", "music", "album", 15)
                .enqueue(new Callback<iTunesResponse>() {
                    @Override
                    public void onResponse(Call<iTunesResponse> call, Response<iTunesResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Filtrem només els resultats que realment tenen collectionName
                            List<Album> albums = new ArrayList<>();
                            for (Song s : response.body().getResults()) {
                                Album album = new Album();
                                album.setCollectionName(s.getCollectionName());
                                album.setArtistName(s.getArtistName());
                                album.setArtworkUrl100(s.getArtworkUrl100());
                                albums.add(album);
                            }
                            albumAdapter.setAlbums(albums);
                        }
                    }

                    @Override
                    public void onFailure(Call<iTunesResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private void searchSongs(String term) {
        Log.d("API_JSON", "Term: " + term);
        iTunesApiClient.getApiService().searchSongs(term, "music", "musicTrack", 15)
                .enqueue(new Callback<iTunesResponse>() {
                    @Override
                    public void onResponse(Call<iTunesResponse> call, Response<iTunesResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Song> songs = response.body().getResults();
                            Log.d("CercaFragment", "Songs received: " + songs.size());
                            songAdapter.setSongs(songs);
                        } else {
                            Log.e("CercaFragment", "Error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<iTunesResponse> call, Throwable t) {
                        Log.e("CercaFragment", "Failure: " + t.getMessage());
                        t.printStackTrace();
                    }
                });
    }

    private void searchAlbums(String term) {

        Log.d("API_JSON", "Term: " + term);
        iTunesApiClient.getApiService().searchAlbumsSimple(term, "music", "album", 15)
                .enqueue(new Callback<iTunesResponse>() {
                    @Override
                    public void onResponse(Call<iTunesResponse> call, Response<iTunesResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Album> albums = new ArrayList<>();
                            for (Song s : response.body().getResults()) {
                                Album album = new Album();
                                album.setCollectionName(s.getCollectionName());
                                album.setArtistName(s.getArtistName());
                                album.setArtworkUrl100(s.getArtworkUrl100());
                                albums.add(album);
                            }
                            albumAdapter.setAlbums(albums);
                        }
                    }

                    @Override
                    public void onFailure(Call<iTunesResponse> call, Throwable t) {
                        Log.e("API_ERROR", "Fallo al llamar API: " + t.getMessage());
                        t.printStackTrace();
                    }
                });
    }


}
