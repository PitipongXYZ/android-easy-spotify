# AndroidEasySpotify
Use this library to add Spotfy to your project, only in a few minutes !

# Installation

Top Level Gradle :
```
allprojects {
    repositories {
        maven {
            url "http://dl.bintray.com/joxad/maven"
        }
    }
}
```

Project's build.gradle

```
dependencies {
    compile 'com.github.joxad:android-easy-spotify:1.0.0'
}
```

# Utilisation

In whatever class with Context (better in your first launched Activity class) 

## Create EasyGCM
 
```
 new SpotifyManager.Builder()
                    .setContext(this)
                    .setApiCallback(getString(R.string.api_spotify_callback))
                    .setApiKey(getString(R.string.api_spotify_id))
                    .setScope(new String[]{"user-read-private", "streaming"})
                    .setConnectionType(AuthenticationResponse.Type.CODE)
                    .build();
```


## Login With Activity
 
  
```
  SpotifyManager.loginWithActivity(new SpotifyManager.OAuthListener() {
            @Override
            public void onReceived(String code) {
                Log.d(TAG, code);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, error);
            }
        });
           
```

## Login With WebView
```

SpotifyManager.loginWithBrowser(new SpotifyManager.OAuthListener() {
            @Override
            public void onReceived(String code) {
                Log.d(TAG, code);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, error);
            }
        });


```

