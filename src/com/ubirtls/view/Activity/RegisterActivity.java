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
 * 注册界面，同时实现AccountCheckListener监听注册事件。
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class RegisterActivity extends Activity implements AccountCheckListener,
		OnClickListener {
	/** 用户名编辑框 */
	private EditText userNameEditText;

	/** email编辑框 */
	private EditText emailEditText;

	/** 用户密码编辑框 */
	private EditText passwordEditText;

	/** 重复输入密码 */
	private EditText repeatPasswordEditText;
	
	/**通信设置按钮*/
	private Button commSettingButton;
	
	/** 注册按钮，点击完成注册 */
	private Button registerButton;

	/** 退出按钮，返回到登录界面 */
	private Button backButton;

	/** 带进度条的对话框 */
	ProgressDialog dialog;

	/** 控制器对象 */
	private Controller controller;

	/** 处理连接超时 */
	private Handler handler;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.user_register);
		getWindow().setFeatureInt(Window.FEATURE_NO_TITLE,
				R.layout.register_titlebar);
		/* 初始化UI控件 */
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
		/* 初始化控制器 */
		controller = Controller.getInstance();
		/* 设置监听 */
		controller.addAccountCheckListerner(this);
		/* 初始handler */
		handler = new Handler() {
			public void handleMessage(Message msg) {
				/* 提示用户注册错误 */
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
							.create();// 创建按钮
					alertDialog.show();
					break;
				case 1:
					/* 提示用户注册错误 */
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
							.create().show();// 创建按钮
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

	// 在Activity暂停时调用,当一个Activity变成不活跃状态时都会先暂停
	@Override
	protected void onPause() {
		super.onPause();
	}

	// onStart后会调用,进行重绘
	@Override
	protected void onResume() {
		super.onResume();
	}

	// 在Activity 销毁时调用
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/* 实现接口AccountCheckListener，监听身份认证事件 */
	@Override
	public void loginCheck(boolean success, boolean usernameSuccess,
			boolean passwordSuccess) {

	}

	/* 实现接口AccountCheckListener，监听注册事件 */
	@Override
	public void registerCheck(boolean success) {
		/* 销毁进度条 */
		dialog.dismiss();
		if (success) {
			/*请求地图描述信息*/
			//Controller.getInstance().requestMapInfo();
			/* 启动地图Activity */
/*			Intent goToMap = new Intent(RegisterActivity.this,
					MapActivity.class);
			startActivity(goToMap);
*/			RegisterActivity.this.finish();
		} else {
			handler.sendEmptyMessage(1);
		}
	}

	/* 实现接口AccountCheckListener，监听修改密码事件 */
	@Override
	public void changePasswordCheck(boolean success) {

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		/* 用户点击返回登录界面按钮 */
		if (view.getId() == backButton.getId()) {
			RegisterActivity.this.finish();
		}
		//设置通信信息
		else if(view.getId() == commSettingButton.getId()){
			Intent goToSetting = new Intent(RegisterActivity.this,CommSettingActivity.class);
			startActivity(goToSetting);
		}
		/* 用户点击注册按钮 */
		else if (view.getId() == registerButton.getId()) {
			final String username = this.userNameEditText.getText().toString();
			final String email = this.emailEditText.getText().toString();
			final String password = this.passwordEditText.getText().toString();
			String repeatPassword = this.repeatPasswordEditText.getText()
					.toString();
			/* 初步检查 */
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

			/* 进度条提示 */
			dialog = ProgressDialog.show(RegisterActivity.this, getResources()
					.getString(R.string.register_progress_title),
					getResources()
							.getString(R.string.register_progress_message),
					true);
			/*检查超时3s*/

			/*注册线程*/
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
			 * 不作处理
			 */
		}
	}
	
}
