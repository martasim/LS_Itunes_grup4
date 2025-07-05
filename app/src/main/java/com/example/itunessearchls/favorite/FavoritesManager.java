package com.example.itunessearchls.favorite;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class FavoritesManager {

    private static final String PREF_NAME = "favorites_pref";
    private static final String KEY = "favorite_ids";

    public static void addFavorite(Context context, int trackId) {
        Set<String> favorites = getFavorites(context);
        favorites.add(String.valueOf(trackId));
        save(context, favorites);
    }

    public static void removeFavorite(Context context, int trackId) {
        Set<String> favorites = getFavorites(context);
        favorites.remove(String.valueOf(trackId));
        save(context, favorites);
    }

    public static boolean isFavorite(Context context, int trackId) {
        return getFavorites(context).contains(String.valueOf(trackId));
    }

    public static Set<String> getFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> stored = prefs.getStringSet(KEY, new HashSet<>());
        return stored == null ? new HashSet<>() : new HashSet<>(stored);
    }

    private static void save(Context context, Set<String> favorites) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putStringSet(KEY, favorites).commit();
    }
}
