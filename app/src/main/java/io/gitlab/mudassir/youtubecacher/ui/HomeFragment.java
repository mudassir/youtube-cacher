package io.gitlab.mudassir.youtubecacher.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.gitlab.mudassir.youtubecacher.R;
import io.gitlab.mudassir.youtubecacher.model.DownloadListener;
import io.gitlab.mudassir.youtubecacher.util.Common;

/**
 * First fragment that the user sees (i.e., the home screen)
 */
public class HomeFragment extends Fragment {

	private WebView mWebView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, container, false);

		mWebView = (WebView) root.findViewById(R.id.home_web_view);
		mWebView.getSettings().setJavaScriptEnabled(true);

		mWebView.setWebViewClient(new WebViewClient() {
			/**
			 * This method intendeds to intercept the video URL that loads from a user click.
			 * Since each request is received individually, the request is checked to see if
			 * it has a gesture associated with it (i.e., it was clicked). If so, the url
			 * must be checked to make sure it is not a search url. If it is a video url,
			 * it is delegated to the main activity where the download is processed.
			 */
			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
				// The url should have a value for the parameter v (i.e., youtube.com/watch?v=VIDEOID)
				String videoId = request.getUrl().getQueryParameter("v");
				if (request.hasGesture() && !TextUtils.isEmpty(videoId)) {
					((DownloadListener) getActivity()).download(Common.VIDEO_PREFIX_URL + videoId);
					/* Reset web view
					 *
					 * Can't call loadUrl() directly on the view because of some 8 year old bug,
					 * so posting this runnable is a workaround
					 */
					view.getHandler().post(() -> view.loadUrl("https://m.youtube.com/"));
				}
				return null;
			}
		});

		return root;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mWebView.loadUrl("https://m.youtube.com/");
	}
}
