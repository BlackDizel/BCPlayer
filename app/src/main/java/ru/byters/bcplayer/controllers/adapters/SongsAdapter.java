package ru.byters.bcplayer.controllers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ru.byters.bcplayer.R;
import ru.byters.bcplayer.controllers.Core;
import ru.byters.bcplayer.model.PlaylistItem;

public class SongsAdapter extends BaseAdapter {

    Core controller;

    public SongsAdapter(Core controller) {
        this.controller = controller;
    }

    @Override
    public int getCount() {
        return controller.getControllerSongs().getItemsSize();
    }

    @Override
    public PlaylistItem getItem(int position) {
        return controller.getControllerSongs().getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_playlist_item, parent, false);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.setData(position);

        return convertView;
    }

    private class ViewHolder implements View.OnClickListener {
        private TextView tvTitle;
        private TextView tvArtist;
        private TextView tvGenre;
        private TextView tvDuration;
        private TextView tvSource;
        private int id;

        public ViewHolder(View view) {
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvArtist = (TextView) view.findViewById(R.id.tvArtist);
            tvGenre = (TextView) view.findViewById(R.id.tvGenre);
            tvDuration = (TextView) view.findViewById(R.id.tvDuration);
            tvSource = (TextView) view.findViewById(R.id.tvSource);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            controller.getControllerPlayer().playClick(id);
        }

        public void setData(int position) {
            PlaylistItem item = getItem(position);
            if (item == null) return;
            this.id = item.Id;
            tvTitle.setText(item.Title);
            tvArtist.setText(item.Artist);
            tvGenre.setText(item.Genre);
            tvDuration.setText(item.Duration);
            tvSource.setText(item.Source);
        }
    }
}
