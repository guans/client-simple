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
 *�ؼ����������棬��������Ĺؼ��ֲ�ѯ��Ӧ����ĵص�
 * 
 * @author �����
 * @version 1.0
 */
public class KeywordsSearchActivity extends Activity implements OnClickListener,QueryListener {

	/** �ؼ����������棬��������Ĺؼ��ֲ�ѯ��Ӧ����ĵص� */
	private Spinner areaSpinner;
	/** ���ذ�ť��������һ������ */
	private Button backButton;
	/** �༭������ؼ��ֽ������� */
	private EditText searchEditText;
	/** �����ʼ�������� */
	private Button searchButton;
	/**�������Ի���*/
	private ProgressDialog progressDialog;
	/***/
	private Handler handler;
	/* ��Activity�״δ���ʱ���� */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* �Զ�������� */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.zimusearch);
		getWindow().setFeatureInt(Window.FEATURE_NO_TITLE,
				R.layout.search_titlebar);
		/**���ò�ѯ����*/
		Controller.getInstance().addQueryListener(this);
		/* ��ʼ��UI���� */
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
					new AlertDialog.Builder(KeywordsSearchActivity.this).setTitle("��֤��ʱ")
					.setMessage("��֤��ʱ��������������").setPositiveButton("ȷ��", null).show();
					break;
				case 1:
					new AlertDialog.Builder(KeywordsSearchActivity.this).setTitle("���ݽ��ܳ�ʱ")
					.setMessage("�ӷ������������ݳ�ʱ��������������").setPositiveButton("ȷ��", null).show();
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
	}
    
	/*�ڰ����ؼ�ʱ����*/
	@Override
	public void onBackPressed() {
		/*���´�����������*/
        Intent intent = new Intent(KeywordsSearchActivity.this, SearchActivity.class);
        startActivity(intent);
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// �����ѯ��ť
		if (v.getId() == searchButton.getId()) {
			/*Intent intent = new Intent(KeywordsSearchActivity.this,
					SearchResultActivity.class);
			startActivity(intent);*/
			MapNavigateContants.QUERY_TYPE="Query_Query";
			try {
				Controller.getInstance().name_Query(searchEditText.getText().toString());
				progressDialog=ProgressDialog.show(KeywordsSearchActivity.this, "��ȴ�...", "���ڲ�ѯ�У���ȴ�...");
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
		// �������ϵķ��ذ�ť
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
			/**�Գ������г�ʼ��*/
			MapNavigateContants.QUERY_NAME=result.Point_Name;
			MapNavigateContants.QUERY_COORDINATES=result.coordinate;
			
			handler.sendEmptyMessage(2);
		}
	}
}
