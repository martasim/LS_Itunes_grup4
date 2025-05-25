package com.example.itunessearchls.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itunessearchls.R;
import com.example.itunessearchls.api.iTunesApiClient;
import com.example.itunessearchls.favorite.FavoritesManager;
import com.example.itunessearchls.model.Song;
import com.example.itunessearchls.model.iTunesResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SongVerticalAdapter songAdapter;
    private TextView tvEmptyMessage;

    public FavoritsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorits, container, false);

        recyclerView = view.findViewById(R.id.rv_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tvEmptyMessage = view.findViewById(R.id.tv_no_favorites);

        songAdapter = new SongVerticalAdapter(new ArrayList<>(), true, this::loadFavoriteSongs);
        recyclerView.setAdapter(songAdapter);

        loadFavoriteSongs();

        return view;
    }

    private void loadFavoriteSongs() {
        Set<String> favoriteIds = FavoritesManager.getFavorites(requireContext());

        if (favoriteIds.isEmpty()) {
            songAdapter.setSongs(new ArrayList<>());
            tvEmptyMessage.setVisibility(View.VISIBLE);
            return;
        }

        String ids = String.join(",", favoriteIds);

        iTunesApiClient.getApiService().lookupMultipleSongs(ids)
                .enqueue(new Callback<iTunesResponse>() {
                    @Override
                    public void onResponse(Call<iTunesResponse> call, Response<iTunesResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Song> songs = response.body().getResults();
                            songAdapter.setSongs(songs);
                            tvEmptyMessage.setVisibility(songs.isEmpty() ? View.VISIBLE : View.GONE);
                        } else {
                            Log.e("FavoritsFragment", "Error: " + response.code());
                            tvEmptyMessage.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<iTunesResponse> call, Throwable t) {
                        Log.e("FavoritsFragment", "Failure: " + t.getMessage());
                        tvEmptyMessage.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            loadFavoriteSongs();
        }
    }

}
