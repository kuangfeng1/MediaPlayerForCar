package org.videolan.vlc.util;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextView extends TextView {

	/**
	 * constructor
	 * 
	 * @param context
	 *            Context
	 */
	public MarqueeTextView(Context context) {
		super(context);
	}

	/**
	 * constructor
	 * 
	 * @param context
	 *            Context
	 * @param attrs
	 *            AttributeSet
	 */
	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * constructor
	 * 
	 * @param context
	 *            Context
	 * @param attrs
	 *            AttributeSet
	 * @param defStyle
	 *            int
	 */
	public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isFocused() {
		return true;
	}


}