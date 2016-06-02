package io.github.mudassir.youtubecacher;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.github.mudassir.youtubecacher.ui.PasteDialog;
import io.github.mudassir.youtubecacher.ui.CachedFragment;
import io.github.mudassir.youtubecacher.ui.FragmentPagerAdapter;
import io.github.mudassir.youtubecacher.ui.HomeFragment;

public class MainActivity extends AppCompatActivity implements PasteDialog.Listener {

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
		fragmentPagerAdapter.addFragment(new HomeFragment());
		fragmentPagerAdapter.addFragment(new CachedFragment());

		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(fragmentPagerAdapter);

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

	@Override
	public void onPasteReceived(String url) {
		// TODO verify the string being sent in is valid
		android.widget.Toast.makeText(this, url, android.widget.Toast.LENGTH_SHORT).show();
	}
}
