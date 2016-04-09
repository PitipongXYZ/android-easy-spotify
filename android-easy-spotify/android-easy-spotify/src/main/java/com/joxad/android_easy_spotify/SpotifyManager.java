package com.joxad.android_easy_spotify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.PlayerNotificationCallback;

/***
 * {@link SpotifyManager is a helper to the connexion with token/code}
 */
public class SpotifyManager {

    private static final int REQUEST_CODE = 1337;

    public static PlayerNotificationCallback playerNotificationCallback;
    public static ConnectionStateCallback connectionStateCallback;

    private static Context context;
    protected static String API_KEY;
    private static OAuthListener oAuthListener;


    /****
     * @param context
     * @param apiKey
     */
    private static void init(@NonNull Context context, @StringRes final int apiKey) {
        SpotifyManager.context = context;
        SpotifyManager.API_KEY = context.getString(apiKey);
    }


    /***
     * Use this method to login with the webview
     *
     * @param activity            the activity that will handle the result of the login
     * @param oAuthListener
     * @param type
     * @param apiCallbackResource
     * @param scopes
     */
    public static void loginWithBrowser(final Activity activity, final Type type, @StringRes final int apiCallbackResource, final String[] scopes, final OAuthListener oAuthListener) {
        setoAuthListener(oAuthListener);
        AuthenticationClient.openLoginInBrowser(activity, buildRequest(type, context.getString(apiCallbackResource), scopes));
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
     * @param activity
     * @param type
     * @param apiCallback
     * @param scopes        uses the {@link Scope}
     * @param oAuthListener
     */
    public static void loginWithActivity(final Activity activity, final Type type, @StringRes final int apiCallback, final String[] scopes, final OAuthListener oAuthListener) {
        setoAuthListener(oAuthListener);
        AuthenticationClient.openLoginActivity(activity, REQUEST_CODE, buildRequest(type, context.getString(apiCallback), scopes));
    }


    /***
     * Manage the answer of Spotify when using the SpotifyActivity using
     * {@link #loginWithActivity(Activity, Type, int, String[], OAuthListener)}
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

    /***
     * Called when used the method {@link #loginWithBrowser(Activity, Type, int, String[], OAuthListener)}
     *
     * @param intent
     */
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

        private Context context;

        @StringRes
        private int apiKey = -1;

        /***
         * @param context
         * @return
         */
        public Builder setContext(final Context context) {
            this.context = context;
            return this;
        }

        /***
         * @param apiKey
         * @return
         */
        public Builder setApiKey(final int apiKey) {
            this.apiKey = apiKey;
            return this;
        }


        /***
         *
         */
        public void build() throws Exception {
            if (this.context == null)
                throw new Exception("Please set the Context");
            if (this.apiKey == -1)
                throw new Exception("Please set the APIKey");
            SpotifyManager.init(this.context, this.apiKey);
        }

    }


}
