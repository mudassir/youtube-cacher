package io.github.mudassir.youtubecacher.util;

import android.os.AsyncTask;
import android.os.Environment;

import com.github.axet.vget.VGet;

import java.io.File;
import java.net.URL;

/**
 * Basic AsyncTask that downloads a specified video.
 *
 * TODO implement downloading in parts
 */
public class VideoDownloadTask extends AsyncTask<String, Void, Void> {

	@Override
	protected Void doInBackground(String... args) {
		try {
			String url = args[0];
			String path = Environment.getExternalStorageDirectory().getPath() + "/Download/";
			VGet v = new VGet(new URL(url), new File(path));
			v.download();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
