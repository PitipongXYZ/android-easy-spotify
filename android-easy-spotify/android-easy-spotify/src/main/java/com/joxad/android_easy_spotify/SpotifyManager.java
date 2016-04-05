package com.joxad.android_easy_spotify;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.Spotify;

/***
 * {@link SpotifyManager is a helper to the connexion with token/code}
 * <p>
 * In your Activity :
 * <p>
 * <p>
 * new SpotifyManager.Builder()
 * .setContext(context)
 * .setApiCallback(context.getString(R.string.api_spotify_callback))
 * .setApiKey(context.getString(R.string.api_spotify_id))
 * .setScope(new String[]{"user-read-private", "streaming"})
 * .setConnectionType(Type.CODE)
 * .build();
 * <p>
 * <p>
 * SpotifyManager.loginWithBrowser(new SpotifyManager.OAuthListener() {
 *
 * @Override public void onReceived(String code) {
 * accessCode = code;
 * getTokenFromCode();
 * }
 * @Override public void onError(String error) {
 * Log.d(TAG, error);
 * }
 * });
 * @<code> public void onActivityResult(int requestCode, int resultCode, Intent data) {
 * SpotifyManager.onActivityResult(requestCode, resultCode, data);
 * }
 * public void onNewIntent(Intent intent) {
 * SpotifyManager.onNewIntent(intent);
 * }
 * <p>
 * </code>
 */
public class SpotifyManager {

    private static final int REQUEST_CODE = 1337;

    public static PlayerNotificationCallback playerNotificationCallback;
    public static ConnectionStateCallback connectionStateCallback;

    private static Activity context;
    protected static String API_KEY;
    private static OAuthListener oAuthListener;


    /****
     * @param c
     * @param apiKey
     */
    private static void init(Activity c, final String apiKey) {
        SpotifyManager.context = c;
        SpotifyManager.API_KEY = apiKey;
    }


    /***
     * Use this method to login with the webview
     *
     * @param oAuthListener
     * @param type
     * @param apiCallback
     * @param scopes
     */
    public static void loginWithBrowser(final Type type, final String apiCallback, final String[] scopes, final OAuthListener oAuthListener) {
        setoAuthListener(oAuthListener);
        AuthenticationClient.openLoginInBrowser(context, buildRequest(type, apiCallback, scopes));
    }


    /***
     * Build the REquest according to the configuration given
     *
     * @return
     */
    private static AuthenticationRequest buildRequest(final Type type, final String apiCallback, final String[] scopes) {
        AuthenticationResponse.Type spotifyType = (type == Type.CODE) ? AuthenticationResponse.Type.CODE : AuthenticationResponse.Type.TOKEN;

        AuthenticationRequest.Builder builder = new AuthenticationRequest
                .Builder(SpotifyManager.API_KEY, spotifyType, apiCallback).setScopes(scopes);
        return builder.build();
    }


    /***
     * @param type
     * @param apiCallback
     * @param scopes
     * @param oAuthListener
     */
    public static void loginWithActivity(final Type type, final String apiCallback, final String[] scopes, final OAuthListener oAuthListener) {
        setoAuthListener(oAuthListener);
        AuthenticationClient.openLoginActivity(context, REQUEST_CODE, buildRequest(type, apiCallback, scopes));
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

    /***
     *
     */
    public static class Builder {

        private Activity context;
        private String apiKey;

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
         *
         */
        public void build() throws Exception {
            if (this.context == null)
                throw new Exception("Please set the Context");
            if (this.apiKey == null)
                throw new Exception("Please set the APIKey");
            SpotifyManager.init(this.context, this.apiKey);
        }

    }


}
