package ru.byters.bcplayer.controllers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import ru.byters.bcplayer.model.PlaylistItem;

public class ControllerSongs {

    public static final int NO_VALUE = -1;
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
            i.Id = 0;
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
    public Uri getSongUri(int id) {
        PlaylistItem item = getSong(id);
        if (item == null) return null;
        return item.Uri;
    }

    //todo cache this
    @Nullable
    public PlaylistItem getSong(int id) {
        if (items == null) return null;
        for (PlaylistItem item : items)
            if (item.Id == id)
                return item;
        return null;
    }

    public int getNextSongId(int id) {
        return getOtherSongId(id, 1);
    }

    public int getPreviousSongId(int id) {
        return getOtherSongId(id, -1);
    }

    private int getOtherSongId(int id, int i) {
        if (items == null || items.size() == 0) return NO_VALUE;

        int pos = NO_VALUE;
        for (PlaylistItem item : items)
            if (item.Id == id) {
                pos = items.indexOf(item);
                break;
            }

        if (pos == NO_VALUE) return items.get(0).Id;

        int newPos = (i == 1)
                ? (++pos) % items.size()
                : (items.size() - 1 + pos) % items.size();

        return items.get(newPos).Id;
    }

    public int getItemsSize() {
        return items == null ? 0 : items.size();
    }

    @Nullable
    public PlaylistItem getItem(int position) {
        if (position < 0 || position >= getItemsSize())
            return null;
        return items.get(position);
    }

}
