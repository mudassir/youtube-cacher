package io.github.mudassir.youtubecacher.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.mudassir.youtubecacher.R;

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
		android.widget.Toast.makeText(getActivity(), "refresh", android.widget.Toast.LENGTH_SHORT).show();
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
		// TODO play selected video
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
		holder.text.setText(data.get(position).toString());
		// Thumbnail:
		// Bitmap bMap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
	}
}

class CacheViewHolder extends BaseRecyclerAdapter.BaseViewHolder {

	TextView text;

	public CacheViewHolder(View view, @Nullable BaseRecyclerAdapter.RecyclerClickListener listener) {
		super(view, listener);
		text = (TextView) view.findViewById(R.id.text);
	}
}
