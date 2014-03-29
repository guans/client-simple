/**
 * 
 */
package com.ubirtls.view.Activity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.ubirtls.AccountCheckListener;
import com.ubirtls.Controller;
import com.ubirtls.R;
import com.ubirtls.config.Setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.PrintWriterPrinter;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��¼���棬ͬʱʵ��AccountCheckListener����������֤�¼���
 * 
 * @author �����
 * @version 1.0
 */
public class LoginActivity extends Activity implements AccountCheckListener,
		OnClickListener {
	/** �û����༭�� */
	private EditText userNameEditText;

	/** �û�����༭�� */
	private EditText passwordEditText;

	/** �Ƿ񱣴��û����� */
	private CheckBox saveCheckBox;

	/** ��¼��ť */
	private Button loginButton;

	/** ע�ᰴť������ע����� */
	private Button registerButton;
	/**ע���ı�*/
	private TextView registerText;
	/**ͨ�����ð�ť*/
	private Button commSettingButton;
	/** �˳���ť���˳�Ӧ�ó��� */
	private Button exitButton;

	/** ���������ĶԻ��� */
	ProgressDialog dialog;

	/** ���������� */
	private Controller controller;

	/** ������¼������֤��ʱ */
	private Handler handler;

	/* ��Activity�״δ���ʱ���� */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.userlogin);
		getWindow().setFeatureInt(Window.FEATURE_NO_TITLE,
				R.layout.login_titlebar);

		/* ��ʼ��UI�ؼ� */
		userNameEditText = (EditText) this
				.findViewById(R.id.login_username);
		passwordEditText = (EditText) this
				.findViewById(R.id.login_password);
		saveCheckBox = (CheckBox) this
				.findViewById(R.id.login_savepassword);
		/*commSettingButton = (Button)this.findViewById(R.id.login_setting_button);
		commSettingButton.setOnClickListener(this);*/
		loginButton = (Button) this.findViewById(R.id.login_login);
		loginButton.setOnClickListener(this);
		
		registerText=(TextView)this.findViewById(R.id.login_register);
		registerText.setOnClickListener(this);
		/*registerButton = (Button) this.findViewById(R.id.login_register_button);
		registerButton.setOnClickListener(this);*/
		/*exitButton = (Button) this.findViewById(R.id.login_exit_button);
		exitButton.setOnClickListener(this);*/
		/* ��ʼ��Controller */
		controller = Controller.getInstance();
		/* ���ü��� */
		controller.addAccountCheckListerner(this);
		/* ��SharedPreferences�л���û��˺���Ϣ */
		SharedPreferences userInfo = getSharedPreferences("user_info",
				Activity.MODE_PRIVATE);
		boolean check = userInfo.getBoolean("sava_password", false);
		String username = userInfo.getString("username", "");
		String password = "";
		if (check)
			password = userInfo.getString("password", "");
		/* �����û���Ϣ */
		saveCheckBox.setChecked(check);
		userNameEditText.setText(username);
		passwordEditText.setText(password);
		Setting.USERNAME = username;
		/* ��ʼhandler */
		handler = new Handler() {
			public void handleMessage(Message msg) {
				/* ��ʾ�û���¼��Ϣ */
				switch (msg.what) {
				/* ��¼��ʱ */
				case 0:
					Dialog alertDialog = new AlertDialog.Builder(
							LoginActivity.this)
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
				case 1: // ��¼�û������������
					new AlertDialog.Builder(LoginActivity.this)
							.setTitle(
									getResources().getString(
											R.string.login_error_alter_title))
							.setMessage(
									getResources().getString(
											R.string.login_error_alter_message))
							.setPositiveButton(
									getResources().getString(R.string.OK), null)
							.create().show();
					break;
				default:
					break;
				}

			}
		};
	}

	/* ��Activity���������������� */
	@Override
	public void onStart() {
		super.onStart();
	}

	/* ��Activity��ͣʱ���� */
	@Override
	protected void onPause() {
		super.onPause();
	}

	/* onStart������ */
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Controller.getInstance().closeCommunication();
	}

	/**/
	@Override
	protected void onStop() {
		super.onStop();
	}

	/* ��Activity ����ʱ���� */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/* ʵ�ֽӿ�AccountCheckListener������������֤�¼� */
	@Override
	public void loginCheck(boolean success, boolean usernameSuccess,
			boolean passwordSuccess) {
		/* �رս�������ʾ */
		if (dialog.isShowing())
			dialog.dismiss();
		if (success) {
			/* ��¼�ɹ��ȱ���SharedPreference */
			SharedPreferences userInfo = getSharedPreferences("user_info",
					Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = userInfo.edit();
			boolean check = saveCheckBox.isChecked();
			editor.putBoolean("sava_password", check);
			editor.putString("username", userNameEditText.getText().toString());
			if (check)
				editor.putString("password", passwordEditText.getText()
						.toString());
			editor.commit();
			Setting.USERNAME = userNameEditText.getText().toString();
			/*�����ͼ������Ϣ*/
			Controller.getInstance().requestMapInfo();
			/* ������ͼActivity */
			//Intent goToMap = new Intent(LoginActivity.this, MapActivity.class);
			//startActivity(goToMap);
			LoginActivity.this.finish();
			Setting.LOGIN_STATUS="true";
		} else {
			/* ��ʾ�û�������֤���� */
			handler.sendEmptyMessage(1);
		}
	}

	/* ʵ�ֽӿ�AccountCheckListener������ע���¼� */
	@Override
	public void registerCheck(boolean success) {

	}

	/* ʵ�ֽӿ�AccountCheckListener�������޸������¼� */
	@Override
	public void changePasswordCheck(boolean success) {

	}
	

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		/* �û������¼��ť */
		if (view.getId() == loginButton.getId()) {
			final String username = this.userNameEditText.getText().toString();
 			final String password = this.passwordEditText.getText().toString();
			/* �ڴ˴������û����������� */
			if (username.equals("") || password.equals("")) {
				Toast.makeText(this,
						getResources().getString(R.string.login_check_null),
						2000).show();
				return;
			}
			/* ����һ����������ʾ */
			dialog = ProgressDialog.show(LoginActivity.this, getResources()
					.getString(R.string.login_progress_title), getResources()
					.getString(R.string.login_progress_message), true);

			try {
				controller.login(username, password);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//������¼�߳�
			/*new Thread() {
				public void run() {
					try {			
						controller.login(username, password);			
					} catch (Exception e) {						
						if (dialog.isShowing()) {
							dialog.dismiss();
							handler.sendEmptyMessage(0);
						}
						e.printStackTrace();
					}
				}
			}.start();
			*/
			// �ȴ�5 ����Ƿ����ӳ�ʱ
			new Thread() {
				public void run() {
					try {
						sleep(6000);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (dialog.isShowing()) {
							dialog.dismiss();
							handler.sendEmptyMessage(0);
						}
					}
				}
			}.start();
		}
		/*//����ͨ����Ϣ
		else if(view.getId() == commSettingButton.getId()){
			Intent goToSetting = new Intent(LoginActivity.this,CommSettingActivity.class);
			startActivity(goToSetting);
		}*/
		/* �û����ע�ᰴť */
		else if (view.getId() == registerText.getId()) {
			/* ����ע��Activity */
			Intent goToRegister = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(goToRegister);
		}
		/* �û�����˳���ť 
		else if (view.getId() == exitButton.getId()) {
			LoginActivity.this.finish();
		}*/ else {
			/**
			 * ��������
			 */
		}
	}

}