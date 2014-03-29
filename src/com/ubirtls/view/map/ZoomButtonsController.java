package com.ubirtls.view.map;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import android.view.MotionEvent;
import android.view.View;

/**
 * java������ƣ�����ʵ����Android��ZoomButtonControl��
 * 
 * @author �����
 * @version 0.1
 */
public class ZoomButtonsController {

	/**
	 * ZOOM �����
	 */
	private static Class ZOOM_CLASS;
	/**
	 * Listener�����
	 */
	private static Class LISTENER_CLASS;
	/**
	 * setOnZoomListener��������
	 */
	private static Method setOnZoomListener;
	/**
	 * setVisible��������
	 */
	private static Method setVisible;
	/**
	 * setZoomInEnabled��������
	 */
	private static Method setZoomInEnabled;
	/**
	 * setZoomOutEnabled��������
	 */
	private static Method setZoomOutEnabled;
	private static Method onTouch;
	/**
	 * isVisible��������
	 */
	private static Method isVisible;

	private Object controller;
	/**
	 * ��ʼ��������Լ���������
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
			 * ��־ ��¼�쳣��Ϣ
			 */
		}
	}

	/**
	 * ZOOM�ļ����߽ӿ�
	 * 
	 * @author �����
	 * @version 0.1
	 */
	public interface OnZoomListener {
		public void onZoom(boolean zoomIn);

		public void onVisibilityChanged(boolean visible);
	}

	/**
	 * ���캯��
	 * 
	 * @param view ��controller�ĸ���ͼ
	 */
	public ZoomButtonsController(final View view) {
		if (ZOOM_CLASS != null) {
			try {
				controller = ZOOM_CLASS.getConstructor(View.class).newInstance(
						view);
			} catch (Exception ex) {
				/**
				 * ��־ ��¼�쳣��Ϣ
				 */
			}
		}
	}

	/**
	 * ����zoom�ؼ��ļ�����
	 * 
	 * @param listener ZOOM�ؼ��ļ�����
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
							 * ��־ ��¼�쳣��Ϣ
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
				 * ��־ ��¼�쳣��Ϣ
				 */
			}
		}
	}

	/**
	 * ����zoom�ؼ��Ŀɼ���
	 * 
	 * @param visible true�ɼ� false���ɼ�
	 */
	public void setVisible(final boolean visible) {
		if (controller != null) {
			try {
				setVisible.invoke(controller, visible);
			} catch (Exception ex) {
				/**
				 * ��־ ��¼�쳣��Ϣ
				 */
			}
		}
	}

	/**
	 * zoomIn�ؼ�ʹ��
	 * 
	 * @param enabled true�Ŵ�ؼ�����ʹ�� false�Ŵ�ؼ�������ʹ��
	 */
	public void setZoomInEnabled(final boolean enabled) {
		if (controller != null) {
			try {
				setZoomInEnabled.invoke(controller, enabled);
			} catch (Exception ex) {
				/**
				 * ��־ ��¼�쳣��Ϣ
				 */
			}
		}
	}

	/**
	 * zoomOut�ؼ�ʹ��
	 * 
	 * @param enabled true��С�ؼ�ʹ�� false��С�ؼ�����ʹ��
	 */
	public void setZoomOutEnabled(final boolean enabled) {
		if (controller != null) {
			try {
				setZoomOutEnabled.invoke(controller, enabled);
			} catch (Exception ex) {
				/**
				 * ��־ ��¼�쳣��Ϣ
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
	 * �ж����ſؼ��Ƿ�ɼ�
	 * 
	 * @return �ɼ��򷵻�true ���򷵻�false
	 */
	public boolean isVisible() {
		if (controller != null) {
			try {
				return (Boolean) isVisible.invoke(controller);
			} catch (Exception ex) {
				/**
				 * ��־ ��¼�쳣��Ϣ
				 */
			}
		}
		return false;
	}
}
