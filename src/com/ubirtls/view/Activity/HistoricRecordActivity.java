
package com.ubirtls.view.Activity;

import android.app.Activity;
/**
 * ��������Ϣ���棬������Ӧ���������ղص����Ϣ��
 * @author �����
 * @version 1.0
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ubirtls.Controller;
import com.ubirtls.R;
import com.ubirtls.util.DatabaseManager;
import com.ubirtls.util.OverlayItem;
import com.ubirtls.util.POI;


import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ����������棬��ʾ�����������Ľ����
 * 
 * @author �����
 * @version 1.0
 */
public class HistoricRecordActivity extends Activity implements OnClickListener, DatabaseConstants{
	/** �������Ľ������ */
	private int searchResultCount = 0;
	/** ����������һ��ListView�ؼ� */
	private ListView listView;
	/** listView��Ӧ������ */
//	private List<Map<String, Object>> content;
	/** ���ذ�ť��������һ������ */
	private Button clearButton;
	/**ButtonBar ����������� �յ��ѡ��*/
	private LinearLayout buttonBar;
	/**��ǰ��ѡ�е�Item*/
	private View currentItem = null;
	/**��ǰ��ѡ�е�itemλ��*/
	private int position = -1;
	/**���ݿ������*/
	private DatabaseManager databaseManager = null;
	private ArrayList<POI> pois = null;
	/* ��Activity�״δ���ʱ���� */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* �Զ�������� */
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.search_result_list);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.search_result_historical_titlebar);
		/*�������ݿ��POI����*/
		databaseManager = new DatabaseManager(this);
		databaseManager.createPoiTable();
		/* ��ʼ��UI���� */
		buttonBar = (LinearLayout)findViewById(R.id.search_result_button_bar);
		((Button)findViewById(R.id.search_result_start_point_button)).setOnClickListener(this);
		((Button)findViewById(R.id.search_result_end_point_button)).setOnClickListener(this);
		//��ֹ�ղذ�ť
		((Button)findViewById(R.id.search_result_show_on_button)).setOnClickListener(this);

		clearButton = (Button) findViewById(R.id.clearbutton);
		clearButton.setOnClickListener(this);
		TextView searchResultContentView = (TextView) findViewById(R.id.result_size);
		listView = (ListView) findViewById(R.id.search_result_listview);
		
		/*��ѯ���е��ղص�*/
		pois = databaseManager.searchHistoricalPois();
		searchResultCount = pois.size();
		searchResultContentView.setText(searchResultCount+"����ʷ��");
		/*����Щ�����չʾ*/
		List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < searchResultCount; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("poi", pois.get(i).description);
			map.put("coor", pois.get(i).coordinate.toString());
			contents.add(map);
		}
		/* ��ӳ�����ӵ�Adapter */
		SimpleAdapter adapter = new SimpleAdapter(this,
				(List<Map<String, Object>>) contents,
				R.layout.search_result_listi_tem,
				new String[] { "poi", "coor" }, new int[] {
						R.id.search_result_list_poi,
						R.id.search_result_list_coor });
		listView.setAdapter(adapter);
		// ���ü���
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View item,
					int position, long arg3) {
				/*ѡ��ĳ��Item*/
				if(currentItem != null)
					currentItem.setBackgroundColor(Color.TRANSPARENT);
				HistoricRecordActivity.this.position = position;
				currentItem = item;
				currentItem.setBackgroundColor(Color.BLUE);
				buttonBar.setVisibility(View.VISIBLE);
			}
		});
	}

	/* ��Activity���������������� */
	public void onStart() {
		super.onStart();
	}

	/* ��Activity��ͣʱ���� */
	protected void onPause() {
		super.onPause();
	}

	/* onStart������ */
	protected void onResume() {
		super.onResume();
	}

	/* ��Activity ����ʱ���� */
	protected void onDestroy() {
		super.onDestroy();
		if(databaseManager != null)
		databaseManager.close();
	}
	@Override
	public void onBackPressed(){
		Intent intent = new Intent();
		intent.setClass(HistoricRecordActivity.this, SearchActivity.class);
		startActivity(intent);
		HistoricRecordActivity.this.finish();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//����ղص�
		if (v.getId() == clearButton.getId()) {
            databaseManager.clearHistoricalPois();
			Intent intent = new Intent();
			intent.setClass(HistoricRecordActivity.this, HistoricRecordActivity.class);
			startActivity(intent);
			HistoricRecordActivity.this.finish();
		}
		//�������
		else if(v.getId() == R.id.search_result_start_point_button){
			
		}
		//�����յ�
		else if(v.getId() == R.id.search_result_end_point_button){
			
		}
		//�ڵ�ͼ����ʾ��
		else if(v.getId() == R.id.search_result_show_on_button){
			/* �����Ӧ��λ�� */
			Controller.getInstance().addMarkerItem(
					new OverlayItem(pois.get(position).getDescription(), pois.get(position).getCoordinate(), null));

			// ��ת��MapActivity���� 
			Intent intent = new Intent(HistoricRecordActivity.this,
					MapActivity.class);
			startActivity(intent);

			HistoricRecordActivity.this.finish();
			// ����λ�� 
			Controller.getInstance().followPosition(
					pois.get(position).getCoordinate());
		}
	}
}