package com.example.itunessearchls.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class iTunesApiClient {

    private static final String BASE_URL = "https://itunes.apple.com/";
    private static iTunesApiService apiService;

    public static iTunesApiService getApiService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(iTunesApiService.class);
        }
        return apiService;
    }
}
