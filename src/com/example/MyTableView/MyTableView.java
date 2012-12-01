package com.example.MyTableView;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MyTableView extends TiUIView implements ListView.OnItemClickListener {
	private ListView listView;
	private MyTableViewAdapter adapter;

	public MyTableView(MyTableViewProxy proxy) {
		super(proxy);

		listView = new ListView(proxy.getActivity());
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setOnItemClickListener(this);
		Object data = proxy.getProperty(TiC.PROPERTY_DATA);
		if (data instanceof Object[]) {
			adapter = new MyTableViewAdapter(proxy, (Object[]) data);
			Object headerView = proxy.getProperty(TiC.PROPERTY_HEADER_VIEW);
			if (headerView instanceof TiViewProxy) {
				adapter.setHeaderView((TiViewProxy) headerView);
			}
			listView.setAdapter(adapter);
		}
		setNativeView(listView);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		KrollDict event = new KrollDict();
		event.put(TiC.EVENT_PROPERTY_INDEX, position);
		event.put(TiC.EVENT_PROPERTY_ROW, adapter.getItem(position));
		proxy.fireEvent(TiC.EVENT_CLICK, event);
	}
}
