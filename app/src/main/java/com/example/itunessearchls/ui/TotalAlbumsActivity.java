package com.example.itunessearchls.ui;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.itunessearchls.R;
import com.example.itunessearchls.api.iTunesApiClient;
import com.example.itunessearchls.model.Album;
import com.example.itunessearchls.model.Song;
import com.example.itunessearchls.model.iTunesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class TotalAlbumsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlbumAdapter albumAdapter;
    private boolean isLoading = false;
    private int offset = 0;
    private final int PAGE_SIZE = 15;
    private String searchTerm = "music"; // valor por defecto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_albums);

        searchTerm = getIntent().getStringExtra("search_term");
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            searchTerm = "music";
        }

        recyclerView = findViewById(R.id.rv_total_albums);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        albumAdapter = new AlbumAdapter(new ArrayList<>(), true);
        recyclerView.setAdapter(albumAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);
                if (!rv.canScrollVertically(1) && !isLoading) {
                    loadMoreAlbums();
                }
            }
        });

        loadMoreAlbums();
    }

    private void loadMoreAlbums() {
        isLoading = true;
        iTunesApiClient.getApiService().searchAlbums(searchTerm, "music", "album", PAGE_SIZE, offset)
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
                                album.setCollectionPrice(s.getCollectionPrice());

                                album.setCollectionId(s.getCollectionId());

                                albums.add(album);
                            }
                            albumAdapter.addAlbums(albums);
                            offset += PAGE_SIZE;
                        }
                        isLoading = false;
                    }

                    @Override
                    public void onFailure(Call<iTunesResponse> call, Throwable t) {
                        Log.e("TotalAlbumsActivity", "Error: " + t.getMessage());
                        isLoading = false;
                    }
                });
    }
}
