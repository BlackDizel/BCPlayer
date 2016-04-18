package org.byters.bcplayer.controllers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import org.byters.bcplayer.model.PlaylistItem;

public class ControllerSongs {

    private static final int NEXT = 1;
    private static final int PREVIOUS = -1;
    private ArrayList<PlaylistItem> items;

    public ControllerSongs(Core controller) {
        items = listMusic(controller, MediaStore.Audio.Media.INTERNAL_CONTENT_URI);

        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            ArrayList<PlaylistItem> songList = listMusic(controller, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            if (songList == null) return;
            if (items == null) items = new ArrayList<>();
            else items.addAll(songList);
        }
    }

    //create data from library
    @Nullable
    private ArrayList<PlaylistItem> listMusic(Context context, Uri storageUri) {
        //Some audio may be explicitly marked as not being music
        String selection = String.format("%s != 0", MediaStore.Audio.Media.IS_MUSIC);

        String[] projection =
                {
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DURATION
                };

        ContentResolver cr = context.getContentResolver();

        Cursor cursor = cr.query(
                storageUri,
                projection,
                selection,
                null,
                null);

        if (cursor == null) return null;

        ArrayList<PlaylistItem> songs = new ArrayList<>();
        while (cursor.moveToNext()) {
            PlaylistItem i = new PlaylistItem();
            i.Id = cursor.getString(0);
            i.Artist = cursor.getString(1);
            i.Title = cursor.getString(2);
            i.Uri = Uri.parse(cursor.getString(3));
            i.Duration = Core.toTime(Integer.parseInt(cursor.getString(5)));
            i.Genre = "";
            songs.add(i);
        }
        cursor.close();
        return songs;
    }

    @Nullable
    public Uri getSongUri(String id) {
        PlaylistItem item = getSong(id);
        if (item == null) return null;
        return item.Uri;
    }

    //todo cache this
    @Nullable
    public PlaylistItem getSong(String id) {
        if (items == null) return null;
        for (PlaylistItem item : items)
            if (item.Id.equals(id))
                return item;
        return null;
    }

    @Nullable
    public String getNextSongId(String id) {
        return getOtherSongId(id, NEXT);
    }

    @Nullable
    public String getPreviousSongId(String id) {
        return getOtherSongId(id, PREVIOUS);
    }

    @Nullable
    private String getOtherSongId(String id, int nextItem) {
        if (getItemsSize() == 0) return null;

        PlaylistItem item = getSong(id);
        if (item == null) return null;
        int pos = items.indexOf(item);

        int newPos = (nextItem == NEXT)
                ? (++pos) % items.size()
                : (items.size() - 1 + pos) % items.size();

        PlaylistItem result = getItemByPos(newPos);
        if (result == null) return null;
        return result.Id;
    }

    public int getItemsSize() {
        return items == null ? 0 : items.size();
    }

    @Nullable
    public PlaylistItem getItemByPos(int position) {
        if (position < 0 || position >= getItemsSize())
            return null;
        return items.get(position);
    }

}
