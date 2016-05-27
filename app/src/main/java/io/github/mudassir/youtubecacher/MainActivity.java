package io.github.mudassir.youtubecacher;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.axet.vget.VGet;
import com.github.axet.vget.info.VGetParser;
import com.github.axet.vget.info.VideoFileInfo;
import com.github.axet.vget.info.VideoInfo;
import com.github.axet.wget.SpeedInfo;
import com.github.axet.wget.info.DownloadInfo;
import com.github.axet.wget.info.DownloadInfo.Part;
import com.github.axet.wget.info.DownloadInfo.Part.States;
import com.github.axet.wget.info.ex.DownloadInterruptedError;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

	private long last;
	private Map<VideoFileInfo, SpeedInfo> map = new HashMap<>();
	private VideoInfo videoInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new AsyncTask<Void, Integer, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				// ex: http://www.youtube.com/watch?v=Nj6PFaDmp6c
				String address = "https://www.youtube.com/watch?v=-YGDyPAwQz0";
				// ex: "/Users/axet/Downloads"
				String path = Environment.getExternalStorageDirectory().getPath() + "/Download/";

				try {
					final AtomicBoolean stop = new AtomicBoolean(false);
					Runnable notify = new Runnable() {
						@Override
						public void run() {
							VideoInfo videoinfo = MainActivity.this.videoInfo;
							List<VideoFileInfo> dInfoList = videoinfo.getInfo();

							// notify app or save download state
							// you can extract information from DownloadInfo info;
							switch (videoinfo.getState()) {
								case EXTRACTING:
									for (VideoFileInfo dInfo : videoinfo.getInfo()) {
										SpeedInfo speedInfo = map.get(dInfo);
										if (speedInfo == null) {
											speedInfo = new SpeedInfo();
											speedInfo.start(dInfo.getCount());
											map.put(dInfo, speedInfo);
										}
									}
									break;
								case DOWNLOADING:
									long now = System.currentTimeMillis();
									if (now - 1000 > last) {
										last = now;

										String parts = "";

										for (VideoFileInfo dInfo : dInfoList) {
											SpeedInfo speedInfo = map.get(dInfo);
											speedInfo.step(dInfo.getCount());

											List<Part> pp = dInfo.getParts();
											if (pp != null) {
												// multipart download
												for (Part p : pp) {
													if (p.getState().equals(States.DOWNLOADING)) {
														parts += String.format(Locale.getDefault(), "part#%d(%.2f) ", p.getNumber(),
																p.getCount() / (float) p.getLength());
													}
												}
											}
											publishProgress((int) (dInfo.getCount() / (float) dInfo.getLength() * 100));
										}
									}
									break;
								default:
									break;
							}
						}
					};

					URL url = new URL(address);

					VGetParser user = null;

					// create proper html parser depends on url
					user = VGet.parser(url);

					// download limited video quality from youtube
					// user = new YouTubeQParser(YoutubeQuality.p480);

					// create proper videoInfo to keep specific video information
					videoInfo = user.info(url);

					VGet v = new VGet(videoInfo, new File(path));

					// [OPTIONAL] call v.extract() only if you d like to get video title
					// or download url link before start download. or just skip it.
					v.extract(user, stop, notify);

					Log.d("AsyncTask", "Title: " + videoInfo.getTitle());
					List<VideoFileInfo> list = videoInfo.getInfo();
					if (list != null) {
						for (DownloadInfo d : list) {
							Log.d("AsyncTask", "Download URL: " + d.getSource() + "\nDownload Type: " + d.getContentType());
							if (d.getContentType().contains("mp4")) {
								// TODO only download MP4, not webm
							}
						}
					}

					v.download(user, stop, notify);
				} catch (DownloadInterruptedError e) {
					System.out.println(videoInfo.getState());
				} catch (RuntimeException e) {
					throw e;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

				return null;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
				int progress = values[0];
				Log.d("AsyncTask", "Progress = " + progress + "%");
			}
		}.execute();
	}

	public static String formatSpeed(long s) {
		if (s > 0.1 * 1024 * 1024 * 1024) {
			float f = s / 1024f / 1024f / 1024f;
			return String.format(Locale.getDefault(), "%.1f GB/s", f);
		} else if (s > 0.1 * 1024 * 1024) {
			float f = s / 1024f / 1024f;
			return String.format(Locale.getDefault(), "%.1f MB/s", f);
		} else {
			float f = s / 1024f;
			return String.format(Locale.getDefault(), "%.1f kb/s", f);
		}
	}
}
