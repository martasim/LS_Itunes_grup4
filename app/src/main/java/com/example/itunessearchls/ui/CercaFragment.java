package com.example.itunessearchls.ui;

import android.content.Intent;
import android.os.Bundle;
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


        rvAlbums = view.findViewById(R.id.rv_albums);
        rvAlbums.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        albumAdapter = new AlbumAdapter(new ArrayList<Album>());
        rvAlbums.setAdapter(albumAdapter);

        EditText etSearch = view.findViewById(R.id.et_search);
        TextView btnVeureTotes = view.findViewById(R.id.btn_veure_totes);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, android.view.KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    String term = etSearch.getText().toString().trim();
                    if (!term.isEmpty()) {
                        searchSongs(term);
                        searchAlbums(term);
                    }
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
        iTunesApiClient.getApiService().searchAlbums("drake", "music", "album", 15)
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
        iTunesApiClient.getApiService().searchSongs(term, "music", "musicTrack", 15)
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

    private void searchAlbums(String term) {
        iTunesApiClient.getApiService().searchAlbums(term, "music", "album", 15)
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
                        t.printStackTrace();
                    }
                });
    }


}
