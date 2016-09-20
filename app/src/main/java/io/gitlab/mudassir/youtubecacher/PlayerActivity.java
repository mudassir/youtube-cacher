package io.gitlab.mudassir.youtubecacher;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.mp4.Mp4Extractor;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.io.IOException;

public class PlayerActivity extends AppCompatActivity {

	public static final String VIDEO_FILE = "io.gitlab.mudassir.youtubecacher.video-file";

	private File mVideo;
	private SimpleExoPlayer mPlayer;
	private SimpleExoPlayerView mPlayerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mVideo = (File) getIntent().getExtras().get(VIDEO_FILE);

		setContentView(R.layout.activity_player);

		mPlayerView = (SimpleExoPlayerView) findViewById(R.id.player);

		// Creating the player
		Handler mainHandler = new Handler();
		BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
		TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
		TrackSelector trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);
		LoadControl loadControl = new DefaultLoadControl();

		mPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
		mPlayerView.setPlayer(mPlayer);

		// Preparing the player
		final Uri uri = Uri.parse(mVideo.getAbsolutePath());
		MediaSource mediaSource = new ExtractorMediaSource(
				uri,
				new FileDataSourceFactory(),
				Mp4Extractor.FACTORY,
				mainHandler,
				new ExtractorMediaSource.EventListener() {
					@Override
					public void onLoadError(IOException error) {
						System.out.println("Media error");
					}
				}
		);
		mPlayer.prepare(mediaSource);
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
		mPlayer.release();
		mPlayer = null;
	}
}
