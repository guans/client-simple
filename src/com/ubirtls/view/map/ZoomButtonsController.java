package com.ubirtls.view.map;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import android.view.MotionEvent;
import android.view.View;

/**
 * java反射机制，重新实现了Android的ZoomButtonControl类
 * 
 * @author 胡旭科
 * @version 0.1
 */
public class ZoomButtonsController {

	/**
	 * ZOOM 类变量
	 */
	private static Class ZOOM_CLASS;
	/**
	 * Listener类变量
	 */
	private static Class LISTENER_CLASS;
	/**
	 * setOnZoomListener方法变量
	 */
	private static Method setOnZoomListener;
	/**
	 * setVisible方法变量
	 */
	private static Method setVisible;
	/**
	 * setZoomInEnabled方法变量
	 */
	private static Method setZoomInEnabled;
	/**
	 * setZoomOutEnabled方法变量
	 */
	private static Method setZoomOutEnabled;
	private static Method onTouch;
	/**
	 * isVisible方法变量
	 */
	private static Method isVisible;

	private Object controller;
	/**
	 * 初始化类变量以及方法变量
	 */
	static {
		try {
			ZOOM_CLASS = Class.forName("android.widget.ZoomButtonsController");
			for (Class clazz : ZOOM_CLASS.getDeclaredClasses()) {
				if ("OnZoomListener".equals(clazz.getSimpleName())) {
					LISTENER_CLASS = clazz;
				}
			}
			setOnZoomListener = ZOOM_CLASS.getMethod("setOnZoomListener",
					LISTENER_CLASS);
			setVisible = ZOOM_CLASS.getMethod("setVisible", Boolean.TYPE);
			setZoomInEnabled = ZOOM_CLASS.getMethod("setZoomInEnabled",
					Boolean.TYPE);
			setZoomOutEnabled = ZOOM_CLASS.getMethod("setZoomOutEnabled",
					Boolean.TYPE);
			onTouch = ZOOM_CLASS.getMethod("onTouch", View.class,
					MotionEvent.class);
			isVisible = ZOOM_CLASS.getMethod("isVisible");
		} catch (Exception ex) {
			/**
			 * 日志 记录异常信息
			 */
		}
	}

	/**
	 * ZOOM的监听者接口
	 * 
	 * @author 胡旭科
	 * @version 0.1
	 */
	public interface OnZoomListener {
		public void onZoom(boolean zoomIn);

		public void onVisibilityChanged(boolean visible);
	}

	/**
	 * 构造函数
	 * 
	 * @param view 该controller的父视图
	 */
	public ZoomButtonsController(final View view) {
		if (ZOOM_CLASS != null) {
			try {
				controller = ZOOM_CLASS.getConstructor(View.class).newInstance(
						view);
			} catch (Exception ex) {
				/**
				 * 日志 记录异常信息
				 */
			}
		}
	}

	/**
	 * 设置zoom控件的监听者
	 * 
	 * @param listener ZOOM控件的监听者
	 */
	public void setOnZoomListener(final OnZoomListener listener) {
		if (controller != null) {
			try {
				final InvocationHandler handler = new InvocationHandler() {
					public Object invoke(Object object, Method method,
							Object[] args) {
						if ("onZoom".equals(method.getName())) {
							listener.onZoom((Boolean) args[0]);
						} else if ("onVisibilityChanged".equals(method
								.getName())) {
							listener.onVisibilityChanged((Boolean) args[0]);
						} else {
							/**
							 * 日志 记录异常信息
							 */
						}
						return null;
					}
				};
				Object proxy = Proxy.newProxyInstance(LISTENER_CLASS
						.getClassLoader(), new Class[] { LISTENER_CLASS },
						handler);
				setOnZoomListener.invoke(controller, proxy);
			} catch (Exception ex) {
				/**
				 * 日志 记录异常信息
				 */
			}
		}
	}

	/**
	 * 设置zoom控件的可见性
	 * 
	 * @param visible true可见 false不可见
	 */
	public void setVisible(final boolean visible) {
		if (controller != null) {
			try {
				setVisible.invoke(controller, visible);
			} catch (Exception ex) {
				/**
				 * 日志 记录异常信息
				 */
			}
		}
	}

	/**
	 * zoomIn控件使能
	 * 
	 * @param enabled true放大控件可以使用 false放大控件不可以使用
	 */
	public void setZoomInEnabled(final boolean enabled) {
		if (controller != null) {
			try {
				setZoomInEnabled.invoke(controller, enabled);
			} catch (Exception ex) {
				/**
				 * 日志 记录异常信息
				 */
			}
		}
	}

	/**
	 * zoomOut控件使能
	 * 
	 * @param enabled true缩小控件使能 false缩小控件可以使用
	 */
	public void setZoomOutEnabled(final boolean enabled) {
		if (controller != null) {
			try {
				setZoomOutEnabled.invoke(controller, enabled);
			} catch (Exception ex) {
				/**
				 * 日志 记录异常信息
				 */
			}
		}
	}

	public boolean onTouch(final View v, final MotionEvent m) {
		if (controller != null) {
			try {
				return (Boolean) onTouch.invoke(controller, v, m);
			} catch (Exception ex) {
			}
		}
		return false;
	}

	/**
	 * 判断缩放控件是否可见
	 * 
	 * @return 可见则返回true 否则返回false
	 */
	public boolean isVisible() {
		if (controller != null) {
			try {
				return (Boolean) isVisible.invoke(controller);
			} catch (Exception ex) {
				/**
				 * 日志 记录异常信息
				 */
			}
		}
		return false;
	}
}
