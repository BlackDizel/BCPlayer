package ru.byters.bcplayer.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ru.byters.bcplayer.R;
import ru.byters.bcplayer.controllers.Core;
import ru.byters.bcplayer.controllers.adapters.SongsAdapter;

public class PlaylistFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playlist, container, false);

        ListView lvPlaylist = (ListView) rootView.findViewById(R.id.lvPlaylist);
        lvPlaylist.setAdapter(new SongsAdapter(((Core) getContext().getApplicationContext())));
        return rootView;
    }
}