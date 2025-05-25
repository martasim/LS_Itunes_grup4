package com.example.itunessearchls.ui;

import android.os.Bundle;
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

public class TotalCanconsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SongVerticalAdapter adapter;
    private List<Song> songList = new ArrayList<>();
    private String searchTerm = "drake"; // per defecte

    private int currentPage = 1;
    private final int PAGE_SIZE = 15;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_cancons);

        recyclerView = findViewById(R.id.rv_total_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SongVerticalAdapter(songList, false, null);

        recyclerView.setAdapter(adapter);


        if (getIntent() != null && getIntent().hasExtra("search_term")) {
            searchTerm = getIntent().getStringExtra("search_term");
        }

        carregarCançons(currentPage);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && !isLastPage) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        currentPage++;
                        carregarCançons(currentPage);
                    }
                }
            }
        });
    }

    private void carregarCançons(int page) {
        isLoading = true;
        int limit = PAGE_SIZE * page;

        iTunesApiClient.getApiService().searchSongs(searchTerm, "music", "musicTrack", limit)
                .enqueue(new Callback<iTunesResponse>() {
                    @Override
                    public void onResponse(Call<iTunesResponse> call, Response<iTunesResponse> response) {
                        isLoading = false;

                        if (response.isSuccessful() && response.body() != null) {
                            List<Song> allResults = response.body().getResults();
                            List<Song> newResults = allResults.subList(
                                    (page - 1) * PAGE_SIZE,
                                    Math.min(page * PAGE_SIZE, allResults.size())
                            );

                            if (newResults.isEmpty()) {
                                isLastPage = true;
                                return;
                            }

                            songList.addAll(newResults);
                            adapter.setSongs(songList);
                        } else {
                            Toast.makeText(TotalCanconsActivity.this, "Error en la resposta", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<iTunesResponse> call, Throwable t) {
                        isLoading = false;
                        Toast.makeText(TotalCanconsActivity.this, "Error de connexió", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
