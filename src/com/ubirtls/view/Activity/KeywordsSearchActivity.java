/**
 * 
 */
package com.ubirtls.view.Activity;

import com.ubirtls.Controller;
import com.ubirtls.QueryListener;
import com.ubirtls.R;
import com.ubirtls.config.MapNavigateContants;
import com.ubirtls.event.Query_Result;
import com.ubirtls.util.OverlayItem;
import com.ubirtls.view.map.MarkOverlay;

import coordinate.TwoDCoordinate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 *关键字搜索界面，根据输入的关键字查询相应区域的地点
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class KeywordsSearchActivity extends Activity implements OnClickListener,QueryListener {

	/** 关键字搜索界面，根据输入的关键字查询相应区域的地点 */
	private Spinner areaSpinner;
	/** 返回按钮，返回上一个界面 */
	private Button backButton;
	/** 编辑框，输入关键字进行搜索 */
	private EditText searchEditText;
	/** 点击开始进行搜索 */
	private Button searchButton;
	/**进度条对话框*/
	private ProgressDialog progressDialog;
	/***/
	private Handler handler;
	/* 在Activity首次创建时调用 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* 自定义标题栏 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.zimusearch);
		getWindow().setFeatureInt(Window.FEATURE_NO_TITLE,
				R.layout.search_titlebar);
		/**设置查询监听*/
		Controller.getInstance().addQueryListener(this);
		/* 初始化UI对象 */
		areaSpinner = (Spinner) findViewById(R.id.search_zone_spinner);
		searchEditText = (EditText) findViewById(R.id.search_edit);
		backButton = (Button) findViewById(R.id.back_btn);
		backButton.setOnClickListener(this);
		searchButton = (Button) findViewById(R.id.search_button);
		searchButton.setOnClickListener(this);
		
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what)
				{
				case 0:
					new AlertDialog.Builder(KeywordsSearchActivity.this).setTitle("验证超时")
					.setMessage("验证超时，请检查网络连接").setPositiveButton("确定", null).show();
					break;
				case 1:
					new AlertDialog.Builder(KeywordsSearchActivity.this).setTitle("数据接受超时")
					.setMessage("从服务器接受数据超时，请检查网络连接").setPositiveButton("确定", null).show();
					break;
				case 2:
					/*MapNavigateContants.QUERYRESULT_SHOW=true;
					MapNavigateContants.QUERYRESULT=new TwoDCoordinate(1.8,8.8);
					KeywordsSearchActivity.this.finish();*/
					if(progressDialog.isShowing())progressDialog.dismiss();
					
					MapNavigateContants.QUERYRESULT_SHOW=true;
					new AlertDialog.Builder(KeywordsSearchActivity.this).setItems(MapNavigateContants.QUERY_NAME,new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							MapNavigateContants.QUERYRESULT=MapNavigateContants.QUERY_COORDINATES[arg1];
							KeywordsSearchActivity.this.finish();
						}
					}).show();
					break;
				}
				super.handleMessage(msg);
			}
		};
	}

	/* 在Activity创建后或重启后调用 */
	public void onStart() {
		super.onStart();
	}

	/* 在Activity暂停时调用 */
	protected void onPause() {
		super.onPause();
	}

	/* onStart后会调用 */
	protected void onResume() {
		super.onResume();
	}

	/* 在Activity 销毁时调用 */
	protected void onDestroy() {
		super.onDestroy();
	}
    
	/*在按返回键时调用*/
	@Override
	public void onBackPressed() {
		/*重新创建搜索界面*/
        Intent intent = new Intent(KeywordsSearchActivity.this, SearchActivity.class);
        startActivity(intent);
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 点击查询按钮
		if (v.getId() == searchButton.getId()) {
			/*Intent intent = new Intent(KeywordsSearchActivity.this,
					SearchResultActivity.class);
			startActivity(intent);*/
			MapNavigateContants.QUERY_TYPE="Query_Query";
			try {
				Controller.getInstance().name_Query(searchEditText.getText().toString());
				progressDialog=ProgressDialog.show(KeywordsSearchActivity.this, "请等待...", "正在查询中，请等待...");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				handler.sendEmptyMessage(0);
			}
			
			/*new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						if(progressDialog.isShowing())
							{
								progressDialog.dismiss();
								handler.sendEmptyMessage(1);
							}
					}
					super.run();
				}
				
			}.start();*/
		}
		// 标题栏上的返回按钮
		else if (v.getId() == backButton.getId()) {
			KeywordsSearchActivity.this.finish();
		}
	}

	@Override
	public void query_point_name(Query_Result result) {
		// TODO Auto-generated method stub
		if(result!=null&&result.query_type.equals("name")&&MapNavigateContants.QUERY_TYPE.equals("Query_Query"))
		{
			int num=result.point_Num;
			MapNavigateContants.QUERY_NAME=new String[num];
			MapNavigateContants.QUERY_COORDINATES=new TwoDCoordinate[num];
			/**对常量进行初始化*/
			MapNavigateContants.QUERY_NAME=result.Point_Name;
			MapNavigateContants.QUERY_COORDINATES=result.coordinate;
			
			handler.sendEmptyMessage(2);
		}
	}
}
