package com.ubirtls.view.Activity;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

import ubimessage.MessageITException;

import com.ubirtls.Controller;
import com.ubirtls.GetDoctorListener;
import com.ubirtls.NavigateRequestListener;
import com.ubirtls.Observer;
import com.ubirtls.QueryListener;
import com.ubirtls.R;
import com.ubirtls.UradioLocationListener;
import com.ubirtls.PDR.PDRService;
import com.ubirtls.config.MapNavigateContants;
import com.ubirtls.config.Setting;
import com.ubirtls.event.DoctorResult;
import com.ubirtls.event.Query_Result;
import com.ubirtls.view.map.ItemOverlay;
import com.ubirtls.view.map.MapController;
import com.ubirtls.view.map.MapView;
import com.ubirtls.view.map.MarkOverlay;
import com.ubirtls.view.map.MyLocationOverlay;
import com.ubirtls.view.map.PathOverlay;
import com.ubirtls.view.map.ScaleBarOverlay;

import coordinate.TwoDCoordinate;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;

/**
 * 地图显示界面，也是主界面，在此界面上可以实现人员位置 的实时显示，导航路径的显示以及其他地图的操作，包括放大，缩小，移动等。
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class MapActivity extends Activity implements MapActivityConstants,OnClickListener,
			GetDoctorListener,QueryListener,UradioLocationListener{


	/** 用于地图显示 */
	private MapView mapView;

	/** 位置覆盖层 用于显示用户的位置 */
	private MyLocationOverlay locationOverlay = null;

	/** 路径层 用于显示用户的轨迹 */
	private PathOverlay trackOverlay = null;
	/**导航路径显示*/
	private PathOverlay navigateOverlay=null;
	/**比例尺*/
	private ScaleBarOverlay scaleBar = null;

	/**item标示*/
	private ItemOverlay itemOverlay = null;

	/**preference 保存数据*/
	private SharedPreferences prefs;

	/**紧急呼叫拨打的电话号码*///需要通过通信获取对应的手机号码
	private String phonenum="15527960872";
	/**添加标识图标*/
	private MarkOverlay markOverlay;
	/***/
	private TwoDCoordinate coor;
	/**导航时Activity的跳转*/
	private Intent intent;
	private Bundle bundle=new Bundle();
	/**为地图导航栏上的图标按钮设置id值*/
	private final int login_id=001;
	private final int location_id=002;
	private final int Emergency_id=003;
	private final int doctor_id=004;
	private final int sos_id=005;
	/**定位图标*/
	private ImageButton get_location;
	/**事件处理句柄*/
	private Handler handler;
	private ProgressDialog progressdialog;
	/**接收服务器消息时加载的进度条*/
	private AlertDialog builder;
	/**在获取屏幕对应点名称时判断响应时间是否超时*/
	private boolean message_flag;
	/**屏幕点与名称对应时判断应该显示哪一个对话框*/
	private boolean dialog_flag;
	/**响应时间超时的错误消息提示对话框*/
	private AlertDialog error_diadialog;
	/**导航查询时输入对话框中的起点、终点编辑框*/
	private EditText start_point,end_point;
	/***/
	private boolean ISCOORDINATESEARCH=false;
	
	/**广播接收者，接收PDR服务返回的航位推算结果*/
    private PdrServiceReceiver pdrReceiver;
    boolean started = false;
	// 在Activity首次创建时调用
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final RelativeLayout rl = new RelativeLayout(this);
		Controller.getInstance().addQueryListener(this);
		Controller.getInstance().changeMap("hospital_2D_floor1");//"office-5000");
		/* mapview和 locationOverlay以及pathOverlay初始化 */
		mapView = new MapView(this, null, null);
		
		intent=new Intent(MapActivity.this,mapnavigate.class);
		prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		/*用户行走轨迹的跟踪*/
		{
			trackOverlay = new PathOverlay(this);
			navigateOverlay = new PathOverlay(this);
			/*对坐标变化进行监听,当有新的位置结果返回时，trackOverlay（轨迹）添加新的轨迹点*/
			Controller.getInstance().addLocationChangeListerner(trackOverlay);
			Controller.getInstance().addNavigateRequestListener(navigateOverlay);
			/*对轨迹变化进行监听，如收到清除轨迹所有数据请求时，则清除轨迹信息*/
			Controller.getInstance().setTrackListener(trackOverlay);
			trackOverlay.addPoint(Setting.MYLOCATIONX,Setting.MYLOCATIONY);
		}
		itemOverlay = new ItemOverlay(this, null);
		/*定位标识*/
		{
			markOverlay=new MarkOverlay(this);
			Controller.getInstance().addLocationChangeListerner(markOverlay);
		}
		/*用户位置图层*/
		{
			locationOverlay = new MyLocationOverlay(this);
			/*设置MapView的locationOverlay的观察者,当有新的位置产生时，通知mapView进行位置的重绘*/
			locationOverlay.setObserver(mapView);
			locationOverlay.setMyLocation(Setting.MYLOCATIONX,Setting.MYLOCATIONY);
		}
		/*比例尺图层*/
		scaleBar = new ScaleBarOverlay(this);
		/*mapview*/
		{
			mapView.getOverlayManager().add(locationOverlay);
			mapView.getOverlayManager().add(itemOverlay);
			mapView.getOverlayManager().add(trackOverlay);
			mapView.getOverlayManager().add(markOverlay);
			mapView.getOverlayManager().add(navigateOverlay);
			
			//mapView.getOverlayManager().add(scaleBar);
			/*使用内部自带的缩放控件*/		
			mapView.setBuiltInZoomControls(true);
			/* 获取mapview上次地图的显示位置以及缩放级别 */
			String region = prefs.getString(this.getResources().getString(R.string.setting_change_map_key), null);
			Controller.getInstance().changeMap(region);
			mapView.getController().setZoom(prefs.getInt(PREFS_ZOOM_LEVEL, 0));
			mapView.scrollTo(prefs.getInt(PREFS_SCROLL_X, 0), prefs.getInt(PREFS_SCROLL_Y, 0));
		}
		
		/* 添加mapView视图 */
		rl.addView(mapView, new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		/*在地图的顶端添加导航文本框*/
		{
			final EditText map_navigate_text=new EditText(this);
			RelativeLayout.LayoutParams mapnav_params=new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			mapnav_params.height=70;mapnav_params.width=187;
			mapnav_params.leftMargin=0;mapnav_params.topMargin=0;
			map_navigate_text.setHint("地图导航");
			map_navigate_text.setInputType(InputType.TYPE_NULL);
			
			rl.addView(map_navigate_text,mapnav_params);
			/**地图导航文本框的事件监听处理*/
			map_navigate_text.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
			 		LayoutInflater factory=LayoutInflater.from(getApplication());
					final View dialogView=factory.inflate(R.layout.navigate_name, null);
					final View end_pointView=factory.inflate(R.layout.navigate_pointname, null);
					
					MapNavigateContants.QUERY_TYPE="MapQuery";
					new AlertDialog.Builder(MapActivity.this).setTitle(R.string.navigate_choose)
					.setIcon(R.drawable.inputinfo)
					.setMessage("是否以自身位置作为定位起点？")
					.setPositiveButton("是", new DialogInterface.OnClickListener() {
						/**仅仅需要输入导航的终点，导航的起点默认为定位点*/
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							new AlertDialog.Builder(MapActivity.this).setTitle(R.string.navigate_endpnt)
							.setIcon(R.drawable.inputinfo)
							.setView(end_pointView)
							.setPositiveButton("查询", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									EditText point_name=(EditText)end_pointView.findViewById(R.id.point_name);
									try {
										Controller.getInstance().name_Query(point_name.getText().toString());
										progressdialog=ProgressDialog.show(MapActivity.this, "请等待...", "正在进行名称查询...");
									} catch (Exception e) {
										// TODO Auto-generated catch block
										handler.sendEmptyMessage(0);
									}
									MapNavigateContants.NAVIGATE_TYPE=1;
									/*获取数据超时的处理*/
									progressDealing();
								}
							}).show();
						}
					})
					/**用户输入导航的起始点和终止点进行导航*/
					.setNegativeButton("否", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							new AlertDialog.Builder(MapActivity.this).setTitle(R.string.navigate)
							.setIcon(R.drawable.inputinfo)
							.setView(dialogView)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									if(start_point.getText().toString().equals("")||end_point.getText().toString().equals(""))
									{
										new AlertDialog.Builder(MapActivity.this).setTitle("注意")
										.setIcon(R.drawable.exit)
										.setMessage("信息输入有空值").show();
									}
									else
									{
										bundle.putInt("flag", 2);
										bundle.putString("start", start_point.getText().toString());
										bundle.putString("end", end_point.getText().toString());
										intent.putExtras(bundle);
										startActivity(intent);
									}
								}
							}).show();
					start_point=(EditText)dialogView.findViewById(R.id.start_point_text);
					end_point=(EditText)dialogView.findViewById(R.id.end_point_text);
					Button search_startPoint=(Button)dialogView.findViewById(R.id.search_startPoint);
					Button search_endPoint=(Button)dialogView.findViewById(R.id.search_endPoint);
					
					search_startPoint.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							try {
								Controller.getInstance().name_Query(start_point.getText().toString());
								progressdialog=ProgressDialog.show(MapActivity.this, "请等待...", "正在进行名称查询，请等待...");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								handler.sendEmptyMessage(0);
							}
							MapNavigateContants.NAVIGATE_TYPE=2;
							progressDealing();
						}
					});
					
					search_endPoint.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							try {
								Controller.getInstance().name_Query(end_point.getText().toString());
								progressdialog=ProgressDialog.show(MapActivity.this, "请等待...", "正在进行名称查询，请等待...");							
							} catch (Exception e) {
								// TODO Auto-generated catch block
								handler.sendEmptyMessage(0);
							}
							MapNavigateContants.NAVIGATE_TYPE=3;
							progressDealing();
						}
					});
						}
					}).show();
					
				}
			});
		}
		/*添加登录、定位、紧急呼叫等图标按钮，进行登录处理*/
		{	  
			final ImageButton login_btn=new ImageButton(this);		login_btn.setId(login_id);
			final ImageButton Em_Call=new ImageButton(this);		Em_Call.setId(sos_id);
			get_location=new ImageButton(this);	get_location.setId(location_id);
			final ImageButton doctor_info=new ImageButton(this);	doctor_info.setId(doctor_id);
			final ImageButton emergency=new ImageButton(this);		emergency.setId(Emergency_id);
			/**为图标添加对应的图片*/
			Em_Call.setImageResource(R.drawable.sos);
			login_btn.setImageResource(R.drawable.user_login);
			get_location.setImageResource(R.drawable.location);
			doctor_info.setImageResource(R.drawable.helper);
			emergency.setImageResource(R.drawable.emergency);
			
			final RelativeLayout.LayoutParams loginParams=new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			final RelativeLayout.LayoutParams EmParams=new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			final RelativeLayout.LayoutParams locParams=new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			final RelativeLayout.LayoutParams doctorParams=new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			final RelativeLayout.LayoutParams emer_Params=new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			/*将登录图标置于地图的顶端*/
			loginParams.width=68;loginParams.height=70;
			login_btn.setScaleType(ScaleType.FIT_XY);
			loginParams.leftMargin=180;
			rl.addView(login_btn,loginParams);
			login_btn.setOnClickListener(this);
			
			/*将定位图标放在顶端*/
			locParams.width=68;locParams.height=70;locParams.leftMargin=238;
			get_location.setScaleType(ScaleType.FIT_XY);
			rl.addView(get_location,locParams);
			get_location.setOnClickListener(this);
			
			/**将紧急处理请求图标置于导航栏的顶端*/
			emer_Params.width=68;emer_Params.height=70;emer_Params.leftMargin=296;
			emergency.setScaleType(ScaleType.FIT_XY);
			rl.addView(emergency,emer_Params);
			emergency.setOnClickListener(this);
			
			/*将医生图标放在导航栏的顶端*/
			doctor_info.setScaleType(ScaleType.FIT_XY);
			doctorParams.width=70;doctorParams.height=70;doctorParams.leftMargin=354;
			rl.addView(doctor_info,doctorParams);
			doctor_info.setOnClickListener(this);
			/*添加医生信息查询*/
			Controller.getInstance().addGetDoctorInfoListener(this);
			
			/*将紧急呼叫图标放在登录图标的右侧*/
			Em_Call.setScaleType(ScaleType.FIT_XY);
			EmParams.width=70;EmParams.height=70;EmParams.leftMargin=414;
			rl.addView(Em_Call,EmParams);
			Em_Call.setOnClickListener(this);
		}
		
		/* 回车位控件，跟踪用户的位置 */
		{
			final ImageView locationImg = new ImageView(this);
			locationImg.setImageResource(R.drawable.previous);
			/* 布局在parent的右侧中心 */
			final RelativeLayout.LayoutParams LocationParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			LocationParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			LocationParams.addRule(RelativeLayout.CENTER_VERTICAL);
			/* 按钮监听 */
			locationImg.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					locationOverlay.enableFollow();
					return true;
				}
			});
			rl.addView(locationImg, LocationParams);
		}


		this.setContentView(rl);
		handler=new Handler(){
			public void handleMessage(Message msg) {
				/* 提示用户登录信息 */
				switch (msg.what) {
				/* 登录延时 */
				case 0:
					Dialog alertDialog = new AlertDialog.Builder(
							MapActivity.this)
							.setTitle(
									getResources().getString(
											R.string.login_timeout_alter_title))
							.setMessage(
									getResources()
											.getString(
													R.string.login_timeout_alter_message))
							.setPositiveButton(
									getResources().getString(R.string.OK), null)
							.create();
					alertDialog.show();
					break;
				/**从服务器获取数据超时*/
				case 1:
					if(ISCOORDINATESEARCH==false)
					{
						new AlertDialog.Builder(MapActivity.this).setTitle("验证超时")
						.setMessage("从服务器获取数据超时，请检查网络连接").setPositiveButton("确定", null).show();
					}
					break;
				/**地图取点的响应*/
				case 2:
					if(progressdialog.isShowing())progressdialog.dismiss();
					/*判断为地图取点的查询*/
					ISCOORDINATESEARCH=true;
					/**设置服务器响应消息失败对话框*/
					if(MapNavigateContants.MAP_GETSTART_POINT||MapNavigateContants.MAP_GETEND_POINT)
					{
						builder=new AlertDialog.Builder(MapActivity.this).setTitle("地图取点")
						.setMessage(MapNavigateContants.POINT_NAME)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								bundle.putInt("flag", 2);
								if(MapNavigateContants.MAP_GETSTART_POINT)
								{
									bundle.putString("start", MapNavigateContants.POINT_NAME);
									bundle.putString("end", MapNavigateContants.ENDPOINT_NAME);
									intent.putExtras(bundle);
									MapNavigateContants.MAP_GETSTART_POINT=false;
									startActivity(intent);
								}
								else
								{
									bundle.putString("start", MapNavigateContants.STARTPOINT_NAME);
									bundle.putString("end", MapNavigateContants.POINT_NAME);
									intent.putExtras(bundle);
									MapNavigateContants.MAP_GETEND_POINT=false;
									startActivity(intent);
								}
							}
						}).show();
					}
					else
					{
						/*通过对话框来判定取到的点是作为起点还是终点*/
						bundle.putInt("flag", 1);
						builder=new AlertDialog.Builder(MapActivity.this).setTitle("地图取点")
						.setMessage(MapNavigateContants.POINT_NAME)
						.setPositiveButton("设为起点", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								//将获取的坐标点作为起点
								bundle.putString("point","start");
								bundle.putString("name", MapNavigateContants.POINT_NAME);
								intent.putExtras(bundle);
								startActivity(intent);
							}
						})
						.setNegativeButton("设为终点", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								//将获取到的点作为终点
								bundle.putString("point","end");
								bundle.putString("name", MapNavigateContants.POINT_NAME);
								intent.putExtras(bundle);
								startActivity(intent);
							}
						}).show();
					}
					break;
				//查询结果的响应与处理
				case 3:
					if(progressdialog.isShowing())progressdialog.dismiss();
					
					new AlertDialog.Builder(MapActivity.this).setItems(MapNavigateContants.QUERY_NAME, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							switch (MapNavigateContants.NAVIGATE_TYPE)
							{
								case 1:
									Intent intent=new Intent(MapActivity.this,mapnavigate.class);
									Bundle bundle=new Bundle();
									bundle.putInt("flag", 3);
									bundle.putString("name", MapNavigateContants.QUERY_NAME[which]);
									intent.putExtras(bundle);
									startActivity(intent);
									MapNavigateContants.QUERY_NAME=null;
									break;
								case 2:
									start_point.setText(MapNavigateContants.QUERY_NAME[which]);
									MapNavigateContants.QUERY_NAME=null;
									break;
								case 3:
									end_point.setText(MapNavigateContants.QUERY_NAME[which]);
									MapNavigateContants.QUERY_NAME=null;
									break;
							}
							MapNavigateContants.NAVIGATE_TYPE=0;
						}
					}).show();
					break;
				//获取优频定位初始值的响应处理
				case 4:
					//if(progressdialog.isShowing())progressdialog.dismiss();
					MapNavigateContants.URADIOLOCATION=new TwoDCoordinate(0.8,8.8);
					
					Intent intent = new Intent(MapActivity.this, PDRService.class);
					startService(intent); 	
					get_location.setImageResource(R.drawable.location_stop);
					Toast.makeText(MapActivity.this, "定位服务已启动", Toast.LENGTH_SHORT).show();
					started = true;
					
					locationOverlay.setMyLocation(MapNavigateContants.URADIOLOCATION);
					trackOverlay.addPoint(MapNavigateContants.URADIOLOCATION);
					break;
				}
				}
		};
		try{
			IntentFilter filter;
			filter = new IntentFilter(PDRService.HIPPO_SERVICE_IDENTIFIER);
			this.pdrReceiver = new PdrServiceReceiver();
			registerReceiver(pdrReceiver,filter);
		}catch(Exception e){
			e.getStackTrace();
		}
	}

	/**
	 * PDR服务广播接收者，接收航位推算结果
	 * @author 胡旭科
	 *
	 */
		public class PdrServiceReceiver extends BroadcastReceiver{

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				Bundle bundle = intent.getExtras();
				Setting.MYLOCATIONX = bundle.getDouble("positionX");
				Setting.MYLOCATIONY= bundle.getDouble("positionY");
				Log.i("getLoccation", "true");
				Controller.getInstance().notifyLocation(Setting.MYLOCATIONX, Setting.MYLOCATIONY);
				
				try {
					Controller.getInstance().sendLocationDataToServer(Setting.MYLOCATIONX, Setting.MYLOCATIONY);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					handler.sendEmptyMessage(0);
				} 
				
	/*			double stepLength = bundle.getDouble("stepLength");
				double stepHeading = bundle.getDouble("stepHeading");
				Controller.getInstance().notifyDisplacement(stepLength,stepHeading);
	*/
			}
			
		}
	// 在Activity创建后或重启后调用
	public void onStart() {
		super.onStart();
		this.setTitle(R.string.map);
	}

	// 在Activity暂停时调用
	protected void onPause() {
		super.onPause();
	}

	// onStart后会调用
	protected void onResume() {
		if(MapNavigateContants.QUERYRESULT_SHOW)
		{
			MapNavigateContants.QUERYRESULT_SHOW=false;
			markOverlay.addpoint(MapNavigateContants.QUERYRESULT);
		}
		super.onResume();
	}

	// 在Activity 销毁时调用 进行一些程序关闭的处理 包括关闭 spotter 连接以及清除一些缓存数据
	protected void onDestroy() {
		super.onDestroy();
		/**解除向系统注册的Receiver*/
		this.unregisterReceiver(pdrReceiver);
		/**关闭PDR服务*/
		Intent intent = new Intent(MapActivity.this, PDRService.class);
		stopService(intent);
		Log.i("服务关闭", "MapActivity");
		
		Controller.getInstance().clearData();
		try {
			Controller.getInstance().logout(Setting.USERNAME);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessageITException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Controller.getInstance().closeCommunication();

		/*保存地图的位置等信息*/
		final SharedPreferences.Editor edit = prefs.edit();
		edit.putInt(PREFS_SCROLL_X, mapView.getScrollX());
		edit.putInt(PREFS_SCROLL_Y, mapView.getScrollY());
		edit.putInt(PREFS_ZOOM_LEVEL, mapView.getZoomLevel());
		edit.commit();
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setTitle(
				R.string.setting_delete_map_data_title).setMessage(
						R.string.exit_message).setNegativeButton(R.string.cancel, null)
						.setPositiveButton(R.string.OK,
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								MapActivity.this.finish();
							}
						}).show();
	}

	// 重载函数，在点击menu按钮时调用，创建菜单
	public boolean onCreateOptionsMenu(final Menu menu) {
		/*
		 * MenuInflater inflater = getMenuInflater(); //
		 * 设置menu界面为res/menu/menu.xml inflater.inflate(R.menu.map_menu, menu);
		 */
		menu.add(0, R.id.search, 0, R.string.search_main).setIcon(R.drawable.search_menu);
		menu.add(0, R.id.setting, 1, R.string.setting).setIcon(R.drawable.setting_menu);
		//menu.add(0, R.id.routemanager, 2, R.string.route_manager).setIcon(R.drawable.menu_bus);

		return true;
	}


	// 重载函数，响应菜单选中请求
	public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
		// 得到当前选中的MenuItem的ID,
		int item_id = item.getItemId();

		switch (item_id) {
		case R.id.search:
			/*
			 * 新建一个Intent对象 指定intent要启动的类 启动一个新的Activity
			 */
			Intent intentToSearch = new Intent();
			intentToSearch.setClass(MapActivity.this, SearchActivity.class);
			startActivity(intentToSearch);
			break;
		case R.id.setting:
			/* 新建一个Intent对象 */
			Intent intentToSetting = new Intent();
			/* 指定intent要启动的类 */
			intentToSetting.setClass(MapActivity.this, SettingActivity.class);
			/* 启动一个新的Activity */
			startActivity(intentToSetting);
			break;
		case R.id.routemanager:
			/* 新建一个Intent对象*/ 
			Intent intentToRoute = new Intent();
			/* 指定intent要启动的类 */
			intentToRoute
			.setClass(MapActivity.this, RouteManagerActivity.class);
			 /*启动一个新的Activity */
			startActivity(intentToRoute);
			break;
		}
		return true;
	}

	
	//实现获取长按手机屏幕的坐标，并将坐标信息发送到导航页面
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(mapView.get_flag()!=-1)
		{
			//获取屏幕的坐标
			TwoDCoordinate coor=mapView.get_location();
			double x=coor.getXCoordinate();
			double y=coor.getYCoordinate();
			Toast.makeText(MapActivity.this, x+","+y, Toast.LENGTH_SHORT).show();
			/**向服务器发送坐标信息*/
			try
			{
				Controller.getInstance().Query_By_Coordinate(x,y);
				progressdialog=ProgressDialog.show(MapActivity.this, "请等待......", "正在获取该点的信息......");
			}
			catch(Exception e)
			{
				/*捕获异常*/
				handler.sendEmptyMessage(0);
			}
			/*超时的响应处理*/
			progressDealing();
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case login_id:
			//进入登录窗口
			startActivity(new Intent(MapActivity.this,LoginActivity.class));					
			break;
		case location_id:
			if(!started){
				/*try{
				//向服务器发送获取自身最新位置的请求
				Controller.getInstance().addUradioLocationListener(this);
				Controller.getInstance().getMyLocation();
				progressdialog=ProgressDialog.show(this, "请等待......", "正在获取自身位置信息......");
				}catch(Exception e){
					handler.sendEmptyMessage(0);
				}
			
				progressDealing();*/
				handler.sendEmptyMessage(4);
			}
			else{
				/**关闭PDR服务*/
				Intent intent = new Intent(MapActivity.this, PDRService.class);
				stopService(intent);
				get_location.setImageResource(R.drawable.location);
				Toast.makeText(MapActivity.this, "定位服务已关闭", Toast.LENGTH_SHORT).show();
				Log.i("服务关闭", "MapActivity");
				started = false;
			}
			break;
		/*紧急导航路线请求*/
		case Emergency_id:
			if(!started)
				Toast.makeText(MapActivity.this, "请开启定位服务", Toast.LENGTH_SHORT).show();
			else{
				if(MapNavigateContants.ISEMERGENCY)
				{
					try {
						Controller.getInstance().Emergency_Request();
						progressdialog=ProgressDialog.show(this, "请等待......", "正在获取路线信息......");
					}catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(0);
					}
					new Thread(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}finally{
								if(progressdialog.isShowing())progressdialog.dismiss();
							}
							super.run();
						}}.start();
				}
				else
					new AlertDialog.Builder(MapActivity.this).setTitle("消息提示").setMessage("当前为非紧急状态，紧急逃生不可用")
					.setPositiveButton("确定", null).show();
			}
			break;
		/*获取对应的医生信息*/
		case doctor_id:
			//通信，向服务器发送获取对应的医生信息的请求	
			if(Setting.LOGIN_STATUS.equals("true")&&MapNavigateContants.ISEMERGENCY)
				if(Setting.DOCTOR_NAME==null){
						
					/**当经过5秒钟后仍然无法得到消息则通信存在问题*/
					try {
					Controller.getInstance().getDoctorInfo("DoctorInformattion");
					progressdialog=ProgressDialog.show(this, "请等待......", "正在获取医生信息......");}
					catch (Exception e) {
						handler.sendEmptyMessage(0);
					}
					progressDealing();
				}
				else{
					new AlertDialog.Builder(MapActivity.this).setTitle("医生的基本信息")
					.setMessage("医生姓名:"+Setting.DOCTOR_NAME+"\n"+"联系方式："+Setting.DOCTOR_PHONE)
					.setPositiveButton("确定", null).show();
				}
			else 
				new AlertDialog.Builder(MapActivity.this).setTitle("提示").setMessage("用户未登录或当前状态为非紧急状态，无法对其进行匹配").setPositiveButton("确定", null).show();
			break;
		case sos_id:
			if(MapNavigateContants.ISEMERGENCY==true)
			{
				Intent intent=new Intent(Intent.ACTION_CALL,
						Uri.parse("tel:"+phonenum));
				startActivity(intent);	
				return;
			}
			else
				Toast.makeText(getApplication(), "当前状态为非紧急情况，紧急呼叫不可用", Toast.LENGTH_SHORT).show();
		}	
	}
	//获取到对应的医生的基本信息
	@Override
	public void GetDoctorInfo(DoctorResult result) {
		// TODO Auto-generated method stub
		if(result!=null)
		{
			Setting.DOCTOR_NAME=result.username;
			Setting.DOCTOR_PHONE=result.phone;
		}
		else 
			handler.sendEmptyMessage(0);
	}

	/*通过坐标查询节点的名称*/
	@Override
	public void query_point_name(Query_Result result) {
		// TODO Auto-generated method stub
		//根据坐标查询得到的结果
		if(result!=null&&result.query_type.equals("point"))
		{
			int num=result.point_Num;
			String[] point_name=new String[num];
			TwoDCoordinate[] coordinates=new TwoDCoordinate[num];
			point_name=result.Point_Name;
			coordinates=result.coordinate;
			
			MapNavigateContants.POINT_NAME=point_name[0];
			MapNavigateContants.coordinate=coordinates[0];		
			
			/*消息的响应处理*/
			handler.sendEmptyMessage(2);
		}
		//根据名称查询得到结果
		else if(result!=null&&result.query_type.equals("name")&&MapNavigateContants.QUERY_TYPE.equals("MapQuery"))
		{
			int num=result.point_Num;
			MapNavigateContants.QUERY_NAME=new String[num];
			MapNavigateContants.QUERY_COORDINATES=new TwoDCoordinate[num];
			/**对常量进行初始化*/
			MapNavigateContants.QUERY_NAME=result.Point_Name;
			MapNavigateContants.QUERY_COORDINATES=result.coordinate;
			
			handler.sendEmptyMessage(3);
		}
	}
	
	@Override
	public void getUradioLocation(TwoDCoordinate coordinate) {
		// TODO Auto-generated method stub
		if(coordinate!=null)
		{
			MapNavigateContants.URADIOLOCATION=coordinate;
			
			handler.sendEmptyMessage(4);
		}
	}
	/**处理从服务器接受数据超时的问题*/
	public void progressDealing()
	{
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
				if(progressdialog.isShowing())
					{
						progressdialog.dismiss();	
						handler.sendEmptyMessage(1);
					}
				}
			}
		}.start();	
	}

}

	