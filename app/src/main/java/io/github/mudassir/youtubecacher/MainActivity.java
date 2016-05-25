package io.github.mudassir.youtubecacher;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.axet.vget.VGet;

import java.io.File;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					// ex: http://www.youtube.com/watch?v=Nj6PFaDmp6c
					String url = "https://www.youtube.com/watch?v=-YGDyPAwQz0";
					// ex: "/Users/axet/Downloads"
					String path = Environment.getExternalStorageDirectory().getPath() + "/Download/";
					VGet v = new VGet(new URL(url), new File(path));
					v.download();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

				return null;
			}
		}.execute();
	}
}
