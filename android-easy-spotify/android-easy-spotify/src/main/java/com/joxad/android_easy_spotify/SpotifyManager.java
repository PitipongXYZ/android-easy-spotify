package com.joxad.android_easy_spotify;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.Spotify;

/**
 * Created by josh on 10/03/16.
 */
public class SpotifyManager {

    private static final int REQUEST_CODE = 1337;

    public static PlayerNotificationCallback playerNotificationCallback;
    public static ConnectionStateCallback connectionStateCallback;

    private static Player player;
    private static Activity context;
    private static String API_KEY;
    private static String API_CALLBACK;
    private static AuthenticationResponse.Type RESPONSE_TYPE;


    private static OAuthListener oAuthListener;
    private static String[] scopes;

    /****
     * @param c
     * @param apiKey
     * @param apiCallback
     * @param type
     * @param scope
     */
    private static void init(Activity c, final String apiKey, final String apiCallback, AuthenticationResponse.Type type, final String[] scope) {
        SpotifyManager.context = c;
        SpotifyManager.API_KEY = apiKey;
        SpotifyManager.API_CALLBACK = apiCallback;
        SpotifyManager.RESPONSE_TYPE = type;
        SpotifyManager.scopes = scope;

    }

    /***
     * Build the REquest according to the configuration given
     *
     * @return
     */
    private static AuthenticationRequest buildRequest() {
        AuthenticationRequest.Builder builder = new AuthenticationRequest
                .Builder(SpotifyManager.API_KEY,
                SpotifyManager.RESPONSE_TYPE,
                SpotifyManager.API_CALLBACK)
                .setScopes(scopes);
        return builder.build();
    }

    /***
     *
     */
    public static void loginWithBrowser(OAuthListener oAuthListener) {
        setoAuthListener(oAuthListener);
        AuthenticationClient.openLoginInBrowser(context, buildRequest());
    }

    /***
     *
     */
    public static void loginWithActivity(OAuthListener oAuthListener) {
        setoAuthListener(oAuthListener);
        AuthenticationClient.openLoginActivity(context, REQUEST_CODE, buildRequest());

    }

    /***
     * Destroy the SpotifyPlayer
     */
    public static void destroy() {
        Spotify.destroyPlayer(context);
    }

    /***
     * Manage the answer of Spotify when using the SpotifyActivity using
     * AuthenticationClient.openLoginActivity(context, REQUEST_CODE, request);
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            handleResponse(response);
        }
    }

    public static void onNewIntent(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            AuthenticationResponse response = AuthenticationResponse.fromUri(uri);
            handleResponse(response);
        }
    }

    /***
     * Manage the response of Spotify
     *
     * @param response
     */
    private static void handleResponse(AuthenticationResponse response) {
        switch (response.getType()) {
            // Response was successful and contains auth token
            case TOKEN:
                SpotifyManager.oAuthListener.onReceived(response.getAccessToken());
                break;
            case CODE:
                SpotifyManager.oAuthListener.onReceived(response.getCode());
                break;
            case ERROR:
                SpotifyManager.oAuthListener.onError(response.getError());
                break;
        }
    }

    /***
     * @param token              : SPoitfy token needed to use the api
     * @param connectionCallback
     * @param playerCallback
     */
    public static void startPlayer(String token, ConnectionStateCallback connectionCallback, PlayerNotificationCallback playerCallback) {

        Config playerConfig = new Config(context, token, API_KEY);

        SpotifyManager.playerNotificationCallback = playerCallback;
        SpotifyManager.connectionStateCallback = connectionCallback;
        Spotify.getPlayer(playerConfig, context, new Player.InitializationObserver() {
            @Override
            public void onInitialized(Player pl) {
                player = pl;
                player.addConnectionStateCallback(SpotifyManager.connectionStateCallback);
                player.addPlayerNotificationCallback(SpotifyManager.playerNotificationCallback);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
            }
        });
    }

    /***
     * @param uri start the song selected
     */
    public static void play(final String uri) {
        player.play(uri);
    }


    /***
     * @param uri start the song selected
     */
    public static void play(final List<String> uris) {
        player.play(uris);
    }

    /***
     * Interface needed to get the callback of the token
     */
    public interface OAuthListener {
        /***
         * @param code can be the TOKEN OR the Code according to the **type** you choose in the init
         */
        public void onReceived(final String code);

        public void onError(final String error);
    }

    /***
     * @param oAuthListener
     */
    public static void setoAuthListener(OAuthListener oAuthListener) {
        SpotifyManager.oAuthListener = oAuthListener;
    }

    public static class Builder {

        private Activity context;
        private String apiKey;
        private String apiCallback;
        private String[] scope;
        private AuthenticationResponse.Type connectionType;

        /***
         * @param context
         * @return
         */
        public Builder setContext(final Activity context) {
            this.context = context;
            return this;
        }

        /***
         * @param apiKey
         * @return
         */
        public Builder setApiKey(final String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /***
         * @param connectionType
         * @return
         */
        public Builder setConnectionType(final AuthenticationResponse.Type connectionType) {
            this.connectionType = connectionType;
            return this;
        }

        /***
         * @param apiCallback
         * @return
         */
        public Builder setApiCallback(final String apiCallback) {
            this.apiCallback = apiCallback;
            return this;
        }

        /**
         * @param scope
         * @return
         */
        public Builder setScope(String[] scope) {
            this.scope = scope;
            return this;
        }

        /***
         *
         */
        public void build() throws Exception {
            if (this.context == null)
                throw new Exception("Please set the Context");
            if (this.apiKey == null)
                throw new Exception("Please set the APIKey");
            if (this.connectionType == null)
                throw new Exception("Please set the ConnectionType");
            if (this.apiCallback == null)
                throw new Exception("Please set the Callback");
            if (this.scope == null)
                scope = new String[]{"user-read-private", "streaming"};
            SpotifyManager.init(this.context, this.apiKey, this.apiCallback, this.connectionType, this.scope);
        }

    }


}
