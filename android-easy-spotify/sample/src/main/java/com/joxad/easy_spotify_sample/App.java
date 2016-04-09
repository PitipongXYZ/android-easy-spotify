package com.joxad.easy_spotify_sample;

import android.app.Application;

import com.joxad.android_easy_spotify.SpotifyManager;

/**
 * Created by josh on 09/04/16.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            new SpotifyManager.Builder().setContext(this).setApiKey(R.string.api_spotify_key).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
