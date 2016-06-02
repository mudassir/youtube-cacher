package io.github.mudassir.youtubecacher.util;

import android.content.Context;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.github.mudassir.youtubecacher.obj.VideoMetadata;

/**
 * File that scrapes the YouTube homepage for videos
 *
 * Solution adapted from https://stackoverflow.com/a/4892013
 * and https://stackoverflow.com/a/32040564/1319292
 */
public class YoutubeScraper {

	public interface ScrapeReceiver {
		void onScrapeReceived(List<VideoMetadata> infoList);
	}

	public static final String ID_PREFIX = "/watch?v=";
	public static final String THUMBNAIL_PREFIX_URL = "https://i.ytimg.com/vi/";
	public static final String THUMBNAIL_SUFFIX_URL = "/mqdefault.jpg";
	public static final String TRENDING_FEED_URL = "https://m.youtube.com/feed/trending";
	public static final String CHANNEL_PREFIX = "/user/";
	public static final String VIDEO_PREFIX_URL = "https://www.youtube.com" + ID_PREFIX;

	public static void scrape(final Context context, final WebView webView, final ScrapeReceiver receiver) {

		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// Using postDelayed() so the WebView can load all the data from the JavaScript;
				// simply using post() does not work properly.
				webView.postDelayed(new Runnable() {
					@Override
					public void run() {
						webView.evaluateJavascript(
								"(function() { return ('<html>'+document.getElementsByTagName('body')[0].innerHTML+'</html>'); })();",
								new ValueCallback<String>() {
									@Override
									public void onReceiveValue(String html) {
										List<VideoMetadata> idList = new ArrayList<>();
										html = StringEscapeUtils.unescapeJava(html);

										// Extreme brute force parsing
										for (Element _mhb : Jsoup.parse(html).getElementsByClass("_mhb")) {
											if (_mhb.attr("data-index").equals("0")) {
												VideoMetadata.Builder builder = new VideoMetadata.Builder();

												Elements hrefs = _mhb.select("a");
												builder.channelThumbnail(hrefs.get(0).select("img").attr("src"));
												builder.id(hrefs.get(1).attr("href").replace(ID_PREFIX, ""));
												// Stripping title
												String title = hrefs.get(1).attr("aria-label");
												title = title.substring(0, title.lastIndexOf(" - "));
												title = title.substring(0, title.lastIndexOf(" - "));
												builder.title(title);

												for (Element _mebb : _mhb.getElementsByClass("_mebb")) {
													if (!_mebb.attr("aria-label").equals("")) {
														builder.duration(_mebb.text());
													} else if (_mebb.text().contains("ago")) {
														builder.postedTime(_mebb.text());
													} else if (_mebb.text().contains("views")) {
														builder.views(_mebb.text());
													} else if (!_mebb.text().equals(title) && !_mebb.text().equals("CC")) {
														builder.channel(_mebb.text());
													}
												}

												idList.add(builder.build());
											}
										}

										receiver.onScrapeReceived(idList);
										webView.destroy(); // Don't need it anymore
									}
								}
						);
					}
				}, 250);
			}
		});

		webView.loadUrl(TRENDING_FEED_URL);
	}
}
