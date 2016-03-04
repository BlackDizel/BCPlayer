package ru.byters.bcplayer.controllers;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.IOException;

import ru.byters.bcplayer.model.PlaylistItem;

public class ControllerPlayer implements OnCompletionListener {
    private MediaPlayer player;
    private int currentId;
    private Core controller;

    public ControllerPlayer(Core controller) {
        this.controller = controller;
    }

    //pos - position in playlist
    public void playClick(int id) {
        if (controller == null) return;

        if (player == null) {
            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnCompletionListener(this);
        }

        if (player.isPlaying() && id == currentId)
            player.pause();
        else if (id == currentId) { //player stopped/paused
            player.seekTo(player.getCurrentPosition());
            player.start();
        } else { //pos != currentPos && player is playing or paused/stopped
            play(id);
        }
    }

    private void play(int id) {
        player.reset();
        Uri uri = controller.getControllerSongs().getSongUri(id);
        if (uri == null) return;
        try {
            player.setDataSource(controller, uri);
            player.prepare();
            player.start();
            currentId = id;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playNext();
    }

    @Nullable
    public MediaPlayer getPlayer() {
        return player;
    }

    @Nullable
    public PlaylistItem getCurrentSong() {
        if (controller == null) return null;
        return controller.getControllerSongs().getItem(currentId);
    }

    public void playClick() {
        playClick(currentId);
    }

    public void playNext() {
        if (controller == null) return;
        int nextSongId = controller.getControllerSongs().getNextSongId(currentId);
        if (nextSongId == ControllerSongs.NO_VALUE) return;
        play(nextSongId);
    }

    public void playPrevious() {
        if (controller == null) return;
        int nextSongId = controller.getControllerSongs().getPreviousSongId(currentId);
        if (nextSongId == ControllerSongs.NO_VALUE) return;
        play(nextSongId);
    }

    public void seekTo(int position) {
        if (player == null) return;
        try {
            player.seekTo(position);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
