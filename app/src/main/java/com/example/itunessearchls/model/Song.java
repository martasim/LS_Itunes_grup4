package com.example.itunessearchls.model;

import java.io.Serializable;

public class Song implements Serializable {
    private String trackName;
    private String artistName;
    private String artworkUrl100;
    private String previewUrl;
    private String collectionName;
    private String releaseDate;
    private double trackPrice;
    private double collectionPrice;
    private String primaryGenreName;
    private int collectionId;
    private int trackId;


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
    public void setTrackPrice(double trackPrice) {
        this.trackPrice = trackPrice;
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
    public int getCollectionId() {
        return collectionId;
    }
    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }
}
