package io.gitlab.mudassir.youtubecacher.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Image view that forces 16:9 aspect ratio.
 * <br>
 * Make sure to have <code>width=match_parent</code>
 * and <code>height=wrap_content</code>
 */
public class SixteenByNineView extends ImageView {

	public SixteenByNineView(Context context) {
		this(context, null);
	}

	public SixteenByNineView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SixteenByNineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = Resources.getSystem().getDisplayMetrics().widthPixels;
		setMeasuredDimension(width, width * 9 / 16);
	}
}
