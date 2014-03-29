package com.ubirtls.view.Activity;

import java.io.IOException;

import ubimessage.MessageITException;

import com.ubirtls.Controller;
import com.ubirtls.NavigateRequestListener;
import com.ubirtls.QueryListener;
import com.ubirtls.R;
import com.ubirtls.config.MapNavigateContants;
import com.ubirtls.event.NavigateResult;
import com.ubirtls.event.Query_Result;

import coordinate.TwoDCoordinate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * ��¼��ͼ��������ʼ������ֹ��
 * @author zhangx
 *
 */
public class mapnavigate extends Activity implements OnClickListener,QueryListener,NavigateRequestListener{
	/**�����е����ֵ*/
	private EditText start_point;
	/**�����е��յ�ֵ*/
	private EditText end_point;
	/**����������������յ㽻��*/
	private ImageButton exchange;
	/**���ݵ����ڵ�����ƽ��е���*/
	private Button navigate;
	/**�˳���������*/
	private Button exit;
	/**����������*/
	private int flag;
	private boolean start_end;
	/**�������ֶ�����Ի����еĲ�ѯ���*/
	private ProgressDialog progress_dialog;
	/**��ѯ��������ı�ʶ*/
	private boolean end_flag=false;
	private boolean dialog_flag=false;
	/**�¼�̎����*/
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
/*		getWindow().setFeatureInt(Window.FEATURE_NO_TITLE, arg1)
*/		this.setContentView(R.layout.navigate);
		//���ò�ѯ������
		Controller.getInstance().addQueryListener(this);
		start_point=(EditText)this.findViewById(R.id.start_point);
		start_point.setOnClickListener(this);
		start_point.setInputType(InputType.TYPE_NULL);
		end_point=(EditText)this.findViewById(R.id.end_point);
		end_point.setOnClickListener(this);
		end_point.setInputType(InputType.TYPE_NULL);
		exchange=(ImageButton)this.findViewById(R.id.exchange);
		exchange.setOnClickListener(this);
		navigate=(Button)this.findViewById(R.id.navigate_navigate);
		navigate.setOnClickListener(this);
		exit=(Button)this.findViewById(R.id.navigate_back);
		exit.setOnClickListener(this);
		
		Bundle bundle=getIntent().getExtras();
		flag=bundle.getInt("flag");
		//���ݵ����ı�ʶ��ȷ������һ�����͵ĵ���
		switch (flag)
		{
			case 1:
				String point=bundle.getString("point");
				String point_name=bundle.getString("name");
				
				if(point.equals("end"))
				{
					start_point.setText(MapNavigateContants.MY_LOCATION);
					end_point.setText(point_name);
				}
				else
				{
					start_point.setText(point_name);
					end_point.setText(MapNavigateContants.MY_LOCATION);
				}
				break;
			case 2:
				String start=bundle.getString("start");
				String end=bundle.getString("end");
				start_point.setText(start);
				end_point.setText(end);
				break;
			case 3:
				String location_name=bundle.getString("name");
				start_point.setText("�ҵ�λ��");
				end_point.setText(location_name);
				break;
		}	
		/*���������*/
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				//�������Ӵ���
				case 0:
					new AlertDialog.Builder(mapnavigate.this).setTitle("��֤��ʱ")
					.setMessage("������֤��ʱ�������������ӣ�").setPositiveButton("ȷ��", null).show();
					break;
				//���ݻ�ȡ��ʱ
				case 1:
					if(progress_dialog.isShowing())progress_dialog.dismiss();
					
					new AlertDialog.Builder(mapnavigate.this).setTitle("�������ݳ�ʱ")
					.setMessage("���ݽ��ܳ�ʱ�������������ӣ�").setPositiveButton("ȷ��", null).show();
					break;
				//��ѯ�������Ӧ����
				case 2:
					if(progress_dialog.isShowing())progress_dialog.dismiss();
					
					new AlertDialog.Builder(mapnavigate.this).setItems(MapNavigateContants.QUERY_NAME, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if(start_end)
								start_point.setText(MapNavigateContants.QUERY_NAME[which]);
							else
								end_point.setText(MapNavigateContants.QUERY_NAME[which]);
						}
					}).show();
					break;
				case 3:
					if(progress_dialog.isShowing())progress_dialog.dismiss();
					
					mapnavigate.this.finish();
					break;
				}
				super.handleMessage(msg);
			}
		};
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
			start_end=true;
			switch(v.getId())
			{
				case R.id.end_point:
					start_end=false;
				case R.id.start_point:
					String[] start_choose={"��ͼ�ϵĵ�","�ҵ��ղ�","�ֶ�����"};
					new AlertDialog.Builder(mapnavigate.this)
					.setTitle("�������")
					.setItems(start_choose, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							//��ͼѡ��
							if(which==0)
							{
								/**����������ı����н��е�ͼѡ�����ʱ*/
								if(start_end)
								{
									MapNavigateContants.MAP_GETSTART_POINT=true;
									MapNavigateContants.ENDPOINT_NAME=end_point.getText().toString();
									mapnavigate.this.finish();
								}
								else
								{
									MapNavigateContants.MAP_GETEND_POINT=true;
									MapNavigateContants.STARTPOINT_NAME=start_point.getText().toString();
									mapnavigate.this.finish();
								}
							}
							//���ղؼ���ȡ��
							else if(which==1)
								new AlertDialog.Builder(mapnavigate.this)
								.setItems(R.array.location_name,new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										String[] location_name=getResources().getStringArray(R.array.location_name);
										if(start_end)
											start_point.setText(location_name[which]);
										else
											end_point.setText(location_name[which]);
									}
								}).show();
							else
							{
								MapNavigateContants.QUERY_TYPE="Navigate_Query";
								LayoutInflater factory=LayoutInflater.from(getApplication());
								final View dialogView=factory.inflate(R.layout.navigate_pointname,null);
								new AlertDialog.Builder(mapnavigate.this).setTitle("������ڵ�����")
								.setView(dialogView)
								.setPositiveButton("��ѯ", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
											EditText point_name=(EditText)dialogView.findViewById(R.id.point_name);
											try {
												Controller.getInstance().name_Query(point_name.getText().toString());
												progress_dialog=ProgressDialog.show(mapnavigate.this, "��ȴ�...", "���ڻ�ȡ��ѯ�������ȴ�...");
											} catch (Exception e) {
												// TODO Auto-generated catch block
												handler.sendEmptyMessage(0);											
											}
											new Thread(){
												@Override
												public void run() {
													// TODO Auto-generated method stub
													try {
														sleep(5000);
													} catch (InterruptedException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}finally{
														if(progress_dialog.isShowing())progress_dialog.dismiss();
														handler.sendEmptyMessage(1);
													}
													super.run();
												}}.start();
									}
								}).show();
							}
						}
					})
					.setNegativeButton("ȡ��", null).show();
					break;
				case R.id.exchange:
					String temp=start_point.getText().toString();
					start_point.setText(end_point.getText().toString());
					end_point.setText(temp);
					break;
				case R.id.navigate_navigate:
						Controller.getInstance().addNavigateRequestListener(this);
						try
						{
							Controller.getInstance().navigate(start_point.getText().toString(),end_point.getText().toString());
							progress_dialog=ProgressDialog.show(mapnavigate.this, "��ȴ�...", "���ڻ�ȡ����·��......");
						}
						catch(Exception e)
						{
							handler.sendEmptyMessage(0);
						}
						new Thread(){
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									sleep(5000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}finally{
									if(progress_dialog.isShowing())
									{
										progress_dialog.dismiss();
									}
								}
								super.run();
							}
					}.start();
					
					break;
				case R.id.navigate_back:
					mapnavigate.this.finish();
					break;
			}			
	}
	/**
	 * ��ѯ�����������
	 */
	@Override
	public void query_point_name(Query_Result result) {
		// TODO Auto-generated method stub
		if(result!=null&&result.query_type.equals("name")&&MapNavigateContants.QUERY_TYPE.equals("Navigate_Query"))
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
	@Override
	public void NavigateRoadRequest(NavigateResult result) {
		// TODO Auto-generated method stub
		if(result!=null)
			handler.sendEmptyMessage(3);
	}
}
