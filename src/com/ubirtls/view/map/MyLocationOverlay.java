package com.ubirtls.view.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Paint.Style;
import android.view.MotionEvent;

import com.ubirtls.Controller;
import com.ubirtls.LocationChangeListener;
import com.ubirtls.Observer;
import com.ubirtls.R;

import coordinate.TwoDCoordinate;

/**
 * 显示用户位置的覆盖层
 * 
 * @author 胡旭科
 * @version 0.1
 */
public class MyLocationOverlay extends Overlay implements
		LocationChangeListener {
	/**
	 * 用来绘制人员位置的画笔
	 */
	protected final Paint paint = new Paint();
	/**
	 * 用来绘制精度圆的画笔
	 */
	protected final Paint circlePaint = new Paint();
	/**
	 * 用于显示人员位置的图标
	 */
	protected final Bitmap personIcon;

	/**
	 * 当boolFollow为true时，用来控制地图跟踪用户的位置，即将用户 的位置设在地图的中心。
	 */
	protected boolean boolFollow = true;
	/**
	 * 绘制精度使能
	 */
	protected boolean drawAccuracyEnabled = true;
	/**
	 * 用户当前的坐标位置
	 */
	private TwoDCoordinate myCurrentLocation = null;
	/**
	 * 人员热点
	 */
	protected final PointF PERSON_HOTSPOT;

	/**
	 * 避免在draw时分配
	 */
	private final float[] matrixValues = new float[9];
	private final Matrix matrix = new Matrix();
	/**
	 * 观察者，这里是MapView
	 */
	private Observer observer;

	/**
	 * 构造函数
	 * 
	 * @param context Context对象
	 * @param mapView MapView对象
	 */
	public MyLocationOverlay(final Context context) {
		super(context);
		circlePaint.setARGB(0, 100, 100, 255);
		circlePaint.setAntiAlias(true);
		personIcon = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.mylocality4);
		// Calculate position of person icon's feet, scaled to screen density
		PERSON_HOTSPOT = new PointF(24.0f * scale + 0.5f, 39.0f * scale + 0.5f);
		Controller.getInstance().addLocationChangeListerner(this);

	}

	public void setObserver(Observer observer) {
		if (observer != null)
			this.observer = observer;
	}

	/**
	 * 是否跟踪用户的位置
	 * 
	 * @return 跟踪则返回true 否则返回false
	 */
	public boolean isFollow() {
		return boolFollow;
	}

	/**
	 * 禁止地图跟踪用户的位置
	 */
	public void disableFollow() {
		boolFollow = false;
	}

	/**
	 * 使能地图追踪用户的位置。
	 */
	public void enableFollow() {
		boolFollow = true;
		if ((myCurrentLocation != null) && (observer != null)) {
			observer.notifyFollow(myCurrentLocation);
		} else {
			/**
			 * 从Setting中提取用户的位置信息
			 */
		}
		/*
		 * // 更新屏幕 if (observer != null) { observer.letInvalidate(); }
		 */}

	/**
	 * 如果使能 将会在用户当前的位置绘制一个精度圆
	 * 
	 * @param drawAccuracyEnabled 使能则为true 否则为false
	 */
	public void setDrawAccuracyEnabled(final boolean drawAccuracyEnabled) {
		this.drawAccuracyEnabled = drawAccuracyEnabled;
	}

	/**
	 * 如果使能 将会在用户当前的位置绘制一个精度圆
	 * 
	 * @return 使能返回true 否则返回false
	 * 
	 */
	public boolean isDrawAccuracyEnabled() {
		return this.drawAccuracyEnabled;
	}

	@Override
	public void draw(final Canvas canvas, final MapView mapView,
			final boolean shadow) {// 实现Overlay的draw函数，绘制用户当前的位置
		if (shadow) {
			return;
		}
		// 获得用户位置对应的屏幕像素坐标
		// Projection projection = mapView.getProjection();
		Point screenPixelLocation = null;
		screenPixelLocation = TileSystem.MapXYToMapPixels(myCurrentLocation,
				mapView.getZoomLevel());

		// 绘制精度圆
		if (drawAccuracyEnabled) {
			final float radius = (float) 7;// =
			// pj.metersToEquatorPixels(lastFix.getAccuracy());
			circlePaint.setAlpha(20);
			// circlePaint.setStyle(Style.FILL);
			circlePaint.setStyle(Style.FILL_AND_STROKE);
			circlePaint.setColor(Color.RED);
			canvas.drawCircle(screenPixelLocation.x, screenPixelLocation.y,
					radius, circlePaint);

			circlePaint.setColor(Color.BLUE);
			circlePaint.setAlpha(50);
			circlePaint.setStyle(Style.FILL_AND_STROKE);
			canvas.drawCircle(screenPixelLocation.x, screenPixelLocation.y,
					radius + 20, circlePaint);
		}
		// 绘制人员的位置
		  canvas.getMatrix(matrix);
		 
		 matrix.getValues(matrixValues); 

	}

	/**
	 * 获得用户当前位置
	 * 
	 * @return 用户当前位置
	 */
	public TwoDCoordinate getMyLocation() {
		return myCurrentLocation;// 返回当前的位置
	}

	/**
	 * 设置用户当前位置
	 * 
	 * @param location
	 */
	public void setMyLocation(TwoDCoordinate location) {// 设置当前的位置，会触发重绘。
		myCurrentLocation = location;
	}

	/**
	 * 
	 * @param location
	 */
	public void setMyLocation(double x, double y) {// 设置当前的位置，会触发重绘。

		myCurrentLocation = new TwoDCoordinate(x, y);
	}

	/**
	 * 
	 * @param e
	 * @param mapView
	 * @return
	 */
	public boolean onLongPress(final MotionEvent e, final MapView mapView) {
		return false;
		// 响应长按事件
	}

	/**
	 * 
	 * @param e
	 * @param mapView
	 * @return
	 */
	public boolean onDown(final MotionEvent e, final MapView mapView) { // 响应down事件
		return false;
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event, final MapView mapView) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			disableFollow();
		}

		return super.onTouchEvent(event, mapView);

	}

	@Override
	public void locationChanged(TwoDCoordinate coor) {
		// TODO Auto-generated method stub
		if (coor == null)
			return;
		this.myCurrentLocation = coor;
		if (boolFollow) {
			observer.notifyFollow(myCurrentLocation);
		} else {
			observer.letInvalidate();// 重绘用户的位置
		}
	}

	@Override
	public void firepointchanged(TwoDCoordinate coordinate) {
		// TODO Auto-generated method stub
		
	}
}
