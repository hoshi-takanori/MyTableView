package com.example.MyTableView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.util.TiUIHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyTableViewRow extends RelativeLayout {
	public static final int ID_IMAGE_VIEW = 1;
	public static final int ID_TITLE_LABEL = 2;
	public static final int ID_DESCRIPTION_LABEL = 3;
	public static final int ID_DELETE_BUTTON = 4;

	public static final String PROPERTY_DESCRIPTION = "description";

	public static final String EVENT_DELETE = "delete";

	private ImageView imageView;
	private TextView titleLabel;
	private TextView descriptionLabel;
	private Button deleteButton;

	public MyTableViewRow(Context context, String deleteText) {
		super(context);

		Drawable normal = new ColorDrawable(Color.WHITE);
		Drawable selected = getResources().getDrawable(android.R.drawable.list_selector_background);
		setBackgroundDrawable(createBackground(normal, selected));

		int padding = (int) TiUIHelper.getRawDIPSize(4, context);
		this.setPadding(padding, padding, padding, padding);

		imageView = createImageView(context, ID_IMAGE_VIEW, 40, 40, 4, 10);
		int leftOf = deleteText != null ? ID_DELETE_BUTTON : 0;
		titleLabel = createTextView(context, ID_TITLE_LABEL, Color.BLACK, 16, Typeface.BOLD, 0, leftOf);
		descriptionLabel = createTextView(context, ID_DESCRIPTION_LABEL, Color.BLACK, 12, Typeface.NORMAL, ID_TITLE_LABEL, leftOf);
		if (deleteText != null) {
			deleteButton = createButton(context, ID_DELETE_BUTTON, deleteText, 4);
		}
	}

	private Drawable createBackground(Drawable normal, Drawable selected) {
		StateListDrawable background = new StateListDrawable();

		background.addState(new int[] {
				android.R.attr.state_window_focused,
				android.R.attr.state_enabled,
				android.R.attr.state_pressed }, selected);
		background.addState(new int[] { android.R.attr.state_selected }, selected);

		background.addState(new int[] {
				android.R.attr.state_focused,
				android.R.attr.state_window_focused,
				android.R.attr.state_enabled }, normal);
		background.addState(new int[0], normal);

		return background;
	}

	private ImageView createImageView(Context context, int id, int width, int height, int margin, int rightMargin) {
		ImageView view = new ImageView(context);
		view.setId(id);

		int imageWidth = (int) TiUIHelper.getRawDIPSize(width, context);
		int imageHeight = (int) TiUIHelper.getRawDIPSize(height, context);
		LayoutParams params = new LayoutParams(imageWidth, imageHeight);
		params.topMargin = (int) TiUIHelper.getRawDIPSize(margin, context);
		params.leftMargin = (int) TiUIHelper.getRawDIPSize(margin, context);
		params.rightMargin = (int) TiUIHelper.getRawDIPSize(rightMargin, context);
		params.bottomMargin = (int) TiUIHelper.getRawDIPSize(margin, context);
		params.addRule(ALIGN_PARENT_LEFT);
		addView(view, params);

		return view;
	}

	private TextView createTextView(Context context, int id, int color, int size, int style, int below, int leftOf) {
		TextView view = new TextView(context);
		view.setId(id);
		view.setTextColor(color);
		view.setTextSize(size);
		view.setTypeface(null, style);

		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RIGHT_OF, ID_IMAGE_VIEW);
		if (below != 0) {
			params.addRule(BELOW, below);
		}
		if (leftOf != 0) {
			params.addRule(LEFT_OF, leftOf);
		}
		addView(view, params);

		return view;
	}

	private Button createButton(Context context, int id, String deleteText, int leftMargin) {
		Button button = new Button(context);
		button.setId(id);
		button.setFocusable(false);
		button.setFocusableInTouchMode(false);
		button.setText(deleteText);

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = (int) TiUIHelper.getRawDIPSize(leftMargin, context);
		params.addRule(ALIGN_PARENT_RIGHT);
		params.addRule(CENTER_VERTICAL);
		addView(button, params);

		return button;
	}

	public void setData(final MyTableViewProxy proxy, final int position, final Map<?, ?> data) {
		Bitmap bitmap = loadImage(proxy, (String) data.get(TiC.PROPERTY_IMAGE));
		imageView.setImageBitmap(bitmap);

		titleLabel.setText((String) data.get(TiC.PROPERTY_TITLE));
		descriptionLabel.setText((String) data.get(PROPERTY_DESCRIPTION));

		if (deleteButton != null) {
			deleteButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					KrollDict event = new KrollDict();
					event.put(TiC.EVENT_PROPERTY_INDEX, position);
					event.put(TiC.EVENT_PROPERTY_ROW, data);
					proxy.fireEvent(EVENT_DELETE, event);
				}
			});
		}
	}

	private Bitmap loadImage(MyTableViewProxy proxy, String path) {
		InputStream stream = null;
		try {
			if (path.startsWith("file:")) {
				stream = new URL(path).openStream();
			} else {
				stream = proxy.getActivity().getAssets().open("Resources/" + path);
			}
		} catch (IOException e) {
			return null;
		}

		Bitmap bitmap = null;
		if (stream != null) {
			try {
				bitmap = BitmapFactory.decodeStream(stream);
			} finally {
				try {
					stream.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
		return bitmap;
	}
}
