package com.demolistapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity {

	ListView list;
	RelativeLayout relativeLayoutListparent;
	private View prevOpenListItem;
	private int iCurrentOpenedItem = -1;
	private ListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		relativeLayoutListparent = (RelativeLayout)findViewById(R.id.relativeLayoutListparent);
		list = (ListView) findViewById(R.id.listExpand);
		adapter = new ListAdapter(this);
		list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class ListAdapter extends BaseAdapter {

		Context context;
		LayoutInflater inflater;

		public ListAdapter(Context c){
			this.context = c;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View viewHolder = inflater.inflate(R.layout.list_item, parent, false);
			LinearLayout llListItemTitle = (LinearLayout) viewHolder.findViewById(R.id.llListItemTitle);
			ScrollView scrollViewContents = (ScrollView) viewHolder.findViewById(R.id.svListItemContents);
			TextView textDescript = (TextView) viewHolder.findViewById(R.id.textDescript);
			if (iCurrentOpenedItem != -1 && iCurrentOpenedItem == position) {
				scrollViewContents.setVisibility(View.VISIBLE);	
			} else {
				scrollViewContents.setVisibility(View.GONE);
			}
			if (position % 2 == 1) {
				textDescript.setText("Lorem ipsum dolor sit amet\nLorem ipsum dolor sit amet\nLorem ipsum dolor sit amet");
			}
			llListItemTitle.setTag(new TagItem(scrollViewContents, position));
			llListItemTitle.setOnClickListener(listItemClickListener);
			scrollViewContents.setOnTouchListener(touchListener);
			return viewHolder;
		}
	}
	
	private OnClickListener listItemClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			scrollList(v);
		}
	};
	
	private OnTouchListener touchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.getParent().requestDisallowInterceptTouchEvent(true);
				break;

			case MotionEvent.ACTION_UP:
				v.getParent().requestDisallowInterceptTouchEvent(false);
				break;
			}
			v.onTouchEvent(event);
			return true;
		}
	};

	public void scrollList(final View v){
		TagItem item = (TagItem)v.getTag();
		final int listPos = Integer.valueOf(item.listPos);
		if (item.listContent.getVisibility() == View.VISIBLE) {
			item.listContent.setVisibility(View.GONE);
			iCurrentOpenedItem = -1;
		} else {
			iCurrentOpenedItem = listPos;
			if(prevOpenListItem != null && prevOpenListItem != item.listContent){
				prevOpenListItem.setVisibility(View.GONE);
			}
			int scrollHeight = getScrollHeight(relativeLayoutListparent.getHeight(),
					listPos, v.getHeight());
			item.listContent.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, scrollHeight));
			item.listContent.setVisibility(View.VISIBLE);
			prevOpenListItem = item.listContent;
		}
		postListScroll(listPos);
	}
	
	private int getScrollHeight(int iParentHeight, int listPos, int iSelectedItemHeaderHeight) {
		int height = iSelectedItemHeaderHeight;
		View childView = adapter.getView(listPos + 1, null, list);
		if (childView != null) {
			childView.measure( MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			height += childView.getMeasuredHeight();
		}
		int padding = 60;
		height = iParentHeight - height - padding;
		return height;
	}

	private void postListScroll(final int position){
		list.post(new Runnable() {
			@Override
			public void run() {
				list.smoothScrollToPositionFromTop(position, 0);
			}
		});
	}

	public class TagItem {

		View listContent;
		int listPos;

		public TagItem(View v, int listPos) {
			this.listContent = v;
			this.listPos = listPos; 
		}
	}

}
