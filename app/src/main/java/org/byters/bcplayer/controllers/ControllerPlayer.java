package org.byters.bcplayer.controllers;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.byters.bcplayer.model.PlaylistItem;

import java.io.IOException;

public class ControllerPlayer implements OnCompletionListener {
    private MediaPlayer player;
    private String currentId;
    private Core controller;

    public ControllerPlayer(Core controller) {
        this.controller = controller;
    }

    //pos - position in playlist
    public void playClick(String id) {
        if (controller == null) return;
        if (TextUtils.isEmpty(id)) return;

        if (player == null) {
            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnCompletionListener(this);
        }

        if (player.isPlaying() && id.equals(currentId) && !TextUtils.isEmpty(currentId))
            player.pause();
        else if (id.equals(currentId) && !TextUtils.isEmpty(currentId)) { //player stopped/paused
            player.seekTo(player.getCurrentPosition());
            player.start();
        } else  //pos != currentPos && player is playing or paused/stopped
            play(id);
    }

    private void play(String id) {
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
        return controller.getControllerSongs().getSong(currentId);
    }

    public void playClick() {
        if (!TextUtils.isEmpty(currentId)) {
            playClick(currentId);
            return;
        }
        PlaylistItem item = controller.getControllerSongs().getItemByPos(0);
        if (item == null)
            return;
        playClick(item.Id);
    }

    public void playNext() {
        if (controller == null) return;
        String nextSongId = controller.getControllerSongs().getNextSongId(currentId);
        if (TextUtils.isEmpty(nextSongId)) return;
        play(nextSongId);
    }

    public void playPrevious() {
        if (controller == null) return;
        String nextSongId = controller.getControllerSongs().getPreviousSongId(currentId);
        if (TextUtils.isEmpty(nextSongId)) return;
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
