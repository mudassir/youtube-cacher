package io.github.mudassir.youtubecacher.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.Collections;
import java.util.List;

/**
 * A class that takes care of some boilerplate code with RecyclerView Adapters and ViewHolders.
 */
public abstract class BaseRecyclerAdapter<E extends Object, T extends BaseRecyclerAdapter.BaseViewHolder> extends RecyclerView.Adapter<T> {

	public interface RecyclerClickListener {
		void onClick(View view, int position);
	}

	public static abstract class BaseViewHolder extends RecyclerView.ViewHolder {

		public BaseViewHolder(final View view, @Nullable final RecyclerClickListener listener) {
			super(view);
			if (listener != null) {
				view.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(final View v) {
						listener.onClick(v, getAdapterPosition());
					}
				});
			}
		}
	}

	@NonNull
	protected List<E> data;

	@Nullable
	protected RecyclerClickListener listener;

	public BaseRecyclerAdapter(@Nullable final List<E> data, @Nullable RecyclerClickListener listener) {
		this.data = data != null ? data : Collections.<E>emptyList();
		this.listener = listener;
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	public void swap(final List<E> newData) {
		data.clear();
		data.addAll(newData);
		notifyDataSetChanged();
	}
}
