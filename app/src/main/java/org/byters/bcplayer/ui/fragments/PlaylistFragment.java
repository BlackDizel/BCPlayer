package org.byters.bcplayer.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.byters.bcplayer.R;
import org.byters.bcplayer.controllers.Core;
import org.byters.bcplayer.controllers.adapters.PlaylistAdapter;

public class PlaylistFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playlist, container, false);

        RecyclerView rvPlaylist = (RecyclerView) rootView.findViewById(R.id.rvPlaylist);
        rvPlaylist.setHasFixedSize(true);
        rvPlaylist.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPlaylist.setAdapter(new PlaylistAdapter(((Core) getContext().getApplicationContext())));
        return rootView;
    }
}