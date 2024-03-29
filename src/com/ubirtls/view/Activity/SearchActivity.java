package com.ubirtls.view.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ubirtls.R;
import com.ubirtls.R.string;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 搜索界面。包括多个图标，用户可以选择某项进行不同类别的搜索，包括首字母查询，关键字查询等。
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class SearchActivity extends Activity implements OnItemClickListener{
	/** 网格视图，该界面是多个图标的网格分布 */
	private GridView gridView;
	/** 图标id数组 */
	private int[] images;
	/** 标题字符串数组 */
	private String[] titiles;

	// 在Activity首次创建时调用
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		gridView = (GridView) findViewById(R.id.grid);

		/* 初始化图标和字符串资源 */
		images = new int[] {
				R.drawable.keyword_search, R.drawable.history,
				R.drawable.collect};
		titiles = new String[] {
				
				getResources().getString(R.string.keyword_search),
				getResources().getString(string.history),
				getResources().getString(R.string.collect),
				 };
		/* 图标和字符串标题映射 */
		List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 3; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("PIC", images[i]);
			map.put("TITLE", titiles[i]);
			contents.add(map);
		}
		/* 将映射添加到Adapter */
		SimpleAdapter adapter = new SimpleAdapter(this,
				(List<Map<String, Object>>) contents, R.layout.collect,
				new String[] { "PIC", "TITLE" }, new int[] {
						R.id.griditem_image, R.id.griditem_name });
		gridView.setAdapter(adapter);
		/*设置gridView监听*/
		gridView.setOnItemClickListener(this);
	}

	// 在Activity创建后或重启后调用
	public void onStart() {
		super.onStart();
		this.setTitle(R.string.search_main);
	}

	// 在Activity暂停时调用
	protected void onPause() {
		super.onPause();
	}

	// onStart后会调用
	protected void onResume() {
		super.onResume();
	}

	// 在Activity 销毁时调用
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		// TODO Auto-generated method stub
		switch(position){
		case 0:
			Intent intent1 = new Intent(SearchActivity.this, KeywordsSearchActivity.class);
			startActivity(intent1);
		    this.finish();
			break;
		case 1:
			Intent intent2 = new Intent(SearchActivity.this, HistoricRecordActivity.class);
			startActivity(intent2);
			break;
		case 2:
			Intent intent3 = new Intent(SearchActivity.this, CollectContentsActivity.class);
			startActivity(intent3);
		    this.finish();
			break;
		case 3:
			
		    this.finish();
			break;
		}
	}
}
