package ru.byters.bcplayer.controllers;

import android.app.Application;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

public class Core extends Application {
    private ControllerPlayer player;
    private ControllerSongs controllerSongs;

    @NonNull
    public static String toTime(long millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)
        );
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new ControllerPlayer(this);
        controllerSongs = new ControllerSongs(this);
    }

    public ControllerPlayer getControllerPlayer() {
        return player;
    }

    public ControllerSongs getControllerSongs() {
        return controllerSongs;
    }
}
