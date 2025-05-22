package com.example.itunessearchls.model;

public class Song {
    private String trackName;
    private String artistName;
    private String artworkUrl100;
    private String previewUrl;
    private String collectionName;
    private String releaseDate;
    private double trackPrice;
    private double collectionPrice;
    private String primaryGenreName;

    public String getTrackName() {
        return trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtworkUrl100() {
        return artworkUrl100;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getTrackPrice() {
        return trackPrice;
    }

    public String getPrimaryGenreName() {
        return primaryGenreName;
    }
    public Double getCollectionPrice() {
        return collectionPrice;
    }

    public void setCollectionPrice(Double collectionPrice) {
        this.collectionPrice = collectionPrice;
    }
}
