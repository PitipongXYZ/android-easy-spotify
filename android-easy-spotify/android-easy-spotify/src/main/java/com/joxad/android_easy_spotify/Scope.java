package com.joxad.android_easy_spotify;

/**
 * Created by josh on 09/04/16.
 * List some of the scopes uses by the SpotifyApi
 */
public class Scope {

    /**
     * Read access to user's private playlists.	"Access your private playlists"
     */
    public static String PLAYLIST_READ_PRIVATE = "playlist-read-private";
    /***
     * Include collaborative playlists when requesting a user's playlists.	"Access your collaborative playlists"
     */
    public static String PLAYLIST_READ_COLLABORATIVE = "playlist-read-collaborative";
    /***
     * Write access to a user's public playlists.	"Manage your public playlists"
     */
    public static String PLAYLIST_MODIFY_PUBLIC = "playlist-modify-public";
    /**
     * Write access to a user's private playlists.	"Manage your private playlists"
     */
    public static String PLAYLIST_MODIFY_PRIVATE = "playlist-modify-private";
    /**
     * Control playback of a Spotify track. This scope is currently only available to Spotify native SDKs (for example, the iOS SDK and the Android SDK).
     * The user must have a Spotify Premium account.	"Play music and control playback on your other devices"
     */
    public static String STREAMING = "streaming";
    /***
     * Write/delete access to the list of artists and other users that the user follows.	"Manage who you are following"
     */
    public static String USER_FOLLOW_MODIFY = "user-follow-modify";
    /**
     * Read access to the list of artists and other users that the user follows.	"Access your followers and who you are following"
     */
    public static String USER_FOLLOW_READ = "user-follow-read";
    /**
     * Read access to a user's "Your Music" library.	"Access your saved tracks and albums"
     */
    public static String USER_LIBRARY_READ = "user-library-read";
    /**
     * Write/delete access to a user's "Your Music" library.	"Manage your saved tracks and albums"
     */
    public static String USER_LIBRARY_MODIFIY = "user-library-modify";
    /***
     * Read access to user’s subscription details (type of user account).	"Access your subscription details"
     */
    public static String USER_READ_PRIVATE = "user-read-private";
    /***
     * Read access to the user's birthdate.	"Receive your birthdate"
     */
    public static String USER_READ_BIRTHDATE = "user-read-birthdate";
    /***
     * Read access to user’s email address.	"Get your real email address"
     */
    public static String USER_READ_EMAIL = "user-read-email";
    /***
     * Read access to a user's top artists and tracks	"Read your top artists and tracks"
     */
    public static String USER_TOP_READ = "user-top-read";

}
