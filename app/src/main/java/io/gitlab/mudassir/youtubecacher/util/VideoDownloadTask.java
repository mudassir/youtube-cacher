package io.gitlab.mudassir.youtubecacher.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;

import com.github.axet.vget.VGet;

import java.net.URL;

import io.gitlab.mudassir.youtubecacher.MainActivity;
import io.gitlab.mudassir.youtubecacher.R;

/**
 * Basic AsyncTask that downloads a specified video.
 *
 * TODO implement downloading in parts
 */
public class VideoDownloadTask extends AsyncTask<String, Void, Void> {

	public static final int SUCCESS = 0;
	public static final int FAILED = 1;

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
			// End the task
			cancel(true);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void aVoid) {
		super.onPostExecute(aVoid);
		issueNotification(SUCCESS);
	}

	@Override
	protected void onCancelled(Void aVoid) {
		issueNotification(FAILED);
	}

	private void issueNotification(int result) {
		NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(mContext);
		switch (result) {
			case SUCCESS:
				builder
					.setContentTitle("Download finished")
					.setContentText("Finished downloading video")
					.setSmallIcon(R.drawable.ic_checkmark);
				break;

			case FAILED:
			default:
				builder
					.setContentTitle("Download failed")
					.setContentText("Could not download video")
					.setSmallIcon(R.drawable.ic_x);
				break;
		}
		notificationManager.cancel(MainActivity.NOTIFICATION_ID);
		notificationManager.notify(MainActivity.NOTIFICATION_ID,builder.build());
	}
}
