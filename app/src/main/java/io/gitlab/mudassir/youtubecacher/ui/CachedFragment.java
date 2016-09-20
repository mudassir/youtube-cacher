package io.gitlab.mudassir.youtubecacher.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.gitlab.mudassir.youtubecacher.PlayerActivity;
import io.gitlab.mudassir.youtubecacher.R;

/**
 * Fragment showing the files that are downloaded
 */
public class CachedFragment extends Fragment implements BaseRecyclerAdapter.RecyclerClickListener {

	private CacheAdapter mAdapter;
	private List<File> mFiles;
	private RecyclerView mRecyclerView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_cached, container, false);
		mRecyclerView = (RecyclerView) root.findViewById(R.id.cache_recycler);
		refresh();

		return root;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mRecyclerView.setHasFixedSize(true);
		mAdapter = new CacheAdapter(mFiles, this);
		mRecyclerView.setAdapter(mAdapter);
	}

	/**
	 * Loads a list of files that are in the internal app storage directory
	 */
	public void refresh() {
		// TODO
		File root = getActivity().getFilesDir();

		// Find only MP4 files
		File[] videos = root.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".mp4");
			}
		});

		mFiles = new ArrayList<>(Arrays.asList(videos));

		if (mAdapter != null) {
			mAdapter.swap(mFiles);
		}
	}

	@Override
	public void onClick(View view, int position) {
		if (view.getId() == R.id.delete) {
			boolean delete = mFiles.get(position).getAbsoluteFile().delete();
			if (delete) {
				mFiles.remove(position);
				mAdapter.remove(position);
			}
		} else {
			Intent intent = new Intent(getActivity(), PlayerActivity.class);
			intent.putExtra(PlayerActivity.VIDEO_FILE, mFiles.get(position));
			startActivity(intent);
		}
	}
}

class CacheAdapter extends BaseRecyclerAdapter<File, CacheViewHolder> {

	public CacheAdapter(@Nullable List<File> data, @Nullable RecyclerClickListener listener) {
		super(data, listener);
	}

	@Override
	public CacheViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new CacheViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_cached, parent, false), listener);
	}

	@Override
	public void onBindViewHolder(final CacheViewHolder holder, int position) {
		Cursor cursor = MediaStore.Video.query(
				holder.title.getContext().getContentResolver(),
				Uri.parse(data.get(position).getAbsolutePath()),
				new String[]{
					MediaStore.Video.VideoColumns.DURATION,
					MediaStore.MediaColumns.SIZE,
					MediaStore.MediaColumns.TITLE
				});
		holder.title.setText(data.get(position).getName());
		Glide.with(holder.thumbnail.getContext())
				.load(Uri.fromFile(data.get(position)))
				.into(holder.thumbnail);
		holder.subtitle.setText("-- kB");
		holder.duration.setText("88:88:88");
	}
}

class CacheViewHolder extends BaseRecyclerAdapter.BaseViewHolder {

	TextView title;
	ImageView thumbnail;
	TextView subtitle;
	TextView duration;

	public CacheViewHolder(View view, final BaseRecyclerAdapter.RecyclerClickListener listener) {
		super(view, listener);
		title = (TextView) view.findViewById(R.id.title);
		thumbnail = (ImageView) view.findViewById(R.id.video_thumbnail);
		subtitle = (TextView) view.findViewById(R.id.subtitle);
		duration = (TextView) view.findViewById(R.id.duration);

		view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v, getAdapterPosition());
			}
		});
	}
}
