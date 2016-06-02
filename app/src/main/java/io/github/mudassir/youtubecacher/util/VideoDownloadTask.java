package io.github.mudassir.youtubecacher.util;

import android.content.Context;
import android.os.AsyncTask;

import com.github.axet.vget.VGet;

import java.net.URL;

/**
 * Basic AsyncTask that downloads a specified video.
 *
 * TODO implement downloading in parts
 */
public class VideoDownloadTask extends AsyncTask<String, Void, Void> {

	private Context mContext;

	public VideoDownloadTask(Context context) {
		mContext = context;
	}

	@Override
	protected Void doInBackground(String... args) {
		try {
			String url = args[0];
			new VGet(new URL(url), mContext.getFilesDir()).download();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
