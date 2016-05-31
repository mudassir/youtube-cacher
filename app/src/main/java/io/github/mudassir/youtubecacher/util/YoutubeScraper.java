package io.github.mudassir.youtubecacher.util;

import android.content.Context;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * File that scrapes the YouTube homepage for videos
 *
 * Solution adapted from http://stackoverflow.com/a/4892013
 */
public class YoutubeScraper {

	public interface ScrapeReceiver {
		void onScrapeReceived(List<String> idList);
	}

	public static final String THUMBNAIL_PREFIX_URL = "https://i.ytimg.com/vi/";
	public static final String THUMBNAIL_SUFFIX_URL = "/mqdefault.jpg";
	public static final String TRENDING_FEED_URL = "https://m.youtube.com/feed/trending";
	public static final String VIDEO_PREFIX_URL = "https://www.youtube.com/watch?v=";

	public static void scrape(final Context context, final WebView webView, final ScrapeReceiver receiver) {

		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				webView.postDelayed(new Runnable() {
					@Override
					public void run() {
						webView.evaluateJavascript(
								"(function() { return ('<html>'+document.getElementsByTagName('body')[0].innerHTML+'</html>'); })();",
								new ValueCallback<String>() {
									@Override
									public void onReceiveValue(String html) {
										html = StringEscapeUtils.unescapeJava(html);
										List<String> idList = new ArrayList<>();
										for(Element element : Jsoup.parse(html).select("a")) {
											if (element.attr("href").contains("/watch?v="))
												idList.add(element.attr("href").replace("/watch?v=",""));
										}
										receiver.onScrapeReceived(idList);
										webView.destroy();
									}
								}
						);
					}
				}, 1000);
			}
		});

		webView.loadUrl(TRENDING_FEED_URL);
	}
}
