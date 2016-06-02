package io.github.mudassir.youtubecacher.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import io.github.mudassir.youtubecacher.R;
import io.github.mudassir.youtubecacher.obj.VideoMetadata;
import io.github.mudassir.youtubecacher.util.YoutubeScraper;

/**
 * First fragment that the user sees (i.e., the home screen)
 */
public class HomeFragment extends Fragment implements BaseRecyclerAdapter.RecyclerClickListener, YoutubeScraper.ScrapeReceiver {

	private List<VideoMetadata> mVideoList;
	private RecyclerView mRecyclerView;
	private WebView mWebView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, container, false);
		mRecyclerView = (RecyclerView) root.findViewById(R.id.home_recycler);
		mWebView = (WebView) root.findViewById(R.id.home_web_view);

		return root;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		YoutubeScraper.scrape(getActivity(), mWebView, this);
	}

	@Override
	public void onClick(View view, int position) {
		android.widget.Toast.makeText(getActivity(), YoutubeScraper.VIDEO_PREFIX_URL + mVideoList.get(position).getId(), android.widget.Toast.LENGTH_SHORT).show();
		// TODO replace with actually downloading the file
	}

	@Override
	public void onScrapeReceived(List<VideoMetadata> infoList) {
		mVideoList = infoList;
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setAdapter(new HomeAdapter(mVideoList, this));

		// Add the divider
		mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
			@Override
			public void onDraw(final Canvas c, final RecyclerView parent, final RecyclerView.State state) {
				final Drawable divider = ResourcesCompat.getDrawable(getResources(), R.drawable.divider, null);

				// Don't show the divider underneath the first and last cells
				for (int i = 0; i < parent.getChildCount() - 1; i++) {
					final View child = parent.getChildAt(i);
					divider.setBounds(0, child.getBottom(), parent.getWidth(), child.getBottom() + divider.getIntrinsicHeight());
					divider.draw(c);
				}

				/*
				 * In the event that padding must be preserved:
				 * final int padding = (int) getResources().getDimension(R.dimen.dividerPadding);
				 * final int left = parent.getPaddingLeft() + padding;
				 * final int right = parent.getWidth() - parent.getPaddingRight() + padding;
				*/
			}
		});
	}
}

class HomeAdapter extends BaseRecyclerAdapter<VideoMetadata, HomeViewHolder> {

	public HomeAdapter(@Nullable List<VideoMetadata> data, @Nullable RecyclerClickListener listener) {
		super(data, listener);
	}

	@Override
	public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new HomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_home, parent, false), listener);
	}

	@Override
	public void onBindViewHolder(final HomeViewHolder holder, int position) {
		VideoMetadata info = data.get(position);
		holder.title.setText(info.getTitle());
		Glide.with(holder.videoThumbnail.getContext())
				.load(YoutubeScraper.THUMBNAIL_PREFIX_URL + info.getId() + YoutubeScraper.THUMBNAIL_SUFFIX_URL)
				.into(holder.videoThumbnail);
		Glide.with(holder.channelThumbnail.getContext())
				.load(info.getChannelThumbnail())
				.asBitmap()
				.centerCrop()
				.into(new BitmapImageViewTarget(holder.channelThumbnail) {
					@Override
					protected void setResource(Bitmap resource) {
						RoundedBitmapDrawable circularBitmapDrawable =
								RoundedBitmapDrawableFactory.create(holder.channelThumbnail.getContext().getResources(), resource);
						circularBitmapDrawable.setCircular(true);
						holder.channelThumbnail.setImageDrawable(circularBitmapDrawable);
					}
				});
		holder.subTitle.setText(info.getChannel() + "\n"
				+ info.getViews() + " " + holder.channelThumbnail.getContext().getString(R.string.bullet_separator) + " " + info.getPostedTime());
		holder.duration.setText(info.getDuration());
	}
}

class HomeViewHolder extends BaseRecyclerAdapter.BaseViewHolder {

	TextView title;
	ImageView videoThumbnail;
	ImageView channelThumbnail;
	TextView subTitle;
	TextView duration;

	public HomeViewHolder(View view, @Nullable BaseRecyclerAdapter.RecyclerClickListener listener) {
		super(view, listener);
		title = (TextView) view.findViewById(R.id.title);
		videoThumbnail = (ImageView) view.findViewById(R.id.video_thumbnail);
		channelThumbnail = (ImageView) view.findViewById(R.id.channel_thumbnail);
		subTitle = (TextView) view.findViewById(R.id.subtitle);
		duration = (TextView) view.findViewById(R.id.duration);
	}
}
