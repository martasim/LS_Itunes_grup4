package com.example.itunessearchls.model;

import java.util.List;

public class iTunesResponse {
    private int resultCount;
    private List<Song> results;

    public int getResultCount() {
        return resultCount;
    }

    public List<Song> getResults() {
        return results;
    }
}
