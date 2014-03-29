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
 * ��ͼ��ʾ���棬Ҳ�������棬�ڴ˽����Ͽ���ʵ����Աλ�� ��ʵʱ��ʾ������·������ʾ�Լ�������ͼ�Ĳ����������Ŵ���С���ƶ��ȡ�
 * 
 * @author �����
 * @version 1.0
 */
public class MapActivity extends Activity implements MapActivityConstants,OnClickListener,
			GetDoctorListener,QueryListener,UradioLocationListener{


	/** ���ڵ�ͼ��ʾ */
	private MapView mapView;

	/** λ�ø��ǲ� ������ʾ�û���λ�� */
	private MyLocationOverlay locationOverlay = null;

	/** ·���� ������ʾ�û��Ĺ켣 */
	private PathOverlay trackOverlay = null;
	/**����·����ʾ*/
	private PathOverlay navigateOverlay=null;
	/**������*/
	private ScaleBarOverlay scaleBar = null;

	/**item��ʾ*/
	private ItemOverlay itemOverlay = null;

	/**preference ��������*/
	private SharedPreferences prefs;

	/**�������в���ĵ绰����*///��Ҫͨ��ͨ�Ż�ȡ��Ӧ���ֻ�����
	private String phonenum="15527960872";
	/**��ӱ�ʶͼ��*/
	private MarkOverlay markOverlay;
	/***/
	private TwoDCoordinate coor;
	/**����ʱActivity����ת*/
	private Intent intent;
	private Bundle bundle=new Bundle();
	/**Ϊ��ͼ�������ϵ�ͼ�갴ť����idֵ*/
	private final int login_id=001;
	private final int location_id=002;
	private final int Emergency_id=003;
	private final int doctor_id=004;
	private final int sos_id=005;
	/**��λͼ��*/
	private ImageButton get_location;
	/**�¼�������*/
	private Handler handler;
	private ProgressDialog progressdialog;
	/**���շ�������Ϣʱ���صĽ�����*/
	private AlertDialog builder;
	/**�ڻ�ȡ��Ļ��Ӧ������ʱ�ж���Ӧʱ���Ƿ�ʱ*/
	private boolean message_flag;
	/**��Ļ�������ƶ�Ӧʱ�ж�Ӧ����ʾ��һ���Ի���*/
	private boolean dialog_flag;
	/**��Ӧʱ�䳬ʱ�Ĵ�����Ϣ��ʾ�Ի���*/
	private AlertDialog error_diadialog;
	/**������ѯʱ����Ի����е���㡢�յ�༭��*/
	private EditText start_point,end_point;
	/***/
	private boolean ISCOORDINATESEARCH=false;
	
	/**�㲥�����ߣ�����PDR���񷵻صĺ�λ������*/
    private PdrServiceReceiver pdrReceiver;
    boolean started = false;
	// ��Activity�״δ���ʱ����
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final RelativeLayout rl = new RelativeLayout(this);
		Controller.getInstance().addQueryListener(this);
		Controller.getInstance().changeMap("hospital_2D_floor1");//"office-5000");
		/* mapview�� locationOverlay�Լ�pathOverlay��ʼ�� */
		mapView = new MapView(this, null, null);
		
		intent=new Intent(MapActivity.this,mapnavigate.class);
		prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		/*�û����߹켣�ĸ���*/
		{
			trackOverlay = new PathOverlay(this);
			navigateOverlay = new PathOverlay(this);
			/*������仯���м���,�����µ�λ�ý������ʱ��trackOverlay���켣������µĹ켣��*/
			Controller.getInstance().addLocationChangeListerner(trackOverlay);
			Controller.getInstance().addNavigateRequestListener(navigateOverlay);
			/*�Թ켣�仯���м��������յ�����켣������������ʱ��������켣��Ϣ*/
			Controller.getInstance().setTrackListener(trackOverlay);
			trackOverlay.addPoint(Setting.MYLOCATIONX,Setting.MYLOCATIONY);
		}
		itemOverlay = new ItemOverlay(this, null);
		/*��λ��ʶ*/
		{
			markOverlay=new MarkOverlay(this);
			Controller.getInstance().addLocationChangeListerner(markOverlay);
		}
		/*�û�λ��ͼ��*/
		{
			locationOverlay = new MyLocationOverlay(this);
			/*����MapView��locationOverlay�Ĺ۲���,�����µ�λ�ò���ʱ��֪ͨmapView����λ�õ��ػ�*/
			locationOverlay.setObserver(mapView);
			locationOverlay.setMyLocation(Setting.MYLOCATIONX,Setting.MYLOCATIONY);
		}
		/*������ͼ��*/
		scaleBar = new ScaleBarOverlay(this);
		/*mapview*/
		{
			mapView.getOverlayManager().add(locationOverlay);
			mapView.getOverlayManager().add(itemOverlay);
			mapView.getOverlayManager().add(trackOverlay);
			mapView.getOverlayManager().add(markOverlay);
			mapView.getOverlayManager().add(navigateOverlay);
			
			//mapView.getOverlayManager().add(scaleBar);
			/*ʹ���ڲ��Դ������ſؼ�*/		
			mapView.setBuiltInZoomControls(true);
			/* ��ȡmapview�ϴε�ͼ����ʾλ���Լ����ż��� */
			String region = prefs.getString(this.getResources().getString(R.string.setting_change_map_key), null);
			Controller.getInstance().changeMap(region);
			mapView.getController().setZoom(prefs.getInt(PREFS_ZOOM_LEVEL, 0));
			mapView.scrollTo(prefs.getInt(PREFS_SCROLL_X, 0), prefs.getInt(PREFS_SCROLL_Y, 0));
		}
		
		/* ���mapView��ͼ */
		rl.addView(mapView, new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		/*�ڵ�ͼ�Ķ�����ӵ����ı���*/
		{
			final EditText map_navigate_text=new EditText(this);
			RelativeLayout.LayoutParams mapnav_params=new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			mapnav_params.height=70;mapnav_params.width=187;
			mapnav_params.leftMargin=0;mapnav_params.topMargin=0;
			map_navigate_text.setHint("��ͼ����");
			map_navigate_text.setInputType(InputType.TYPE_NULL);
			
			rl.addView(map_navigate_text,mapnav_params);
			/**��ͼ�����ı�����¼���������*/
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
					.setMessage("�Ƿ�������λ����Ϊ��λ��㣿")
					.setPositiveButton("��", new DialogInterface.OnClickListener() {
						/**������Ҫ���뵼�����յ㣬���������Ĭ��Ϊ��λ��*/
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							new AlertDialog.Builder(MapActivity.this).setTitle(R.string.navigate_endpnt)
							.setIcon(R.drawable.inputinfo)
							.setView(end_pointView)
							.setPositiveButton("��ѯ", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									EditText point_name=(EditText)end_pointView.findViewById(R.id.point_name);
									try {
										Controller.getInstance().name_Query(point_name.getText().toString());
										progressdialog=ProgressDialog.show(MapActivity.this, "��ȴ�...", "���ڽ������Ʋ�ѯ...");
									} catch (Exception e) {
										// TODO Auto-generated catch block
										handler.sendEmptyMessage(0);
									}
									MapNavigateContants.NAVIGATE_TYPE=1;
									/*��ȡ���ݳ�ʱ�Ĵ���*/
									progressDealing();
								}
							}).show();
						}
					})
					/**�û����뵼������ʼ�����ֹ����е���*/
					.setNegativeButton("��", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							new AlertDialog.Builder(MapActivity.this).setTitle(R.string.navigate)
							.setIcon(R.drawable.inputinfo)
							.setView(dialogView)
							.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									if(start_point.getText().toString().equals("")||end_point.getText().toString().equals(""))
									{
										new AlertDialog.Builder(MapActivity.this).setTitle("ע��")
										.setIcon(R.drawable.exit)
										.setMessage("��Ϣ�����п�ֵ").show();
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
								progressdialog=ProgressDialog.show(MapActivity.this, "��ȴ�...", "���ڽ������Ʋ�ѯ����ȴ�...");
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
								progressdialog=ProgressDialog.show(MapActivity.this, "��ȴ�...", "���ڽ������Ʋ�ѯ����ȴ�...");							
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
		/*��ӵ�¼����λ���������е�ͼ�갴ť�����е�¼����*/
		{	  
			final ImageButton login_btn=new ImageButton(this);		login_btn.setId(login_id);
			final ImageButton Em_Call=new ImageButton(this);		Em_Call.setId(sos_id);
			get_location=new ImageButton(this);	get_location.setId(location_id);
			final ImageButton doctor_info=new ImageButton(this);	doctor_info.setId(doctor_id);
			final ImageButton emergency=new ImageButton(this);		emergency.setId(Emergency_id);
			/**Ϊͼ����Ӷ�Ӧ��ͼƬ*/
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
			
			/*����¼ͼ�����ڵ�ͼ�Ķ���*/
			loginParams.width=68;loginParams.height=70;
			login_btn.setScaleType(ScaleType.FIT_XY);
			loginParams.leftMargin=180;
			rl.addView(login_btn,loginParams);
			login_btn.setOnClickListener(this);
			
			/*����λͼ����ڶ���*/
			locParams.width=68;locParams.height=70;locParams.leftMargin=238;
			get_location.setScaleType(ScaleType.FIT_XY);
			rl.addView(get_location,locParams);
			get_location.setOnClickListener(this);
			
			/**��������������ͼ�����ڵ������Ķ���*/
			emer_Params.width=68;emer_Params.height=70;emer_Params.leftMargin=296;
			emergency.setScaleType(ScaleType.FIT_XY);
			rl.addView(emergency,emer_Params);
			emergency.setOnClickListener(this);
			
			/*��ҽ��ͼ����ڵ������Ķ���*/
			doctor_info.setScaleType(ScaleType.FIT_XY);
			doctorParams.width=70;doctorParams.height=70;doctorParams.leftMargin=354;
			rl.addView(doctor_info,doctorParams);
			doctor_info.setOnClickListener(this);
			/*���ҽ����Ϣ��ѯ*/
			Controller.getInstance().addGetDoctorInfoListener(this);
			
			/*����������ͼ����ڵ�¼ͼ����Ҳ�*/
			Em_Call.setScaleType(ScaleType.FIT_XY);
			EmParams.width=70;EmParams.height=70;EmParams.leftMargin=414;
			rl.addView(Em_Call,EmParams);
			Em_Call.setOnClickListener(this);
		}
		
		/* �س�λ�ؼ��������û���λ�� */
		{
			final ImageView locationImg = new ImageView(this);
			locationImg.setImageResource(R.drawable.previous);
			/* ������parent���Ҳ����� */
			final RelativeLayout.LayoutParams LocationParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			LocationParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			LocationParams.addRule(RelativeLayout.CENTER_VERTICAL);
			/* ��ť���� */
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
				/* ��ʾ�û���¼��Ϣ */
				switch (msg.what) {
				/* ��¼��ʱ */
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
				/**�ӷ�������ȡ���ݳ�ʱ*/
				case 1:
					if(ISCOORDINATESEARCH==false)
					{
						new AlertDialog.Builder(MapActivity.this).setTitle("��֤��ʱ")
						.setMessage("�ӷ�������ȡ���ݳ�ʱ��������������").setPositiveButton("ȷ��", null).show();
					}
					break;
				/**��ͼȡ�����Ӧ*/
				case 2:
					if(progressdialog.isShowing())progressdialog.dismiss();
					/*�ж�Ϊ��ͼȡ��Ĳ�ѯ*/
					ISCOORDINATESEARCH=true;
					/**���÷�������Ӧ��Ϣʧ�ܶԻ���*/
					if(MapNavigateContants.MAP_GETSTART_POINT||MapNavigateContants.MAP_GETEND_POINT)
					{
						builder=new AlertDialog.Builder(MapActivity.this).setTitle("��ͼȡ��")
						.setMessage(MapNavigateContants.POINT_NAME)
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							
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
						/*ͨ���Ի������ж�ȡ���ĵ�����Ϊ��㻹���յ�*/
						bundle.putInt("flag", 1);
						builder=new AlertDialog.Builder(MapActivity.this).setTitle("��ͼȡ��")
						.setMessage(MapNavigateContants.POINT_NAME)
						.setPositiveButton("��Ϊ���", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								//����ȡ���������Ϊ���
								bundle.putString("point","start");
								bundle.putString("name", MapNavigateContants.POINT_NAME);
								intent.putExtras(bundle);
								startActivity(intent);
							}
						})
						.setNegativeButton("��Ϊ�յ�", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								//����ȡ���ĵ���Ϊ�յ�
								bundle.putString("point","end");
								bundle.putString("name", MapNavigateContants.POINT_NAME);
								intent.putExtras(bundle);
								startActivity(intent);
							}
						}).show();
					}
					break;
				//��ѯ�������Ӧ�봦��
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
				//��ȡ��Ƶ��λ��ʼֵ����Ӧ����
				case 4:
					//if(progressdialog.isShowing())progressdialog.dismiss();
					MapNavigateContants.URADIOLOCATION=new TwoDCoordinate(0.8,8.8);
					
					Intent intent = new Intent(MapActivity.this, PDRService.class);
					startService(intent); 	
					get_location.setImageResource(R.drawable.location_stop);
					Toast.makeText(MapActivity.this, "��λ����������", Toast.LENGTH_SHORT).show();
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
	 * PDR����㲥�����ߣ����պ�λ������
	 * @author �����
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
	// ��Activity����������������
	public void onStart() {
		super.onStart();
		this.setTitle(R.string.map);
	}

	// ��Activity��ͣʱ����
	protected void onPause() {
		super.onPause();
	}

	// onStart������
	protected void onResume() {
		if(MapNavigateContants.QUERYRESULT_SHOW)
		{
			MapNavigateContants.QUERYRESULT_SHOW=false;
			markOverlay.addpoint(MapNavigateContants.QUERYRESULT);
		}
		super.onResume();
	}

	// ��Activity ����ʱ���� ����һЩ����رյĴ��� �����ر� spotter �����Լ����һЩ��������
	protected void onDestroy() {
		super.onDestroy();
		/**�����ϵͳע���Receiver*/
		this.unregisterReceiver(pdrReceiver);
		/**�ر�PDR����*/
		Intent intent = new Intent(MapActivity.this, PDRService.class);
		stopService(intent);
		Log.i("����ر�", "MapActivity");
		
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

		/*�����ͼ��λ�õ���Ϣ*/
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

	// ���غ������ڵ��menu��ťʱ���ã������˵�
	public boolean onCreateOptionsMenu(final Menu menu) {
		/*
		 * MenuInflater inflater = getMenuInflater(); //
		 * ����menu����Ϊres/menu/menu.xml inflater.inflate(R.menu.map_menu, menu);
		 */
		menu.add(0, R.id.search, 0, R.string.search_main).setIcon(R.drawable.search_menu);
		menu.add(0, R.id.setting, 1, R.string.setting).setIcon(R.drawable.setting_menu);
		//menu.add(0, R.id.routemanager, 2, R.string.route_manager).setIcon(R.drawable.menu_bus);

		return true;
	}


	// ���غ�������Ӧ�˵�ѡ������
	public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
		// �õ���ǰѡ�е�MenuItem��ID,
		int item_id = item.getItemId();

		switch (item_id) {
		case R.id.search:
			/*
			 * �½�һ��Intent���� ָ��intentҪ�������� ����һ���µ�Activity
			 */
			Intent intentToSearch = new Intent();
			intentToSearch.setClass(MapActivity.this, SearchActivity.class);
			startActivity(intentToSearch);
			break;
		case R.id.setting:
			/* �½�һ��Intent���� */
			Intent intentToSetting = new Intent();
			/* ָ��intentҪ�������� */
			intentToSetting.setClass(MapActivity.this, SettingActivity.class);
			/* ����һ���µ�Activity */
			startActivity(intentToSetting);
			break;
		case R.id.routemanager:
			/* �½�һ��Intent����*/ 
			Intent intentToRoute = new Intent();
			/* ָ��intentҪ�������� */
			intentToRoute
			.setClass(MapActivity.this, RouteManagerActivity.class);
			 /*����һ���µ�Activity */
			startActivity(intentToRoute);
			break;
		}
		return true;
	}

	
	//ʵ�ֻ�ȡ�����ֻ���Ļ�����꣬����������Ϣ���͵�����ҳ��
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(mapView.get_flag()!=-1)
		{
			//��ȡ��Ļ������
			TwoDCoordinate coor=mapView.get_location();
			double x=coor.getXCoordinate();
			double y=coor.getYCoordinate();
			Toast.makeText(MapActivity.this, x+","+y, Toast.LENGTH_SHORT).show();
			/**�����������������Ϣ*/
			try
			{
				Controller.getInstance().Query_By_Coordinate(x,y);
				progressdialog=ProgressDialog.show(MapActivity.this, "��ȴ�......", "���ڻ�ȡ�õ����Ϣ......");
			}
			catch(Exception e)
			{
				/*�����쳣*/
				handler.sendEmptyMessage(0);
			}
			/*��ʱ����Ӧ����*/
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
			//�����¼����
			startActivity(new Intent(MapActivity.this,LoginActivity.class));					
			break;
		case location_id:
			if(!started){
				/*try{
				//����������ͻ�ȡ��������λ�õ�����
				Controller.getInstance().addUradioLocationListener(this);
				Controller.getInstance().getMyLocation();
				progressdialog=ProgressDialog.show(this, "��ȴ�......", "���ڻ�ȡ����λ����Ϣ......");
				}catch(Exception e){
					handler.sendEmptyMessage(0);
				}
			
				progressDealing();*/
				handler.sendEmptyMessage(4);
			}
			else{
				/**�ر�PDR����*/
				Intent intent = new Intent(MapActivity.this, PDRService.class);
				stopService(intent);
				get_location.setImageResource(R.drawable.location);
				Toast.makeText(MapActivity.this, "��λ�����ѹر�", Toast.LENGTH_SHORT).show();
				Log.i("����ر�", "MapActivity");
				started = false;
			}
			break;
		/*��������·������*/
		case Emergency_id:
			if(!started)
				Toast.makeText(MapActivity.this, "�뿪����λ����", Toast.LENGTH_SHORT).show();
			else{
				if(MapNavigateContants.ISEMERGENCY)
				{
					try {
						Controller.getInstance().Emergency_Request();
						progressdialog=ProgressDialog.show(this, "��ȴ�......", "���ڻ�ȡ·����Ϣ......");
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
					new AlertDialog.Builder(MapActivity.this).setTitle("��Ϣ��ʾ").setMessage("��ǰΪ�ǽ���״̬����������������")
					.setPositiveButton("ȷ��", null).show();
			}
			break;
		/*��ȡ��Ӧ��ҽ����Ϣ*/
		case doctor_id:
			//ͨ�ţ�����������ͻ�ȡ��Ӧ��ҽ����Ϣ������	
			if(Setting.LOGIN_STATUS.equals("true")&&MapNavigateContants.ISEMERGENCY)
				if(Setting.DOCTOR_NAME==null){
						
					/**������5���Ӻ���Ȼ�޷��õ���Ϣ��ͨ�Ŵ�������*/
					try {
					Controller.getInstance().getDoctorInfo("DoctorInformattion");
					progressdialog=ProgressDialog.show(this, "��ȴ�......", "���ڻ�ȡҽ����Ϣ......");}
					catch (Exception e) {
						handler.sendEmptyMessage(0);
					}
					progressDealing();
				}
				else{
					new AlertDialog.Builder(MapActivity.this).setTitle("ҽ���Ļ�����Ϣ")
					.setMessage("ҽ������:"+Setting.DOCTOR_NAME+"\n"+"��ϵ��ʽ��"+Setting.DOCTOR_PHONE)
					.setPositiveButton("ȷ��", null).show();
				}
			else 
				new AlertDialog.Builder(MapActivity.this).setTitle("��ʾ").setMessage("�û�δ��¼��ǰ״̬Ϊ�ǽ���״̬���޷��������ƥ��").setPositiveButton("ȷ��", null).show();
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
				Toast.makeText(getApplication(), "��ǰ״̬Ϊ�ǽ���������������в�����", Toast.LENGTH_SHORT).show();
		}	
	}
	//��ȡ����Ӧ��ҽ���Ļ�����Ϣ
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

	/*ͨ�������ѯ�ڵ������*/
	@Override
	public void query_point_name(Query_Result result) {
		// TODO Auto-generated method stub
		//���������ѯ�õ��Ľ��
		if(result!=null&&result.query_type.equals("point"))
		{
			int num=result.point_Num;
			String[] point_name=new String[num];
			TwoDCoordinate[] coordinates=new TwoDCoordinate[num];
			point_name=result.Point_Name;
			coordinates=result.coordinate;
			
			MapNavigateContants.POINT_NAME=point_name[0];
			MapNavigateContants.coordinate=coordinates[0];		
			
			/*��Ϣ����Ӧ����*/
			handler.sendEmptyMessage(2);
		}
		//�������Ʋ�ѯ�õ����
		else if(result!=null&&result.query_type.equals("name")&&MapNavigateContants.QUERY_TYPE.equals("MapQuery"))
		{
			int num=result.point_Num;
			MapNavigateContants.QUERY_NAME=new String[num];
			MapNavigateContants.QUERY_COORDINATES=new TwoDCoordinate[num];
			/**�Գ������г�ʼ��*/
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
	/**����ӷ������������ݳ�ʱ������*/
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

	