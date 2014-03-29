package com.ubirtls.view.Activity;

import com.ubirtls.AccountCheckListener;
import com.ubirtls.Controller;
import com.ubirtls.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * ע����棬ͬʱʵ��AccountCheckListener����ע���¼���
 * 
 * @author �����
 * @version 1.0
 */
public class RegisterActivity extends Activity implements AccountCheckListener,
		OnClickListener {
	/** �û����༭�� */
	private EditText userNameEditText;

	/** email�༭�� */
	private EditText emailEditText;

	/** �û�����༭�� */
	private EditText passwordEditText;

	/** �ظ��������� */
	private EditText repeatPasswordEditText;
	
	/**ͨ�����ð�ť*/
	private Button commSettingButton;
	
	/** ע�ᰴť��������ע�� */
	private Button registerButton;

	/** �˳���ť�����ص���¼���� */
	private Button backButton;

	/** ���������ĶԻ��� */
	ProgressDialog dialog;

	/** ���������� */
	private Controller controller;

	/** �������ӳ�ʱ */
	private Handler handler;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.user_register);
		getWindow().setFeatureInt(Window.FEATURE_NO_TITLE,
				R.layout.register_titlebar);
		/* ��ʼ��UI�ؼ� */
		userNameEditText = (EditText) this
				.findViewById(R.id.register_username);
		emailEditText = (EditText) this.findViewById(R.id.register_emailbox);
		passwordEditText = (EditText) this
				.findViewById(R.id.register_password1);
		repeatPasswordEditText = (EditText) this
				.findViewById(R.id.register_password2);
		/*commSettingButton = (Button)this.findViewById(R.id.register_setting_button);
		commSettingButton.setOnClickListener(this);*/
		registerButton = (Button) this.findViewById(R.id.register_login);
		registerButton.setOnClickListener(this);
		backButton = (Button) this.findViewById(R.id.register_back);
		backButton.setOnClickListener(this);
		/* ��ʼ�������� */
		controller = Controller.getInstance();
		/* ���ü��� */
		controller.addAccountCheckListerner(this);
		/* ��ʼhandler */
		handler = new Handler() {
			public void handleMessage(Message msg) {
				/* ��ʾ�û�ע����� */
				switch (msg.what) {
				case 0:
					Dialog alertDialog = new AlertDialog.Builder(
							RegisterActivity.this)
							.setTitle(
									getResources()
											.getString(
													R.string.register_timeout_alter_title))
							.setMessage(
									getResources()
											.getString(
													R.string.register_timeout_alter_message))
							.setPositiveButton(
									getResources().getString(R.string.OK), null)
							.create();// ������ť
					alertDialog.show();
					break;
				case 1:
					/* ��ʾ�û�ע����� */
					new AlertDialog.Builder(RegisterActivity.this)
							.setTitle(
									getResources()
											.getString(
													R.string.register_error_alter_title))
							.setMessage(
									getResources()
											.getString(
													R.string.register_error_alter_message))
							.setPositiveButton(
									getResources().getString(R.string.OK), null)
							.create().show();// ������ť
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

	// ��Activity��ͣʱ����,��һ��Activity��ɲ���Ծ״̬ʱ��������ͣ
	@Override
	protected void onPause() {
		super.onPause();
	}

	// onStart������,�����ػ�
	@Override
	protected void onResume() {
		super.onResume();
	}

	// ��Activity ����ʱ����
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/* ʵ�ֽӿ�AccountCheckListener������������֤�¼� */
	@Override
	public void loginCheck(boolean success, boolean usernameSuccess,
			boolean passwordSuccess) {

	}

	/* ʵ�ֽӿ�AccountCheckListener������ע���¼� */
	@Override
	public void registerCheck(boolean success) {
		/* ���ٽ����� */
		dialog.dismiss();
		if (success) {
			/*�����ͼ������Ϣ*/
			//Controller.getInstance().requestMapInfo();
			/* ������ͼActivity */
/*			Intent goToMap = new Intent(RegisterActivity.this,
					MapActivity.class);
			startActivity(goToMap);
*/			RegisterActivity.this.finish();
		} else {
			handler.sendEmptyMessage(1);
		}
	}

	/* ʵ�ֽӿ�AccountCheckListener�������޸������¼� */
	@Override
	public void changePasswordCheck(boolean success) {

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		/* �û�������ص�¼���水ť */
		if (view.getId() == backButton.getId()) {
			RegisterActivity.this.finish();
		}
		//����ͨ����Ϣ
		else if(view.getId() == commSettingButton.getId()){
			Intent goToSetting = new Intent(RegisterActivity.this,CommSettingActivity.class);
			startActivity(goToSetting);
		}
		/* �û����ע�ᰴť */
		else if (view.getId() == registerButton.getId()) {
			final String username = this.userNameEditText.getText().toString();
			final String email = this.emailEditText.getText().toString();
			final String password = this.passwordEditText.getText().toString();
			String repeatPassword = this.repeatPasswordEditText.getText()
					.toString();
			/* ������� */
			if (username.equals("") || email.equals("") || password.equals("")
					|| repeatPassword.equals("")) {
				Toast.makeText(this,
						getResources().getString(R.string.register_check_null),
						2000).show();
				return;
			} else if (!password.equals(repeatPassword)) {
				Toast.makeText(
						this,
						getResources().getString(
								R.string.register_check_not_equal), 2000)
						.show();
				return;
			}

			/* ��������ʾ */
			dialog = ProgressDialog.show(RegisterActivity.this, getResources()
					.getString(R.string.register_progress_title),
					getResources()
							.getString(R.string.register_progress_message),
					true);
			/*��鳬ʱ3s*/

			/*ע���߳�*/
			new Thread() {
				public void run() {
					try {			
						controller.register(username, password, email);
					} catch (Exception e) {			
						if (dialog.isShowing()) {
							dialog.dismiss();
							handler.sendEmptyMessage(0);
						}
						e.printStackTrace();
					}
				}
			}.start();
			new Thread() {
				public void run() {
					try {
						sleep(5000);
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
		} else {
			/**
			 * ��������
			 */
		}
	}
	
}