package ru.byters.bcplayer.ui.fragments;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import ru.byters.bcplayer.R;
import ru.byters.bcplayer.controllers.ControllerPlayer;
import ru.byters.bcplayer.controllers.Core;
import ru.byters.bcplayer.model.PlaylistItem;


public class MainFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, OnClickListener {
    Button bPlay, bSkipRight, bSkipLeft;
    TextView tvDurationMax, tvDurationCurrent, tvSongName;
    SeekBar sbSongProgress;
    int isUIVisible = View.VISIBLE;

    private Handler mHandler = new Handler();
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (isAdded()) {
                ControllerPlayer playerController = ((Core) getContext().getApplicationContext()).getControllerPlayer();

                PlaylistItem song = playerController.getCurrentSong();
                if (song != null)
                    if (tvSongName != null) tvSongName.setText(song.Title);

                MediaPlayer player = playerController.getPlayer();
                if (player != null) {
                    long currentDuration = player.getCurrentPosition();

                    if (isUIVisible == View.VISIBLE) {
                        if (sbSongProgress != null)
                            sbSongProgress.setProgress((int) currentDuration);
                        if (tvDurationCurrent != null)
                            tvDurationCurrent.setText(Core.toTime(currentDuration));

                        long totalDuration = player.getDuration();
                        if (sbSongProgress != null) sbSongProgress.setMax((int) totalDuration);
                        if (tvDurationMax != null)
                            tvDurationMax.setText(Core.toTime(totalDuration));
                    }

                    if (player.isPlaying()) {
                        bPlay.setText(getResources().getString(R.string.pause));
                        bPlay.setPadding(0, 0, 0, 0);
                    } else {
                        bPlay.setText(getResources().getString(R.string.play));
                        bPlay.setPadding(10, 0, 0, 0);
                    }
                }
            }
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        bPlay = (Button) rootView.findViewById(R.id.btPlay);
        bSkipLeft = (Button) rootView.findViewById(R.id.btSkipLeft);
        bSkipRight = (Button) rootView.findViewById(R.id.btSkipRight);
        sbSongProgress = (SeekBar) rootView.findViewById(R.id.sbSong);
        tvDurationCurrent = (TextView) rootView.findViewById(R.id.tvSongCurrentPos);
        tvDurationMax = (TextView) rootView.findViewById(R.id.tvSongDuration);
        tvSongName = (TextView) rootView.findViewById(R.id.tvSongName);

        bPlay.setOnClickListener(this);
        bSkipLeft.setOnClickListener(this);
        bSkipRight.setOnClickListener(this);
        sbSongProgress.setOnSeekBarChangeListener(this);

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf");
        bPlay.setTypeface(font);
        bSkipLeft.setTypeface(font);
        bSkipRight.setTypeface(font);

        View page = rootView.findViewById(R.id.rootView);
        page.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Animation anim;
                if (isUIVisible == View.INVISIBLE) {
                    isUIVisible = View.VISIBLE;
                    anim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                } else {
                    isUIVisible = View.INVISIBLE;
                    anim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
                }

                bSkipLeft.startAnimation(anim);
                bSkipRight.startAnimation(anim);
                sbSongProgress.startAnimation(anim);
                tvSongName.startAnimation(anim);
                tvDurationMax.startAnimation(anim);
                tvDurationCurrent.startAnimation(anim);
                return true;
            }
        });

        initPlayerUI();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        ControllerPlayer controllerPlayer = ((Core) getContext().getApplicationContext()).getControllerPlayer();
        switch (v.getId()) {
            case R.id.btPlay:
                controllerPlayer.playClick();
                break;
            case R.id.btSkipLeft:
                controllerPlayer.playNext();
                break;
            case R.id.btSkipRight:
                controllerPlayer.playPrevious();
                break;
        }
        initPlayerUI();
    }

    public void initPlayerUI() {
        if (mUpdateTimeTask != null)
            mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, 1000);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int currentPosition = seekBar.getProgress();
        ((Core) getContext().getApplicationContext()).getControllerPlayer().seekTo(currentPosition);
        initPlayerUI();
    }

    @Override
    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
    }


}