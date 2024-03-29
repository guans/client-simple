package com.ubirtls.view.Activity;

import com.ubirtls.Controller;
import com.ubirtls.PreferenceConstants;
import com.ubirtls.R;
import com.ubirtls.config.Setting;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 通信信息设置界面，设置通信IP地址和端口号，以及实时定位平台其他组件的标识
 * 
 * @author 胡旭科
 * 
 */
public class CommSettingActivity extends Activity implements
		PreferenceConstants {
	/** UI控件 */
	/**IP地址编辑框*/
	private EditText IP;
	/**端口号编辑框*/
	private EditText port;
	/**定位引擎标识编辑框*/
	private EditText locationEngine;
	/**服务引擎标识编辑框*/
	private EditText serverEngine;
	/**保存修改信息按钮*/
	private Button save;
	/**取消按钮*/
	private Button cancel;
	/** preference */
	private SharedPreferences prefs;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.comm_setting);

		/* 初始化prefs并获取preference的值 */
		prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		String IP_ADDRESS = prefs.getString(PREFS_IP, Setting.IP_ADDRESS);
		int PORT = prefs.getInt(PREFS_PORT, Setting.PORT);
		String SERVERENGINE = prefs.getString(PREFS_SERVER_ENGINE,
				Setting.SERVER_ENGINE_ID);
		String LOCATIONENGINE = prefs.getString(PREFS_LOCATION_ENGINE,
				Setting.LOCATION_ENGINE_ID);

		/* 初始化UI控件 */
		IP = (EditText) this.findViewById(R.id.comm_setting_ip);
		IP.setText(IP_ADDRESS);
		port = (EditText) this.findViewById(R.id.comm_setting_port);
		port.setText(String.valueOf(PORT));
		locationEngine = (EditText) this
				.findViewById(R.id.comm_setting_location_engine);
		locationEngine.setText(LOCATIONENGINE);
		serverEngine = (EditText) this
				.findViewById(R.id.comm_setting_server_engine);
		serverEngine.setText(SERVERENGINE);
		save = (Button) this.findViewById(R.id.comm_setting_save);
		cancel = (Button) this.findViewById(R.id.comm_setting_cancel);

		// 设置保存按钮监听
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 某一项为空
				if (IP.getText().toString().equals("") || port.getText().toString().equals("")
						|| locationEngine.getText().toString().equals("")
						|| serverEngine.getText().toString().equals("")) {
					Toast.makeText(CommSettingActivity.this, "请保证数据不为空", 1000)
							.show();
				}
				// 都不为空
				else {
					try {
						// 检查ip地址和端口号的合法性
						int intPort = new Integer(port.getText().toString());
						String ipAddress = IP.getText().toString();
						String[] array = ipAddress.split("\\.");
						if (array.length != 4)
							Toast.makeText(CommSettingActivity.this,
									"请注意ip和端口号的格式", 1000).show();
						else {
							for (int i = 0; i <= 3; i++) {
								new Integer(array[i]);
							}
							// 内存中保存设置信息
							Controller.getInstance().setCommInfo(ipAddress,
									intPort, serverEngine.getText().toString(),
									locationEngine.getText().toString());
							/**/
							final SharedPreferences.Editor edit = prefs.edit();
							edit.putString(PREFS_IP, ipAddress);
							edit.putInt(PREFS_PORT, intPort);
							edit.putString(PREFS_LOCATION_ENGINE,
									locationEngine.getText().toString());
							edit.putString(PREFS_SERVER_ENGINE, serverEngine
									.getText().toString());

							Toast.makeText(CommSettingActivity.this, "保存设置成功",
									1000).show();
						}
					} catch (NumberFormatException e1) {
						Toast.makeText(CommSettingActivity.this,
								"请注意ip和端口号的格式", 1000).show();
					}

				}
			}
		});
		// 设置取消按钮监听
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CommSettingActivity.this.finish();
			}
		});
	}
}
