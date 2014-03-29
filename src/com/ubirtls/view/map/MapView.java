package com.ubirtls.view.map;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.ubirtls.Controller;
import com.ubirtls.Observer;
import com.ubirtls.view.Activity.MapActivity;
import com.ubirtls.view.map.ZoomButtonsController.OnZoomListener;
import com.ubirtls.util.SimpleInvalidationHandler;

import coordinate.TwoDCoordinate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Scroller;
import android.widget.Toast;

public class MapView extends ViewGroup implements Observer {
	/**
	 * ��ͼ����ϸ��
	 */
	public int zoomLevel = 4;
	/**
	 * �����������ײ�Ϊ��ͼ��֮�ϻ�������������
	 */
	private OverlayManager overlayManager;
	/**
	 * ������ ��Ҫ����Ƭ��ͼ����ʸ����ͼ
	 */
	private Overlay backgroundOverlay = null;
	/**
	 * ӳ���࣬������Ļ��ߺ͵�ͼ������໥ӳ��
	 */
	private Projection projection;
	/**
	 * ����������Ļ���ƣ����϶���
	 */
	private final GestureDetector gestureDetector;
	/**
	 * �������Ƶ�ͼ�����ŵȲ�����
	 */
	private final MapController controller;
	/**
	 * ��Ļ������
	 */
	private final Scroller scroller;
	/**
	 * �Զ��ı䣬����zoomlevel
	 */
	private final AtomicInteger targetZoomLevel = new AtomicInteger();
	/**
	 * �Զ��ı䣬���ƶ����Ƿ����ڽ���
	 */
	private final AtomicBoolean boolIsAnimating = new AtomicBoolean(false);
	/**
	 * �Ŵ󶯻�
	 */
	private final ScaleAnimation zoomInAnimation;
	/**
	 * ��С����
	 */
	private final ScaleAnimation zoomOutAnimation;
	/**
	 * ���ſؼ�controller
	 */
	private ZoomButtonsController zoomButtonController;
	/**
	 * �Ƿ�ʹ�����ſؼ�
	 */
	private boolean boolEnableZoocontroller = false;
	/**
	 * ���ڴ���map����
	 */
	private Handler handler;
	private Context context;
	/***/
	private int flag=-1;
	private TwoDCoordinate coor;
	/**
	 * ���캯��
	 * 
	 * @param context
	 * @param tileSizePixels
	 * @param tileRequestCompleteHandler
	 * @param attrs
	 * @param widthTileNumber
	 * @param heightTileNumber
	 */
	public MapView(final Context context,
			final Handler tileRequestCompleteHandler, final AttributeSet attrs) {

		super(context, attrs);
		this.context=context;
		
		/**
		 * handler ����
		 */
		if (tileRequestCompleteHandler == null)
			this.handler = new SimpleInvalidationHandler(this);
		else
			this.handler = tileRequestCompleteHandler;
		/**
		 * tilesoverlay ѡ��FIleProvider
		 */
		overlayManager = new OverlayManager(null);
		this.backgroundOverlay = new TilesOverlay(context,
				tileRequestCompleteHandler);
		this.overlayManager.setBackgroundOverlay(backgroundOverlay);
		/**
		 * controller ��ʼ��
		 */
		this.controller = new MapController(this);
		// scroller��ʼ��
		this.scroller = new Scroller(context);

		/**
		 * ��ʼ��gestureDetector
		 */
		this.gestureDetector = new GestureDetector(context,
				new MapViewGestureDetectorListener());
		this.gestureDetector
				.setOnDoubleTapListener(new MapViewDoubleClickListener());
		/**
		 * ��ʼ��zoomButton�ؼ�
		 */

		this.zoomButtonController = new ZoomButtonsController(this);
		zoomButtonController.setOnZoomListener(new MapViewZoomListener());
		/**
		 * ��ʼ�����Ŷ���
		 */
		zoomInAnimation = new ScaleAnimation(1, 2, 1, 2,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		zoomOutAnimation = new ScaleAnimation(1, 0.5f, 1, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		zoomInAnimation.setDuration(500);
		zoomOutAnimation.setDuration(500);
		Controller.getInstance().setInvalidateObserver(this);

	}

	/**
	 * ��ȡmapController
	 * 
	 * @return ����controller
	 */
	public MapController getController() {
		if (this.controller != null)
			return this.controller;
		else
			return new MapController(this);
	}

	/**
	 * ��ȡOverlayManager
	 * 
	 * @return
	 */
	public OverlayManager getOverlayManager() {
		return this.overlayManager;
	}

	/**
	 * ��ȡ��Ļscroller
	 * 
	 * @return
	 */
	public Scroller getScroller() {
		return this.scroller;
	}

	/**
	 * ��ȡmap����Ĵ���handle
	 */
	public Handler getHandler() {
		return this.handler;
	}

	/**
	 * ��ȡ��Ļ���Σ�����ڵ�ͼ������˵
	 * 
	 * @param reuse
	 * @return
	 */
	public Rect getScreenRect(final Rect reuse) {
		final Rect out = reuse == null ? new Rect() : reuse;
		out.set(getScrollX(), getScrollY(), getScrollX() + getWidth(),
				getScrollY() + getHeight());
		return out;
	}
	
	/**
	 * ���projection
	 * 
	 * @return
	 */
	public Projection getProjection() {
		if (this.projection == null)
			this.projection = new Projection(this);
		return this.projection;
	}

	/**
	 * ���õ�ͼ������
	 * 
	 * @param center ���ĵ�����
	 */
	public void setMapCenter(final TwoDCoordinate center) {
		Point pixel = TileSystem.MapXYToMapPixels(center, this.getZoomLevel());
		setMapCenter(pixel.x, pixel.y);
/*		Log.i("center", new Integer(pixel.x).toString() + " "
				+ new Integer(pixel.y).toString());
		Log.i("center", new Double(center.getXCoordinate()).toString() + " "
				+ new Double(center.getYCoordinate()).toString());*/

	}

	/**
	 * ���õ�ͼ������
	 * 
	 * @param pixelX ��ͼ����X����
	 * @param pixelY ��ͼ����Y����
	 */
	void setMapCenter(int pixelX, int pixelY) {

		if (getAnimation() == null || getAnimation().hasEnded()) {
			scroller.startScroll(getScrollX(), getScrollY(), pixelX
					- getScrollX() - this.getWidth() / 2, pixelY - getScrollY()
					- this.getHeight() / 2, 500);
		}
	}

	/**
	 * ���õ�ͼ������ϸ��
	 * 
	 * @param aZoomLevel
	 * @return
	 */
	int setZoomLevel(final int aZoomLevel) {
		final int minZoomLevel = getMinZoomLevel();
		final int maxZoomLevel = getMaxZoomLevel();
		/**
		 * ��ȡ��newZoomLevel�����minZoomLevel��maxZoomLevel֮��
		 * ��ΪaZoomLevel������ΪminZoomLevel��maxZoomLevel
		 */
		final int newZoomLevel = Math.max(minZoomLevel, Math.min(maxZoomLevel,
				aZoomLevel));
		final int curZoomLevel = this.zoomLevel;

		this.zoomLevel = newZoomLevel;
		this.checkZoomButtons();

		/**
		 * �ͷֱ��ʵ��߷ֱ���
		 */
		if (newZoomLevel > curZoomLevel) {
			// ��ǰ��ͼ��width
/*			int worldSize_current_W = 0;
			// ��ǰ��ͼ��height
			int worldSize_current_H = 0;
			// �µĵ�ͼwidth
			int worldSize_new_W = 0;
			// �µĵ�ͼheight
			int worldSize_new_H = 0;

			worldSize_current_W = TileSystem.MapWidthSize(curZoomLevel);
			worldSize_current_H = TileSystem.MapHeightSize(curZoomLevel);
			worldSize_new_W = TileSystem.MapWidthSize(newZoomLevel);
			worldSize_new_H = TileSystem.MapHeightSize(newZoomLevel);*/

			final TwoDCoordinate centerCoorPoint = TileSystem.MapPixelsToMapXY(
					getScrollX() + getWidth()/ 2, getScrollY()
							+ getHeight() / 2, curZoomLevel);
			final Point centerPoint = TileSystem.MapXYToMapPixels(
					centerCoorPoint, newZoomLevel);

			scrollTo(centerPoint.x-this.getWidth() /2, centerPoint.y - this.getHeight() / 2);
/*			scroller.startScroll(getScrollX(), getScrollY(), centerPoint.x
					- getScrollX() - this.getWidth() / 2, centerPoint.y - getScrollY()
					- this.getHeight() / 2, 500);*/

		} else if (newZoomLevel < curZoomLevel) {
			// We are going from a higher-resolution plane to a lower-resolution
			// plane, so we can do
			// it the easy way.
/*			scrollTo(getScrollX() >> (curZoomLevel - newZoomLevel),
					getScrollY() >> (curZoomLevel - newZoomLevel));*/
			
			final TwoDCoordinate centerCoorPoint = TileSystem.MapPixelsToMapXY(
					getScrollX() + getWidth()/ 2, getScrollY()
							+ getHeight() / 2, curZoomLevel);
			final Point centerPoint = TileSystem.MapXYToMapPixels(
					centerCoorPoint, newZoomLevel);

			scrollTo(centerPoint.x- this.getWidth() / 2, centerPoint.y - this.getHeight() / 2);
/*			scroller.startScroll(getScrollX(), getScrollY(), centerPoint.x
					- getScrollX() - this.getWidth() / 2, centerPoint.y - getScrollY()
					- this.getHeight() / 2, 500);*/
		}

		return this.zoomLevel;

	}

	/**
	 * ��õ�ǰ��ͼ������ϸ��
	 * 
	 * @return �����ǰ���ڽ������ŷ���targetZoomLevel ���򷵻� zoomLevel
	 */
	public int getZoomLevel() {
		// ���ڽ�������
		if (this.boolIsAnimating.get())
			return this.targetZoomLevel.get();
		return this.zoomLevel;
	}

	/**
	 * ������С������ϸ��
	 * 
	 * @return
	 */
	public int getMinZoomLevel() {
		return TileSystem.MIN_LEVEL;
	}

	/**
	 * ������������ϸ��
	 * 
	 * @return
	 */
	public int getMaxZoomLevel() {
		return TileSystem.MAX_LEVEL;
	}

	/**
	 * �жϵ�ͼ�Ƿ����ٷŴ�
	 * 
	 * @return
	 */
	public boolean canZoomIn() {
		final int maxZoomLevel = getMaxZoomLevel();
		// �����ǰzoomLevel�����zoomLevel�����򲻿����ٷŴ�
		if (zoomLevel >= maxZoomLevel) {
			return false;
		}
		// �����ǰ���ڽ��ж��� Ȼ��targetZoomLevel�����zoomLevel�����򲻿����ٷŴ�
		if (boolIsAnimating.get() & targetZoomLevel.get() >= maxZoomLevel) {
			return false;
		}
		return true;
	}

	/**
	 * �жϵ�ͼ�Ƿ�������С
	 * 
	 * @return
	 */
	public boolean canZoomOut() {
		final int minZoomLevel = getMinZoomLevel();
		// �����ǰzoomLevel����СzoomLevelС����򲻿�������С
		if (zoomLevel <= minZoomLevel) {
			return false;
		}
		// �����ǰ���ڽ��ж��� Ȼ��targetZoomLevel����СzoomLevelС����򲻿�������С
		if (boolIsAnimating.get() & targetZoomLevel.get() <= minZoomLevel) {
			return false;
		}
		return true;
	}

	/**
	 * �û�����Ŵ�ť�Լ�˫����Ļʱ������øú�����
	 * �ú�����Ҫ�������Ŵ�Ķ��������ı�zoomLevel��ֵ��ʵ�ʵ���������setZoomLevel��ʵ�ֵġ�
	 * 
	 * @return ���ܽ�������(��������Ϊ��ǰ�������Ŷ������ڽ��� ���߷Ŵ󼶱�ﵽ���)
	 */
	boolean zoomIn() {
		if (canZoomIn()) {
			// ��ǰ���ڽ��ж���
			if (boolIsAnimating.get()) {
				// TODO extend zoom (and return true)
				return false;
			} else {
				// level��һ �Ŵ�һ��
				targetZoomLevel.set(zoomLevel + 1);
				// ���õ�ǰ������������Ϊtrue
				boolIsAnimating.set(true);
				//this.setZoomLevel(targetZoomLevel.get());
				// �����Ŵ󶯻� �Ŵ󶯻�����������ʵ�ʵĵ�ͼ����
				startAnimation(zoomInAnimation);
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * ˫����ͼʱ����øú����������õ���ĵ�ͼ��Ϊ��Ļ�����ĵ㣬Ȼ���ٽ������ţ����ú���zoomIn()
	 * 
	 * @param point
	 * @return
	 */
	boolean zoomInFixing(final TwoDCoordinate point) {
		setMapCenter(point);
		return zoomIn();
	}

	/**
	 * ˫����ͼʱ����øú����������õ���ĵ�ͼ��Ϊ��Ļ�����ĵ㣬Ȼ���ٽ������ţ����ú���zoomIn()
	 * 
	 * @param pixelX ��ͼ����X����
	 * @param pixelY ��ͼ����Y����
	 * @return ���ųɹ�����true ���򷵻�false
	 */
	boolean zoomInFixing(int pixelX, int pixelY) {
		setMapCenter(pixelX, pixelY);
		return zoomIn();
	}

	/**
	 * �û������С��ť�Լ�˫����Ļʱ������øú������ú�����Ҫ��������С�Ķ�����
	 * ���ı�zoomLevel��ֵ��ʵ�ʵ���������setZoomLevel��ʵ�ֵġ�
	 * 
	 * @return
	 */
	boolean zoomOut() {
		if (canZoomOut()) {
			// ��ǰ���ڽ��ж���
			if (boolIsAnimating.get()) {
				// TODO extend zoom (and return true)
				return false;
			} else {
				// level��һ��Сһ��
				targetZoomLevel.set(zoomLevel - 1);
				// ���õ�ǰ������������Ϊtrue
				boolIsAnimating.set(true);
				// ������С���� ��С��������������ʵ�ʵĵ�ͼ����
				startAnimation(zoomOutAnimation);
				//this.setZoomLevel(targetZoomLevel.get());
				return true;
			}
		} else {
			return false;
		}

	}

	/**
	 * �����õ���ĵ�ͼ��Ϊ��Ļ�����ĵ㣬Ȼ���ٽ������ţ����ú���zoomOut()
	 * 
	 * @param point
	 * @return
	 */
	boolean zoomOutFixing(final TwoDCoordinate point) {
		return false;
	}

	/**
	 * ��ȡ��ǰ��ͼ����Ļ�ϵ����ĵ�
	 * 
	 * @return
	 */
	public TwoDCoordinate getMapCenter() {
		Rect rect = this.getScreenRect(null);
		return TileSystem.MapPixelsToMapXY(rect.left, rect.top, this
				.getZoomLevel());
	}

	/**
	 * ���ݵ�ǰzoomLevel�ж��Ƿ�ʹ�ܻ�������ſؼ�
	 */
	private void checkZoomButtons() {
		this.zoomButtonController.setZoomInEnabled(canZoomIn());
		this.zoomButtonController.setZoomOutEnabled(canZoomOut());

	}

	/**
	 * �������Ű�ť�Ƿ���ʾ��true��ʾ��������ʾ��
	 * 
	 * @param on
	 */
	public void setBuiltInZoomControls(final boolean on) {
		this.boolEnableZoocontroller = on;
		this.checkZoomButtons();
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			if (scroller.isFinished()) {
				// This will facilitate snapping-to any Snappable points.
				setZoomLevel(zoomLevel);
			} else {
				scrollTo(scroller.getCurrX(), scroller.getCurrY());
			}
			postInvalidate(); // Keep on drawing until the animation has
			// finished.
		}
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
	}

	@Override
	public void onScrollChanged(int x1, int y1, int x2, int y2) {
		super.onScrollChanged(x1, y1, x2, y2);
	}

	@Override
	protected void dispatchDraw(final Canvas c) {
		projection = new Projection(this);

		// Save the current canvas matrix
		c.save();
		// c.translate(getWidth() / 2, getHeight() / 2);

		/* Draw background */
		// c.drawColor(mBackgroundColor);

		/* Draw all Overlays. */
		this.getOverlayManager().onDraw(c, this);

		// Restore the canvas matrix
		c.restore();

		super.dispatchDraw(c);

	}

	@Override
	protected void onAnimationStart() {
		boolIsAnimating.set(true);
		super.onAnimationStart();
	}// �ص����� ������ʼʱ����øú���

	@Override
	protected void onAnimationEnd() {
		boolIsAnimating.set(false);
		clearAnimation();
		setZoomLevel(targetZoomLevel.get());
		super.onAnimationEnd();
	}// �ص���������������ʱ����øú���

	/**
	 * �жϵ�ǰ�Ƿ����ڲ��Ŷ���
	 * 
	 * @return ������ڲ��Ŷ�������true ���򷵻�false
	 */
	public boolean isAnimating() {
		return boolIsAnimating.get();
	}

	/**
	 * ��onDetachedFromWindow��������
	 */
	public void onDetach() {
		this.getOverlayManager().onDetach(this);
	}
	public int get_flag()
	{
		return flag;
	}
	public TwoDCoordinate get_location()
	{
		flag=-1;
		return coor;
	}
	@Override
	protected void onDetachedFromWindow() {
		this.zoomButtonController.setVisible(false);
		this.onDetach();
		super.onDetachedFromWindow();

	}// �ص������������ڸ��ڴ��ڹ�������ʱ����øú���

	/**
	 * ˫���¼��ļ�����
	 * 
	 * @author �����
	 * @version 0.1
	 */
	private class MapViewDoubleClickListener implements
			GestureDetector.OnDoubleTapListener {

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// TODO Auto-generated method stub
			if (MapView.this.getOverlayManager().onDoubleTap(e, MapView.this)) {
				return true;
			}
			final Rect screenRect = getScreenRect(null);
			return zoomInFixing(screenRect.left + (int) e.getX(),
					screenRect.top + (int) e.getY());
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	/**
	 * �����¼��ļ����� �������¼�����ʱ�ᷢ��֪ͨ
	 * 
	 * @author �����
	 * @version 0.1
	 */
	private class MapViewGestureDetectorListener implements OnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			zoomButtonController.setVisible(boolEnableZoocontroller);
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			int mapSizeX = 0;
			int mapSizeY = 0;
			mapSizeX = TileSystem.MapWidthSize(getZoomLevel());
			mapSizeY = TileSystem.MapHeightSize(getZoomLevel());
			scroller.fling(getScrollX(), getScrollY(), (int) -velocityX  ,
					(int) -velocityY , -5*mapSizeX , 5*mapSizeX ,
					-5 * mapSizeY , 5 * mapSizeY );
			// invalidate();
			return true;
		}
		
		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			coor=getProjection().fromScreenPixels(
					e.getX(), e.getY(), getZoomLevel());
			
			MapView.this.getOverlayManager().onLongPress(e, MapView.this,coor,flag);
			//Toast.makeText(context, flag+"", Toast.LENGTH_SHORT).show();
			flag=0;
 		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
/*			Log.i("s", "scroller");
			Log.i("scrollerx", new Integer(getScrollX()).toString());
			Log.i("scrollery", new Integer(getScrollY()).toString());
			Log.i("distancex", new Integer((int) distanceX).toString());
			Log.i("distancey", new Integer((int) distanceY).toString());*/
			/*
			 * scroller.startScroll(getScrollX(), getScrollY(), (int) distanceX,
			 * (int) distanceY, 500);
			 */
			int dx =(int) distanceX;
			int dy =(int) distanceY;
			int mapSizeX = 0;
			int mapSizeY = 0;
			mapSizeX = TileSystem.MapWidthSize(getZoomLevel());
			mapSizeY = TileSystem.MapHeightSize(getZoomLevel());
			if(MapView.this.getScrollX() + dx > mapSizeX)
				dx = mapSizeX - MapView.this.getScrollX();
			if(MapView.this.getScrollY() + dy > mapSizeY)
				dy = mapSizeY - MapView.this.getScrollY();
			scrollBy(dx, (int) dy);
		//	invalidate();
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	/**
	 * zoombutton�ؼ��ļ�����
	 * 
	 * @author �����
	 * @version 0.1
	 */
	private class MapViewZoomListener implements OnZoomListener {

		@Override
		public void onVisibilityChanged(boolean visible) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onZoom(boolean zoomIn) {
			// TODO Auto-generated method stub
			if (zoomIn) {
				MapView.this.zoomIn();
			} else {
				MapView.this.zoomOut();
			}
		}
	}

	@Override
	public boolean onTrackballEvent(final MotionEvent event) {

		scrollBy((int) (event.getX() * 25), (int) (event.getY() * 25));

		return super.onTrackballEvent(event);
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean dispatchTouchEvent(final MotionEvent event) {

		if (zoomButtonController.isVisible()
				&& zoomButtonController.onTouch(this, event)) {
			return true;
		}

		if (this.getOverlayManager().onTouchEvent(event, this)) {
			return true;
		}

		final boolean r = super.dispatchTouchEvent(event);
		boolean e1 = r;
		if (gestureDetector.onTouchEvent(event)) {
			return true;
		}
		return r;

	}

	@Override
	public void letInvalidate() {
		// TODO Auto-generated method stub
		postInvalidate();
	}

	@Override
	public void notifyFollow(TwoDCoordinate coordinate) {
		// TODO Auto-generated method stub
		controller.animateTo(coordinate);
	}
}
