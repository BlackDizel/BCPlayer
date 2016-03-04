package ru.byters.bcplayer.controllers.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ru.byters.bcplayer.ui.fragments.MainFragment;
import ru.byters.bcplayer.ui.fragments.MenuFragment;
import ru.byters.bcplayer.ui.fragments.PlaylistFragment;

public class PagesAdapter extends FragmentStatePagerAdapter {
    public PagesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MenuFragment();
            case 1:
                return new MainFragment();
            case 2:
                return new PlaylistFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
