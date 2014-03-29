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
 * 记录地图导航的起始点与终止点
 * @author zhangx
 *
 */
public class mapnavigate extends Activity implements OnClickListener,QueryListener,NavigateRequestListener{
	/**导航中的起点值*/
	private EditText start_point;
	/**导航中的终点值*/
	private EditText end_point;
	/**导航界面中起点与终点交换*/
	private ImageButton exchange;
	/**根据导航节点的名称进行导航*/
	private Button navigate;
	/**退出导航窗口*/
	private Button exit;
	/**导航的类型*/
	private int flag;
	private boolean start_end;
	/**导航中手动输入对话框中的查询结果*/
	private ProgressDialog progress_dialog;
	/**查询结果结束的标识*/
	private boolean end_flag=false;
	private boolean dialog_flag=false;
	/**事件處理句柄*/
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
/*		getWindow().setFeatureInt(Window.FEATURE_NO_TITLE, arg1)
*/		this.setContentView(R.layout.navigate);
		//设置查询监听者
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
		//根据导航的标识来确定是哪一种类型的导航
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
				start_point.setText("我的位置");
				end_point.setText(location_name);
				break;
		}	
		/*句柄处理函数*/
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				//网络连接错误
				case 0:
					new AlertDialog.Builder(mapnavigate.this).setTitle("验证超时")
					.setMessage("数据验证超时，请检查网络连接！").setPositiveButton("确定", null).show();
					break;
				//数据获取超时
				case 1:
					if(progress_dialog.isShowing())progress_dialog.dismiss();
					
					new AlertDialog.Builder(mapnavigate.this).setTitle("接受数据超时")
					.setMessage("数据接受超时，请检查网络连接！").setPositiveButton("确定", null).show();
					break;
				//查询结果的响应处理
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
					String[] start_choose={"地图上的点","我的收藏","手动输入"};
					new AlertDialog.Builder(mapnavigate.this)
					.setTitle("设置起点")
					.setItems(start_choose, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							//地图选点
							if(which==0)
							{
								/**当导航起点文本框中进行地图选点操作时*/
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
							//从收藏夹中取点
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
								new AlertDialog.Builder(mapnavigate.this).setTitle("请输入节点名称")
								.setView(dialogView)
								.setPositiveButton("查询", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
											EditText point_name=(EditText)dialogView.findViewById(R.id.point_name);
											try {
												Controller.getInstance().name_Query(point_name.getText().toString());
												progress_dialog=ProgressDialog.show(mapnavigate.this, "请等待...", "正在获取查询结果，请等待...");
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
					.setNegativeButton("取消", null).show();
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
							progress_dialog=ProgressDialog.show(mapnavigate.this, "请等待...", "正在获取导航路线......");
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
	 * 查询导航点的名称
	 */
	@Override
	public void query_point_name(Query_Result result) {
		// TODO Auto-generated method stub
		if(result!=null&&result.query_type.equals("name")&&MapNavigateContants.QUERY_TYPE.equals("Navigate_Query"))
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
	@Override
	public void NavigateRoadRequest(NavigateResult result) {
		// TODO Auto-generated method stub
		if(result!=null)
			handler.sendEmptyMessage(3);
	}
}
