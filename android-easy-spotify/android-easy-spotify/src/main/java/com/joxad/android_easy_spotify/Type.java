package com.joxad.android_easy_spotify;

/**
 * Created by josh on 05/04/16.
 */
public enum Type {
    CODE("code"),
    TOKEN("token");

    private final String mType;

    private Type(String type) {
        this.mType = type;
    }

    public String toString() {
        return this.mType;
    }

}
