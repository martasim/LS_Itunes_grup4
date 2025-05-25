package com.example.itunessearchls.api;

import com.example.itunessearchls.model.Song;
import com.example.itunessearchls.model.iTunesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface iTunesApiService {

    @GET("search")
    Call<iTunesResponse> searchSongs(
            @Query("term") String term,
            @Query("media") String media,
            @Query("entity") String entity,
            @Query("limit") int limit
    );



    @GET("search")
    Call<iTunesResponse> searchAlbumsSimple(
            @Query("term") String term,
            @Query("media") String media,
            @Query("entity") String entity,
            @Query("limit") int limit
    );


    @GET("search")
    Call<iTunesResponse> searchAlbums(
            @Query("term") String term,
            @Query("media") String media,
            @Query("entity") String entity,
            @Query("limit") int limit,
            @Query("offset") int offset
    );

    @GET("lookup")
    Call<iTunesResponse> lookupAlbum(
            @Query("id") int id,
            @Query("entity") String entity
    );





}
