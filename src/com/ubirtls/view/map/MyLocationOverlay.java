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
 * ��ʾ�û�λ�õĸ��ǲ�
 * 
 * @author �����
 * @version 0.1
 */
public class MyLocationOverlay extends Overlay implements
		LocationChangeListener {
	/**
	 * ����������Աλ�õĻ���
	 */
	protected final Paint paint = new Paint();
	/**
	 * �������ƾ���Բ�Ļ���
	 */
	protected final Paint circlePaint = new Paint();
	/**
	 * ������ʾ��Աλ�õ�ͼ��
	 */
	protected final Bitmap personIcon;

	/**
	 * ��boolFollowΪtrueʱ���������Ƶ�ͼ�����û���λ�ã������û� ��λ�����ڵ�ͼ�����ġ�
	 */
	protected boolean boolFollow = true;
	/**
	 * ���ƾ���ʹ��
	 */
	protected boolean drawAccuracyEnabled = true;
	/**
	 * �û���ǰ������λ��
	 */
	private TwoDCoordinate myCurrentLocation = null;
	/**
	 * ��Ա�ȵ�
	 */
	protected final PointF PERSON_HOTSPOT;

	/**
	 * ������drawʱ����
	 */
	private final float[] matrixValues = new float[9];
	private final Matrix matrix = new Matrix();
	/**
	 * �۲��ߣ�������MapView
	 */
	private Observer observer;

	/**
	 * ���캯��
	 * 
	 * @param context Context����
	 * @param mapView MapView����
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
	 * �Ƿ�����û���λ��
	 * 
	 * @return �����򷵻�true ���򷵻�false
	 */
	public boolean isFollow() {
		return boolFollow;
	}

	/**
	 * ��ֹ��ͼ�����û���λ��
	 */
	public void disableFollow() {
		boolFollow = false;
	}

	/**
	 * ʹ�ܵ�ͼ׷���û���λ�á�
	 */
	public void enableFollow() {
		boolFollow = true;
		if ((myCurrentLocation != null) && (observer != null)) {
			observer.notifyFollow(myCurrentLocation);
		} else {
			/**
			 * ��Setting����ȡ�û���λ����Ϣ
			 */
		}
		/*
		 * // ������Ļ if (observer != null) { observer.letInvalidate(); }
		 */}

	/**
	 * ���ʹ�� �������û���ǰ��λ�û���һ������Բ
	 * 
	 * @param drawAccuracyEnabled ʹ����Ϊtrue ����Ϊfalse
	 */
	public void setDrawAccuracyEnabled(final boolean drawAccuracyEnabled) {
		this.drawAccuracyEnabled = drawAccuracyEnabled;
	}

	/**
	 * ���ʹ�� �������û���ǰ��λ�û���һ������Բ
	 * 
	 * @return ʹ�ܷ���true ���򷵻�false
	 * 
	 */
	public boolean isDrawAccuracyEnabled() {
		return this.drawAccuracyEnabled;
	}

	@Override
	public void draw(final Canvas canvas, final MapView mapView,
			final boolean shadow) {// ʵ��Overlay��draw�����������û���ǰ��λ��
		if (shadow) {
			return;
		}
		// ����û�λ�ö�Ӧ����Ļ��������
		// Projection projection = mapView.getProjection();
		Point screenPixelLocation = null;
		screenPixelLocation = TileSystem.MapXYToMapPixels(myCurrentLocation,
				mapView.getZoomLevel());

		// ���ƾ���Բ
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
		// ������Ա��λ��
		  canvas.getMatrix(matrix);
		 
		 matrix.getValues(matrixValues); 

	}

	/**
	 * ����û���ǰλ��
	 * 
	 * @return �û���ǰλ��
	 */
	public TwoDCoordinate getMyLocation() {
		return myCurrentLocation;// ���ص�ǰ��λ��
	}

	/**
	 * �����û���ǰλ��
	 * 
	 * @param location
	 */
	public void setMyLocation(TwoDCoordinate location) {// ���õ�ǰ��λ�ã��ᴥ���ػ档
		myCurrentLocation = location;
	}

	/**
	 * 
	 * @param location
	 */
	public void setMyLocation(double x, double y) {// ���õ�ǰ��λ�ã��ᴥ���ػ档

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
		// ��Ӧ�����¼�
	}

	/**
	 * 
	 * @param e
	 * @param mapView
	 * @return
	 */
	public boolean onDown(final MotionEvent e, final MapView mapView) { // ��Ӧdown�¼�
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
			observer.letInvalidate();// �ػ��û���λ��
		}
	}

	@Override
	public void firepointchanged(TwoDCoordinate coordinate) {
		// TODO Auto-generated method stub
		
	}
}
