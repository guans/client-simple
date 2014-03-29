package com.ubirtls.view.Activity;

import com.ubirtls.Controller;
import com.ubirtls.R;
import com.ubirtls.config.Setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;

/**
 * Ӧ�ó������������
 * 
 * @author �����
 * @version 1.0
 * 
 */
public class SplashScreen extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Handler x = new Handler();
		
		
		/* ��ʱ3�� */
		x.postDelayed(new splashhandler(), 3000);
		
		WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		//ȡ��WifiInfo����
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		Setting.MAC = wifiInfo.getMacAddress();
		
		
	}

	/**
	 * ʵ��Runnable�ӿ�
	 * 
	 * @author �����
	 * @version 1.0
	 * 
	 */
	class splashhandler implements Runnable {

		public void run() {
			Controller.getInstance().requestMapInfo();
			startActivity(new Intent(SplashScreen.this,MapActivity.class));
			SplashScreen.this.finish();
		}

	}
}