package io.gitlab.mudassir.youtubecacher;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

public class PlayerActivity extends AppCompatActivity {

	public static final String VIDEO_FILE = "io.gitlab.mudassir.youtubecacher.video-file";

	private File mAudio;
	private File mVideo;
	private SimpleExoPlayer mPlayer;
	private SimpleExoPlayerView mPlayerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_player);
		mPlayerView = (SimpleExoPlayerView) findViewById(R.id.player);
		// TODO find file paths more elegantly
		mVideo = (File) getIntent().getExtras().get(VIDEO_FILE);
		mAudio = new File(mVideo.getAbsolutePath().replaceAll("mp4","webm"));

		// Creating the player
		BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
		TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
		TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
		LoadControl loadControl = new DefaultLoadControl();

		mPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
		mPlayerView.setPlayer(mPlayer);

		// Preparing the player
		// Video is in mp4 file, audio is in webm file
		Uri videoUri = Uri.parse(mVideo.getAbsolutePath());
		Uri audioUri = Uri.parse(mAudio.getAbsolutePath());
		DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "io.gitlab.mudassir.youtubecacher"));
		ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
		MediaSource videoSource = new ExtractorMediaSource(videoUri, dataSourceFactory, extractorsFactory, null, null);
		MediaSource audioSource = new ExtractorMediaSource(audioUri, dataSourceFactory, extractorsFactory, null, null);
		MergingMediaSource mergedVideo = new MergingMediaSource(videoSource, audioSource);

		mPlayer.prepare(mergedVideo);
		mPlayer.setPlayWhenReady(true);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (Util.SDK_INT <= 23) {
			releasePlayer();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (Util.SDK_INT > 23) {
			releasePlayer();
		}
	}

	private void releasePlayer() {
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
}
