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
 * ͨ����Ϣ���ý��棬����ͨ��IP��ַ�Ͷ˿ںţ��Լ�ʵʱ��λƽ̨��������ı�ʶ
 * 
 * @author �����
 * 
 */
public class CommSettingActivity extends Activity implements
		PreferenceConstants {
	/** UI�ؼ� */
	/**IP��ַ�༭��*/
	private EditText IP;
	/**�˿ںű༭��*/
	private EditText port;
	/**��λ�����ʶ�༭��*/
	private EditText locationEngine;
	/**���������ʶ�༭��*/
	private EditText serverEngine;
	/**�����޸���Ϣ��ť*/
	private Button save;
	/**ȡ����ť*/
	private Button cancel;
	/** preference */
	private SharedPreferences prefs;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.comm_setting);

		/* ��ʼ��prefs����ȡpreference��ֵ */
		prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		String IP_ADDRESS = prefs.getString(PREFS_IP, Setting.IP_ADDRESS);
		int PORT = prefs.getInt(PREFS_PORT, Setting.PORT);
		String SERVERENGINE = prefs.getString(PREFS_SERVER_ENGINE,
				Setting.SERVER_ENGINE_ID);
		String LOCATIONENGINE = prefs.getString(PREFS_LOCATION_ENGINE,
				Setting.LOCATION_ENGINE_ID);

		/* ��ʼ��UI�ؼ� */
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

		// ���ñ��水ť����
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// ĳһ��Ϊ��
				if (IP.getText().toString().equals("") || port.getText().toString().equals("")
						|| locationEngine.getText().toString().equals("")
						|| serverEngine.getText().toString().equals("")) {
					Toast.makeText(CommSettingActivity.this, "�뱣֤���ݲ�Ϊ��", 1000)
							.show();
				}
				// ����Ϊ��
				else {
					try {
						// ���ip��ַ�Ͷ˿ںŵĺϷ���
						int intPort = new Integer(port.getText().toString());
						String ipAddress = IP.getText().toString();
						String[] array = ipAddress.split("\\.");
						if (array.length != 4)
							Toast.makeText(CommSettingActivity.this,
									"��ע��ip�Ͷ˿ںŵĸ�ʽ", 1000).show();
						else {
							for (int i = 0; i <= 3; i++) {
								new Integer(array[i]);
							}
							// �ڴ��б���������Ϣ
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

							Toast.makeText(CommSettingActivity.this, "�������óɹ�",
									1000).show();
						}
					} catch (NumberFormatException e1) {
						Toast.makeText(CommSettingActivity.this,
								"��ע��ip�Ͷ˿ںŵĸ�ʽ", 1000).show();
					}

				}
			}
		});
		// ����ȡ����ť����
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CommSettingActivity.this.finish();
			}
		});
	}
}