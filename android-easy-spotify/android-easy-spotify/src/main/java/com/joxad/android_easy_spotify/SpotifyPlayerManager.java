package com.joxad.android_easy_spotify;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.Spotify;

import java.util.List;

/**
 * Created by josh on 05/04/16.
 */
public class SpotifyPlayerManager {
    private static Player player;
    private static Context context;

    /***
     * @param context
     * @param token                      : Spotify token needed to use the api
     * @param observer                   : used to know when player is ready
     * @param playerNotificationCallback
     * @param connectionStateCallback
     */
    public static void startPlayer(final Context context, String token, final Player.InitializationObserver observer, @Nullable PlayerNotificationCallback playerNotificationCallback, @Nullable ConnectionStateCallback connectionStateCallback) {
        SpotifyPlayerManager.context = context;
        Config playerConfig = new Config(context, token, SpotifyManager.API_KEY);
        setConnectionStateCallback(connectionStateCallback);
        setPlayerNotificationCallback(playerNotificationCallback);
        Spotify.getPlayer(playerConfig, context, new Player.InitializationObserver() {
            @Override
            public void onInitialized(Player pl) {
                player = pl;
                player.addConnectionStateCallback(SpotifyManager.connectionStateCallback);
                player.addPlayerNotificationCallback(SpotifyManager.playerNotificationCallback);
                observer.onInitialized(player);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e(SpotifyManager.class.getSimpleName(), "Could not initialize player: " + throwable.getMessage());
                observer.onError(throwable);

            }
        });
    }


    /***
     * Set the callback on the player (will be used to have information about the playing status)
     *
     * @param playerCallback
     */
    private static void setPlayerNotificationCallback(PlayerNotificationCallback playerCallback) {
        SpotifyManager.playerNotificationCallback = playerCallback;

    }

    /***
     * Set the callback on the connection player (will be used to have information about the playing status)
     *
     * @param connectionCallback
     */
    private static void setConnectionStateCallback(ConnectionStateCallback connectionCallback) {
        SpotifyManager.connectionStateCallback = connectionCallback;
    }

    /***
     * @param uri start the song selected
     */
    public static void play(final String uri) {
        player.play(uri);
    }

    /***
     * @param uri add the song to the queu
     */
    public static void queue(final String uri) {
        player.queue(uri);
    }

    /***
     * @param uris start the songs selected
     */
    public static void play(final List<String> uris) {
        player.play(uris);
    }


    /**
     * Pause the player
     */
    public static void pause() {
        player.pause();

    }

    /***
     * Destroy the SpotifyPlayer
     */
    public static void destroy() {
        Spotify.destroyPlayer(context);
    }

    /***
     *
     */
    public static void clear() {
        player.clearQueue();
    }

    /***
     * 1
     */
    public static void shutdown() {
        player.shutdown();
    }
}
