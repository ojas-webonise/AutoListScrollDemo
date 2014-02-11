package com.demolistapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

public class ListUsingScroll extends Activity {
	
	ScrollView scrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		scrollView = (ScrollView)findViewById(R.id.listScroll);
		((ListView)findViewById(R.id.listExpand)).setVisibility(View.GONE);
		scrollView.setVisibility(View.VISIBLE);
	}
	
}
