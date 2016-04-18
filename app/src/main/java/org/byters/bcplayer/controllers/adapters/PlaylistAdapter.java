package org.byters.bcplayer.controllers.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.byters.bcplayer.R;
import org.byters.bcplayer.controllers.Core;
import org.byters.bcplayer.model.PlaylistItem;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private Core core;

    public PlaylistAdapter(Core core) {
        this.core = core;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_playlist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return core.getControllerSongs().getItemsSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle;
        private TextView tvArtist;
        private TextView tvDuration;
        private String id;


        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvArtist = (TextView) itemView.findViewById(R.id.tvArtist);
            tvDuration = (TextView) itemView.findViewById(R.id.tvDuration);

            itemView.setOnClickListener(this);
        }

        public void setData(int position) {
            PlaylistItem item = core.getControllerSongs().getItemByPos(position);
            if (item == null) {
                this.id = null;
                return;
            }

            this.id = item.Id;
            tvTitle.setText(item.Title);
            tvArtist.setText(item.Artist);
            tvDuration.setText(item.Duration);
        }


        @Override
        public void onClick(View v) {
            core.getControllerPlayer().playClick(id);
        }

    }
}
