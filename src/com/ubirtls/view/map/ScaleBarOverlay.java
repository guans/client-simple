package com.ubirtls.view.map;

import java.lang.reflect.Field;

import coordinate.TwoDCoordinate;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.Paint.Style;

public class ScaleBarOverlay extends Overlay {
	/**
	 * ������Overlay�������Ļ���Ͻ������xƫ��ֵ
	 */
	private float xOffset = 10;
	/**
	 * ������Overlay�������Ļ���Ͻ������yƫ��ֵ
	 */
	private float yOffset = 680;
	/**
	 * �߿�
	 */
	float lineWidth = 2;
	/**
	 * ���������ֵ������С
	 */
	final int textSize = 12;
	/**
	 * ��С���ż���
	 */
	int minZoom = 0;
	/**
	 * ������������Activity
	 */
	private final Activity activity;
	/**
	 * ���Ʊ����ߵ�Picture
	 */
	protected final Picture scaleBarPicture = new Picture();
	/**
	 * ��Ļx����1 inch��Ӧ������ֵ
	 */
	public float xdpi;
	/**
	 * ��Ļx����1 inch��Ӧ������ֵ
	 */
	public float ydpi;
	private int meters = 0;
	/**
	 * �Ƿ����ˮƽ������
	 */
	boolean latitudeBar = true;
	/**
	 * �Ƿ���ƴ�ֱ������
	 */
	boolean longitudeBar = false;

	/**
	 * ��Ļ��
	 */
	public int screenWidth;
	/**
	 * ��Ļ��
	 */
	public int screenHeight;
	/**
	 * ���Ʊ���������Paint
	 */
	private Paint barPaint;
	/**
	 * �����ı���Paint
	 */
	private Paint textPaint;
	/**
	 * ӳ����
	 */
	private Projection projection;

	final private Rect mBounds = new Rect();

	public ScaleBarOverlay(Activity activity) {
		super(activity);

		this.activity = activity;

		this.barPaint = new Paint();
		this.barPaint.setColor(Color.BLACK);
		this.barPaint.setAntiAlias(true);
		this.barPaint.setStyle(Style.FILL);
		this.barPaint.setAlpha(255);

		this.textPaint = new Paint();
		this.textPaint.setColor(Color.BLACK);
		this.textPaint.setAntiAlias(true);
		this.textPaint.setStyle(Style.FILL);
		this.textPaint.setAlpha(255);
		this.textPaint.setTextSize(textSize);

		this.xdpi = this.activity.getResources().getDisplayMetrics().xdpi;
		this.ydpi = this.activity.getResources().getDisplayMetrics().ydpi;

		this.screenWidth = this.activity.getResources().getDisplayMetrics().widthPixels;
		this.screenHeight = this.activity.getResources().getDisplayMetrics().heightPixels;

		// DPI corrections for specific models
		String manufacturer = null;
		try {
			final Field field = android.os.Build.class.getField("MANUFACTURER");
			manufacturer = (String) field.get(null);
		} catch (final Exception ignore) {
		}

		if ("motorola".equals(manufacturer)
				&& "DROIDX".equals(android.os.Build.MODEL)) {

			// If the screen is rotated, flip the x and y dpi values
			if (activity.getWindowManager().getDefaultDisplay()
					.getOrientation() > 0) {
				this.xdpi = (float) (this.screenWidth / 3.75);
				this.ydpi = (float) (this.screenHeight / 2.1);
			} else {
				this.xdpi = (float) (this.screenWidth / 2.1);
				this.ydpi = (float) (this.screenHeight / 3.75);
			}

		} else if ("motorola".equals(manufacturer)
				&& "Droid".equals(android.os.Build.MODEL)) {
			// http://www.mail-archive.com/android-developers@googlegroups.com/msg109497.html
			this.xdpi = 264;
			this.ydpi = 264;
		}

		// TODO Auto-generated constructor stub
	}

	/**
	 * ���ñ�����λ���������Ļ���Ͻ������xƫ��ֵ
	 * 
	 * @param x x����ƫ��ֵ
	 * @param y y����ƫ��ֵ
	 */
	public void setScaleBarOffset(final float x, final float y) {
		xOffset = x;
		yOffset = y;
	}

	/**
	 * �����߿�
	 * 
	 * @param width int �߿�
	 */
	public void setLineWidth(final float width) {
		this.lineWidth = width;
	}

	/**
	 * �����ı��������С
	 * 
	 * @param size
	 */
	public void setTextSize(final float size) {
		this.textPaint.setTextSize(size);
	}

	/**
	 * ��ñ�����������
	 * 
	 * @return Paint����
	 */
	public Paint getBarPaint() {
		return barPaint;
	}

	/**
	 * ���ñ�����������
	 * 
	 * @param pBarPaint Paint����
	 */
	public void setBarPaint(final Paint pBarPaint) {
		if (pBarPaint == null) {
			throw new IllegalArgumentException(
					"pBarPaint argument cannot be null");
		}
		barPaint = pBarPaint;
	}

	/**
	 * ����ı�����
	 * 
	 * @return Paint����
	 */
	public Paint getTextPaint() {
		return textPaint;
	}

	/**
	 * �����ı�����
	 * 
	 * @param pTextPaint Paint����
	 */
	public void setTextPaint(final Paint pTextPaint) {
		if (pTextPaint == null) {
			throw new IllegalArgumentException(
					"pTextPaint argument cannot be null");
		}
		textPaint = pTextPaint;
	}

	@Override
	protected void draw(Canvas c, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		if (shadow) {
			return;
		}

		// If map view is animating, don't update, scale will be wrong.
		if (mapView.isAnimating()) {
			return;
		}

		final int zoomLevel = mapView.getZoomLevel();

		if (zoomLevel >= minZoom) {
			final Projection projection = mapView.getProjection();

			if (projection == null) {
				return;
			}

			final TwoDCoordinate center = projection.fromScreenPixels(
					(screenWidth / 2), screenHeight / 2, zoomLevel);
			createScaleBarPicture(mapView);
			/*
			 * if (zoomLevel != lastZoomLevel || (int) (center.getLatitudeE6() /
			 * 1E6) != (int) (lastLatitude / 1E6)) { lastZoomLevel = zoomLevel;
			 * lastLatitude = center.getLatitudeE6();
			 * createScaleBarPicture(mapView); }
			 */
			mBounds.set(projection.getScreenRect(null));
			mBounds.offset((int) xOffset, (int) yOffset);

			mBounds.set(mBounds.left, mBounds.top, mBounds.left
					+ scaleBarPicture.getWidth(), mBounds.top
					+ scaleBarPicture.getHeight());
			c.drawPicture(scaleBarPicture, mBounds);
		}

	}

	/**
	 * ��ֹ���Ʊ�����
	 */
	public void disableScaleBar() {
		setEnabled(false);
	}

	/**
	 * ʹ�ܻ��Ʊ�����
	 */
	public void enableScaleBar() {
		setEnabled(true);
	}

	/**
	 * ������������Picture
	 * 
	 * @param mapView MapView����
	 */
	private void createScaleBarPicture(final MapView mapView) {
		// We want the scale bar to be as long as the closest round-number
		// miles/kilometers
		// to 1-inch at the latitude at the current center of the screen.

		projection = mapView.getProjection();

		if (projection == null) {
			return;
		}

		// Two points, 1-inch apart in x/latitude, centered on screen
		TwoDCoordinate p1 = projection.fromScreenPixels((screenWidth / 2)
				- (xdpi / 2), screenHeight / 2, mapView.getZoomLevel());
		TwoDCoordinate p2 = projection.fromScreenPixels((screenWidth / 2)
				+ (xdpi / 2), screenHeight / 2, mapView.getZoomLevel());
		final int xMetersPerCentiMeter;
		// p2�ڵ�һ��ͼƬ��p1�����һ��ͼƬ
		if (p1.getXCoordinate() > p2.getXCoordinate()) {
			xMetersPerCentiMeter = meters;

		} else {
			xMetersPerCentiMeter = (int) (p1.distanceFrom(p2) / 2.54);
			meters = xMetersPerCentiMeter;
		}

		p1 = projection.fromScreenPixels(screenWidth / 2, (screenHeight / 2)
				- (ydpi / 2), mapView.getZoomLevel());
		p2 = projection.fromScreenPixels(screenWidth / 2, (screenHeight / 2)
				+ (ydpi / 2), mapView.getZoomLevel());

		final int yMetersPerCentiMeter = (int) (p1.distanceFrom(p2) / 2.54);

		final Canvas canvas = scaleBarPicture.beginRecording((int) xdpi,
				(int) ydpi);

		if (latitudeBar) {
			final String xMsg = scaleBarLengthText(xMetersPerCentiMeter);
			final Rect xTextRect = new Rect();
			textPaint.getTextBounds(xMsg, 0, xMsg.length(), xTextRect);

			final int textSpacing = (int) (xTextRect.height() / 5.0);

			canvas.drawRect(0, 0, xdpi, lineWidth, barPaint);
			canvas.drawRect(xdpi, 0, xdpi + lineWidth, xTextRect.height()
					+ lineWidth + textSpacing, barPaint);

			if (!longitudeBar) {
				canvas.drawRect(0, 0, lineWidth, xTextRect.height() + lineWidth
						+ textSpacing, barPaint);
			}

			canvas.drawText(xMsg, xdpi / 2 - xTextRect.width() / 2, xTextRect
					.height()
					+ lineWidth + textSpacing, textPaint);
		}

		if (longitudeBar) {
			final String yMsg = scaleBarLengthText(yMetersPerCentiMeter);
			final Rect yTextRect = new Rect();
			textPaint.getTextBounds(yMsg, 0, yMsg.length(), yTextRect);

			final int textSpacing = (int) (yTextRect.height() / 5.0);

			canvas.drawRect(0, 0, lineWidth, ydpi, barPaint);
			canvas.drawRect(0, ydpi, yTextRect.height() + lineWidth
					+ textSpacing, ydpi + lineWidth, barPaint);

			if (!latitudeBar) {
				canvas.drawRect(0, 0, yTextRect.height() + lineWidth
						+ textSpacing, lineWidth, barPaint);
			}

			final float x = yTextRect.height() + lineWidth + textSpacing;
			final float y = ydpi / 2 + yTextRect.width() / 2;

			canvas.rotate(-90, x, y);
			canvas.drawText(yMsg, x, y + textSpacing, textPaint);

		}

		scaleBarPicture.endRecording();
	}

	/**
	 * ��ȡ��������ʾ�ı�
	 * 
	 * @param meters �����ߴ�С ��λΪm
	 * @return �������ı�����
	 */
	private String scaleBarLengthText(final int meters) {
		if (meters >= 1000) {
			return String.valueOf(meters / 1000) + "km";
		} else {
			return String.valueOf(meters) + "m";
		}
	}
}
