package com.example.myapplication;

import android.app.Application;

import app.infoset.android.InfosetChatConfiguration;

import static com.example.myapplication.MainActivity.INFOSET_ANDROID_KEY;
import static com.example.myapplication.MainActivity.INFOSET_API_KEY;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static InfosetChatConfiguration getInfosetChatConfiguration() {
        return new InfosetChatConfiguration(INFOSET_API_KEY, INFOSET_ANDROID_KEY, null, null, null, null);
    }
}
