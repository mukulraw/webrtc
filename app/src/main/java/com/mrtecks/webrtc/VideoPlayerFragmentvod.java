package com.mrtecks.webrtc;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

public class VideoPlayerFragmentvod extends Fragment {


    private static final String KEY_PLAY_WHEN_READY = "play_when_ready";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    String videoId, url, thumb;
    int count = 0;
    CustomViewPager pager;
    String liveId = "mi6.mp4";
    private Uri uri;
    private PlayerView mPlayerView;
    private SimpleExoPlayer player;
    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;
    private ImageView ivHideControllerButton;
    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_video_player2, container, false);

        assert getArguments() != null;
        videoId = getArguments().getString("videoId");
        mPlayerView = view.findViewById(R.id.player_view);
        // mainPlayerView = view.findViewById(R.id.main_player);
        // thumb_player=view.findViewById(R.id.thumb_plplayer);
        if (savedInstanceState == null) {
            playWhenReady = true;
            currentWindow = 0;
            playbackPosition = 0;
        } else {
            playWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);
            currentWindow = savedInstanceState.getInt(KEY_WINDOW);
            playbackPosition = savedInstanceState.getLong(KEY_POSITION);
        }

        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();
        //  ivHideControllerButton = view.findViewById(R.id.exo_controller);

        // Log.d("statusss", "rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/vod/" + liveId);
        //url="http://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com/softcode/upload/video/maharani.mp4";


        return view;
    }




    private void initializePlayer() {

        mPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        mPlayerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);
/*        MediaSource mediaSource = new HlsMediaSource(Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"),
                mediaDataSourceFactory, mainHandler, null);*/

        player.setRepeatMode(Player.REPEAT_MODE_ONE);

        bean b = (bean) getContext().getApplicationContext();

        MediaSource mediaSource = new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                .createMediaSource(Uri.parse(videoId));

        boolean haveStartPosition = currentWindow != C.INDEX_UNSET;
        if (haveStartPosition) {
            player.seekTo(currentWindow, playbackPosition);
        }
        player.prepare(mediaSource, !haveStartPosition, false);
/*
        ivHideControllerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerView.hideController();
            }
        });*/

        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_BUFFERING) {
                    //loadingProgress.setVisibility(View.VISIBLE);
                } else {
                    //loadingProgress.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        initializePlayer();

    }

    @Override
    public void onResume() {
        super.onResume();
        if ((player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        releasePlayer();

    }

    @Override
    public void onStop() {
        super.onStop();

        releasePlayer();

    }

    private void releasePlayer() {
        if (player != null) {
            updateStartPosition();
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    /* @Override
     public void onSaveInstanceState(Bundle outState) {
         updateStartPosition();

         outState.putBoolean(KEY_PLAY_WHEN_READY, playWhenReady);
         outState.putInt(KEY_WINDOW, currentWindow);
         outState.putLong(KEY_POSITION, playbackPosition);
         super.onSaveInstanceState(outState);
     }
 */
    private void updateStartPosition() {
        playbackPosition = player.getCurrentPosition();
     /*   if(player!=null)
        {
            currentWindow = player.getCurrentWindowIndex();
        }
     */
        playWhenReady = player.getPlayWhenReady();
    }




}
