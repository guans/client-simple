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
 * 登录界面，同时实现AccountCheckListener监听身份认证事件。
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class LoginActivity extends Activity implements AccountCheckListener,
		OnClickListener {
	/** 用户名编辑框 */
	private EditText userNameEditText;

	/** 用户密码编辑框 */
	private EditText passwordEditText;

	/** 是否保存用户密码 */
	private CheckBox saveCheckBox;

	/** 登录按钮 */
	private Button loginButton;

	/** 注册按钮，跳到注册界面 */
	private Button registerButton;
	/**注册文本*/
	private TextView registerText;
	/**通信设置按钮*/
	private Button commSettingButton;
	/** 退出按钮，退出应用程序 */
	private Button exitButton;

	/** 带进度条的对话框 */
	ProgressDialog dialog;

	/** 控制器对象 */
	private Controller controller;

	/** 处理登录身份验证超时 */
	private Handler handler;

	/* 在Activity首次创建时调用 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.userlogin);
		getWindow().setFeatureInt(Window.FEATURE_NO_TITLE,
				R.layout.login_titlebar);

		/* 初始化UI控件 */
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
		/* 初始化Controller */
		controller = Controller.getInstance();
		/* 设置监听 */
		controller.addAccountCheckListerner(this);
		/* 从SharedPreferences中获得用户账号信息 */
		SharedPreferences userInfo = getSharedPreferences("user_info",
				Activity.MODE_PRIVATE);
		boolean check = userInfo.getBoolean("sava_password", false);
		String username = userInfo.getString("username", "");
		String password = "";
		if (check)
			password = userInfo.getString("password", "");
		/* 设置用户信息 */
		saveCheckBox.setChecked(check);
		userNameEditText.setText(username);
		passwordEditText.setText(password);
		Setting.USERNAME = username;
		/* 初始handler */
		handler = new Handler() {
			public void handleMessage(Message msg) {
				/* 提示用户登录信息 */
				switch (msg.what) {
				/* 登录延时 */
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
				case 1: // 登录用户名或密码错误
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

	/* 在Activity创建后或重启后调用 */
	@Override
	public void onStart() {
		super.onStart();
	}

	/* 在Activity暂停时调用 */
	@Override
	protected void onPause() {
		super.onPause();
	}

	/* onStart后会调用 */
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

	/* 在Activity 销毁时调用 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/* 实现接口AccountCheckListener，监听身份认证事件 */
	@Override
	public void loginCheck(boolean success, boolean usernameSuccess,
			boolean passwordSuccess) {
		/* 关闭进度条提示 */
		if (dialog.isShowing())
			dialog.dismiss();
		if (success) {
			/* 登录成功先保存SharedPreference */
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
			/*请求地图描述信息*/
			Controller.getInstance().requestMapInfo();
			/* 启动地图Activity */
			//Intent goToMap = new Intent(LoginActivity.this, MapActivity.class);
			//startActivity(goToMap);
			LoginActivity.this.finish();
			Setting.LOGIN_STATUS="true";
		} else {
			/* 提示用户身份认证错误 */
			handler.sendEmptyMessage(1);
		}
	}

	/* 实现接口AccountCheckListener，监听注册事件 */
	@Override
	public void registerCheck(boolean success) {

	}

	/* 实现接口AccountCheckListener，监听修改密码事件 */
	@Override
	public void changePasswordCheck(boolean success) {

	}
	

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		/* 用户点击登录按钮 */
		if (view.getId() == loginButton.getId()) {
			final String username = this.userNameEditText.getText().toString();
 			final String password = this.passwordEditText.getText().toString();
			/* 在此处添加用户名和密码检查 */
			if (username.equals("") || password.equals("")) {
				Toast.makeText(this,
						getResources().getString(R.string.login_check_null),
						2000).show();
				return;
			}
			/* 添加一个进度条提示 */
			dialog = ProgressDialog.show(LoginActivity.this, getResources()
					.getString(R.string.login_progress_title), getResources()
					.getString(R.string.login_progress_message), true);

			try {
				controller.login(username, password);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//启动登录线程
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
			// 等待5 检查是否连接超时
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
		/*//设置通信信息
		else if(view.getId() == commSettingButton.getId()){
			Intent goToSetting = new Intent(LoginActivity.this,CommSettingActivity.class);
			startActivity(goToSetting);
		}*/
		/* 用户点击注册按钮 */
		else if (view.getId() == registerText.getId()) {
			/* 启动注册Activity */
			Intent goToRegister = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(goToRegister);
		}
		/* 用户点击退出按钮 
		else if (view.getId() == exitButton.getId()) {
			LoginActivity.this.finish();
		}*/ else {
			/**
			 * 不作处理
			 */
		}
	}

}
