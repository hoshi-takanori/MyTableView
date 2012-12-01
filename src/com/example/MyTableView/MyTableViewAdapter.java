package com.example.MyTableView;

import java.util.Map;

import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

public class MyTableViewAdapter implements ListAdapter {
	public static final int VIEWTYPE_TABLE_HEADER = 0;
	public static final int VIEWTYPE_SECTION_HEADER = 1;
	public static final int VIEWTYPE_TABLE_ROW = 2;
	public static final int VIEWTYPE_COUNT = 3;

	private MyTableViewProxy proxy;
	private TiViewProxy headerView;
	private Object[] list;

	public MyTableViewAdapter(MyTableViewProxy proxy, Object[] list) {
		this.proxy = proxy;
		this.list = list;
	}

	public void setHeaderView(TiViewProxy headerView) {
		this.headerView = headerView;
	}

	@Override
	public int getCount() {
		return list.length + (headerView != null ? 1 : 0);
	}

	@Override
	public boolean isEmpty() {
		return getCount() > 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public Object getItem(int position) {
		if (headerView != null && position == 0) {
			return headerView;
		}
		return list[position - (headerView != null ? 1 : 0)];
	}

	@Override
	public int getViewTypeCount() {
		return VIEWTYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		if (headerView != null && position == 0) {
			return VIEWTYPE_TABLE_HEADER;
		} else if (getItem(position) instanceof String) {
			return VIEWTYPE_SECTION_HEADER;
		} else {
			return VIEWTYPE_TABLE_ROW;
		}
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		if (headerView != null && position == 0) {
			return false;
		}
		return ! (getItem(position) instanceof String);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (headerView != null && position == 0) {
			return getTableHeader(convertView, parent);
		}
		Object data = getItem(position);
		if (data instanceof String) {
			return getSectionHeader((String) data, convertView, parent);
		} else {
			return getTableRow(position, data, convertView, parent);
		}
	}

	private View getTableHeader(View convertView, ViewGroup parent) {
		if (convertView != null) {
			return convertView;
		} else {
			View view = headerView.getOrCreateView().getOuterView();
			String height = TiConvert.toString(headerView.getProperty(TiC.PROPERTY_HEIGHT));
			if (height != null) {
				view.setMinimumHeight(TiConvert.toTiDimension(height, 0).getAsPixels(view));
			}
			return view;
		}
	}

	private View getSectionHeader(String text, View convertView, ViewGroup parent) {
		TextView view = (TextView) convertView;
		if (view == null) {
			view = new TextView(proxy.getActivity());
			view.setEnabled(false);
			view.setBackgroundColor(Color.DKGRAY);
			view.setTextColor(Color.WHITE);
		}
		view.setText(text);
		return view;
	}

	private View getTableRow(int position, Object data, View convertView, ViewGroup parent) {
		MyTableViewRow view = (MyTableViewRow) convertView;
		if (view == null) {
			String deleteText = (String) proxy.getProperty(MyTableViewProxy.PROPERTY_DELETE_BUTTON);
			view = new MyTableViewRow(proxy.getActivity(), deleteText);
		}
		if (data instanceof Map) {
			view.setData(proxy, position, (Map<?, ?>) data);
		}
		return view;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
	}
}
