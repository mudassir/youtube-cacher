package io.github.mudassir.youtubecacher;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.github.mudassir.youtubecacher.model.DownloadListener;
import io.github.mudassir.youtubecacher.ui.PasteDialog;
import io.github.mudassir.youtubecacher.ui.CachedFragment;
import io.github.mudassir.youtubecacher.ui.FragmentPagerAdapter;
import io.github.mudassir.youtubecacher.ui.HomeFragment;
import io.github.mudassir.youtubecacher.util.VideoDownloadTask;

public class MainActivity extends AppCompatActivity implements DownloadListener, PasteDialog.Listener {

	public static final int NOTIFICATION_ID = 786;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*
		 * Setting toolbar manually so it plays nicely with tabLayout. To
		 * set it manually, the base theme must not set its own toolbar, so
		 * the theme was set to Theme.AppCompat.Light.NoActionBar
		 */
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// Set up view pager & tabs
		FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager());
		final CachedFragment cache = new CachedFragment(); // Need reference
		fragmentPagerAdapter.addFragment(new HomeFragment());
		fragmentPagerAdapter.addFragment(cache);

		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(fragmentPagerAdapter);
		// Add a listener to see when the cached fragment is selected, so that the list of files could be refreshed at that time
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

			@Override
			public void onPageSelected(int position) {
				switch (position) {
					case 1:
						cache.refresh();
						break;
					default:
						break;
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) { }
		});

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(viewPager);
		tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
		tabLayout.getTabAt(1).setIcon(R.drawable.ic_cached);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.paste:
				new PasteDialog().show(getSupportFragmentManager(), "paste");
				return true;

			default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Initiate download from the pasted text being received
	 * from the dialog in the options menu on the toolbar.
	 * @param url Link to the video
	 */
	@Override
	public void onPasteReceived(String url) {
		download(url);
	}

	@Override
	public void download(String url) {
		// TODO verify url somehow
		Notification.Builder builder = new Notification.Builder(this);
		builder.setContentTitle("Downloading video")
				.setContentText("Download in progress")
				.setSmallIcon(R.drawable.ic_cached);
		builder.setProgress(-1, -1, true); // Indeterminate progress
		((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, builder.build());
		new VideoDownloadTask(this).execute(url);
	}
}
