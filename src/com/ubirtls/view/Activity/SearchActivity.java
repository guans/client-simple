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
 * �������档�������ͼ�꣬�û�����ѡ��ĳ����в�ͬ������������������ĸ��ѯ���ؼ��ֲ�ѯ�ȡ�
 * 
 * @author �����
 * @version 1.0
 */
public class SearchActivity extends Activity implements OnItemClickListener{
	/** ������ͼ���ý����Ƕ��ͼ�������ֲ� */
	private GridView gridView;
	/** ͼ��id���� */
	private int[] images;
	/** �����ַ������� */
	private String[] titiles;

	// ��Activity�״δ���ʱ����
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		gridView = (GridView) findViewById(R.id.grid);

		/* ��ʼ��ͼ����ַ�����Դ */
		images = new int[] {
				R.drawable.keyword_search, R.drawable.history,
				R.drawable.collect};
		titiles = new String[] {
				
				getResources().getString(R.string.keyword_search),
				getResources().getString(string.history),
				getResources().getString(R.string.collect),
				 };
		/* ͼ����ַ�������ӳ�� */
		List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 3; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("PIC", images[i]);
			map.put("TITLE", titiles[i]);
			contents.add(map);
		}
		/* ��ӳ�����ӵ�Adapter */
		SimpleAdapter adapter = new SimpleAdapter(this,
				(List<Map<String, Object>>) contents, R.layout.collect,
				new String[] { "PIC", "TITLE" }, new int[] {
						R.id.griditem_image, R.id.griditem_name });
		gridView.setAdapter(adapter);
		/*����gridView����*/
		gridView.setOnItemClickListener(this);
	}

	// ��Activity����������������
	public void onStart() {
		super.onStart();
		this.setTitle(R.string.search_main);
	}

	// ��Activity��ͣʱ����
	protected void onPause() {
		super.onPause();
	}

	// onStart������
	protected void onResume() {
		super.onResume();
	}

	// ��Activity ����ʱ����
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