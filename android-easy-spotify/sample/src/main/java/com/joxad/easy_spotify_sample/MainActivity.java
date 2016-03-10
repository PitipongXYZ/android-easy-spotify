package com.joxad.easy_spotify_sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.joxad.android_easy_spotify.SpotifyManager;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        try {
            new SpotifyManager.Builder()
                    .setContext(this)
                    .setApiCallback(getString(R.string.api_spotify_callback))
                    .setApiKey(getString(R.string.api_spotify_id))
                    .setScope(new String[]{"user-read-private", "streaming"})
                    .setConnectionType(AuthenticationResponse.Type.CODE)
                    .build();
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }

        SpotifyManager.loginWithActivity(new SpotifyManager.OAuthListener() {
            @Override
            public void onReceived(String code) {
                Log.d(TAG, code);
               tv.setText(code);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, error);
                tv.setText(error);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SpotifyManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SpotifyManager.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpotifyManager.destroy();
    }
}
