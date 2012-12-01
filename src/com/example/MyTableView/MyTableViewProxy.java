package com.example.MyTableView;

import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

import android.app.Activity;
import android.os.Message;
import android.widget.ListView;

@Kroll.proxy(creatableInModule=MyTableViewModule.class, propertyAccessors = {
	TiC.PROPERTY_DATA,
	TiC.PROPERTY_HEADER_VIEW,
	MyTableViewProxy.PROPERTY_DELETE_BUTTON,
})
public class MyTableViewProxy extends TiViewProxy {
	public static final String PROPERTY_DELETE_BUTTON = "deleteButton";

	public static final int MSG_SCROLL_TO = TiViewProxy.MSG_LAST_ID + 9901;

	@Override
	public TiUIView createView(Activity activity) {
		TiUIView view = new MyTableView(this);
		view.getLayoutParams().autoFillsHeight = true;
		view.getLayoutParams().autoFillsWidth = true;
		return view;
	}

	@Kroll.method
	public void scrollTo(int position) {
		Message message = getMainHandler().obtainMessage(MSG_SCROLL_TO);
		message.arg1 = position;
		message.sendToTarget();
	}

	@Override
	public boolean handleMessage(Message message) {
		if (message.what == MSG_SCROLL_TO) {
			ListView listView = (ListView) this.getOrCreateView().getNativeView();
			listView.setSelectionFromTop(message.arg1, 0);
			return true;
		}

		return super.handleMessage(message);
	}
}
