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
	 * 地图缩放细节
	 */
	public int zoomLevel = 4;
	/**
	 * 覆盖物管理，最底层为地图，之上还有其他覆盖物
	 */
	private OverlayManager overlayManager;
	/**
	 * 背景层 主要是瓦片地图或者矢量地图
	 */
	private Overlay backgroundOverlay = null;
	/**
	 * 映射类，用来屏幕左边和地图坐标的相互映射
	 */
	private Projection projection;
	/**
	 * 用来捕获屏幕手势，如拖动等
	 */
	private final GestureDetector gestureDetector;
	/**
	 * 用来控制地图的缩放等操作。
	 */
	private final MapController controller;
	/**
	 * 屏幕滚动条
	 */
	private final Scroller scroller;
	/**
	 * 自动改变，控制zoomlevel
	 */
	private final AtomicInteger targetZoomLevel = new AtomicInteger();
	/**
	 * 自动改变，控制动画是否正在进行
	 */
	private final AtomicBoolean boolIsAnimating = new AtomicBoolean(false);
	/**
	 * 放大动画
	 */
	private final ScaleAnimation zoomInAnimation;
	/**
	 * 缩小动画
	 */
	private final ScaleAnimation zoomOutAnimation;
	/**
	 * 缩放控件controller
	 */
	private ZoomButtonsController zoomButtonController;
	/**
	 * 是否使能缩放控件
	 */
	private boolean boolEnableZoocontroller = false;
	/**
	 * 用于处理map请求。
	 */
	private Handler handler;
	private Context context;
	/***/
	private int flag=-1;
	private TwoDCoordinate coor;
	/**
	 * 构造函数
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
		 * handler 处理
		 */
		if (tileRequestCompleteHandler == null)
			this.handler = new SimpleInvalidationHandler(this);
		else
			this.handler = tileRequestCompleteHandler;
		/**
		 * tilesoverlay 选择FIleProvider
		 */
		overlayManager = new OverlayManager(null);
		this.backgroundOverlay = new TilesOverlay(context,
				tileRequestCompleteHandler);
		this.overlayManager.setBackgroundOverlay(backgroundOverlay);
		/**
		 * controller 初始化
		 */
		this.controller = new MapController(this);
		// scroller初始化
		this.scroller = new Scroller(context);

		/**
		 * 初始化gestureDetector
		 */
		this.gestureDetector = new GestureDetector(context,
				new MapViewGestureDetectorListener());
		this.gestureDetector
				.setOnDoubleTapListener(new MapViewDoubleClickListener());
		/**
		 * 初始化zoomButton控件
		 */

		this.zoomButtonController = new ZoomButtonsController(this);
		zoomButtonController.setOnZoomListener(new MapViewZoomListener());
		/**
		 * 初始化缩放动画
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
	 * 获取mapController
	 * 
	 * @return 返回controller
	 */
	public MapController getController() {
		if (this.controller != null)
			return this.controller;
		else
			return new MapController(this);
	}

	/**
	 * 获取OverlayManager
	 * 
	 * @return
	 */
	public OverlayManager getOverlayManager() {
		return this.overlayManager;
	}

	/**
	 * 获取屏幕scroller
	 * 
	 * @return
	 */
	public Scroller getScroller() {
		return this.scroller;
	}

	/**
	 * 获取map请求的处理handle
	 */
	public Handler getHandler() {
		return this.handler;
	}

	/**
	 * 获取屏幕矩形，相对于地图坐标来说
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
	 * 获得projection
	 * 
	 * @return
	 */
	public Projection getProjection() {
		if (this.projection == null)
			this.projection = new Projection(this);
		return this.projection;
	}

	/**
	 * 设置地图的中心
	 * 
	 * @param center 中心点坐标
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
	 * 设置地图的中心
	 * 
	 * @param pixelX 地图像素X坐标
	 * @param pixelY 地图像素Y坐标
	 */
	void setMapCenter(int pixelX, int pixelY) {

		if (getAnimation() == null || getAnimation().hasEnded()) {
			scroller.startScroll(getScrollX(), getScrollY(), pixelX
					- getScrollX() - this.getWidth() / 2, pixelY - getScrollY()
					- this.getHeight() / 2, 500);
		}
	}

	/**
	 * 设置地图的缩放细节
	 * 
	 * @param aZoomLevel
	 * @return
	 */
	int setZoomLevel(final int aZoomLevel) {
		final int minZoomLevel = getMinZoomLevel();
		final int maxZoomLevel = getMaxZoomLevel();
		/**
		 * 获取的newZoomLevel如果在minZoomLevel和maxZoomLevel之间
		 * 则为aZoomLevel，否则为minZoomLevel或maxZoomLevel
		 */
		final int newZoomLevel = Math.max(minZoomLevel, Math.min(maxZoomLevel,
				aZoomLevel));
		final int curZoomLevel = this.zoomLevel;

		this.zoomLevel = newZoomLevel;
		this.checkZoomButtons();

		/**
		 * 低分辨率到高分辨率
		 */
		if (newZoomLevel > curZoomLevel) {
			// 当前地图的width
/*			int worldSize_current_W = 0;
			// 当前地图的height
			int worldSize_current_H = 0;
			// 新的地图width
			int worldSize_new_W = 0;
			// 新的地图height
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
	 * 获得当前地图的缩放细节
	 * 
	 * @return 如果当前正在进行缩放返回targetZoomLevel 否则返回 zoomLevel
	 */
	public int getZoomLevel() {
		// 正在进行缩放
		if (this.boolIsAnimating.get())
			return this.targetZoomLevel.get();
		return this.zoomLevel;
	}

	/**
	 * 返回最小的缩放细节
	 * 
	 * @return
	 */
	public int getMinZoomLevel() {
		return TileSystem.MIN_LEVEL;
	}

	/**
	 * 返回最大的缩放细节
	 * 
	 * @return
	 */
	public int getMaxZoomLevel() {
		return TileSystem.MAX_LEVEL;
	}

	/**
	 * 判断地图是否能再放大
	 * 
	 * @return
	 */
	public boolean canZoomIn() {
		final int maxZoomLevel = getMaxZoomLevel();
		// 如果当前zoomLevel比最大zoomLevel大或等则不可能再放大
		if (zoomLevel >= maxZoomLevel) {
			return false;
		}
		// 如果当前正在进行动画 然后targetZoomLevel比最大zoomLevel大或等则不可能再放大
		if (boolIsAnimating.get() & targetZoomLevel.get() >= maxZoomLevel) {
			return false;
		}
		return true;
	}

	/**
	 * 判断地图是否能再缩小
	 * 
	 * @return
	 */
	public boolean canZoomOut() {
		final int minZoomLevel = getMinZoomLevel();
		// 如果当前zoomLevel比最小zoomLevel小或等则不可能再缩小
		if (zoomLevel <= minZoomLevel) {
			return false;
		}
		// 如果当前正在进行动画 然后targetZoomLevel比最小zoomLevel小或等则不可能再缩小
		if (boolIsAnimating.get() & targetZoomLevel.get() <= minZoomLevel) {
			return false;
		}
		return true;
	}

	/**
	 * 用户点击放大按钮以及双击屏幕时都会调用该函数。
	 * 该函数主要是启动放大的动画，并改变zoomLevel的值。实际的缩放是在setZoomLevel中实现的。
	 * 
	 * @return 不能进行缩放(可能是因为当前正在缩放动作正在进行 或者放大级别达到最高)
	 */
	boolean zoomIn() {
		if (canZoomIn()) {
			// 当前正在进行动画
			if (boolIsAnimating.get()) {
				// TODO extend zoom (and return true)
				return false;
			} else {
				// level加一 放大一倍
				targetZoomLevel.set(zoomLevel + 1);
				// 设置当前动画正在运行为true
				boolIsAnimating.set(true);
				//this.setZoomLevel(targetZoomLevel.get());
				// 启动放大动画 放大动画结束后会进行实际的地图缩放
				startAnimation(zoomInAnimation);
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * 双击地图时会调用该函数，先设置点击的地图点为屏幕的中心点，然后再进行缩放，调用函数zoomIn()
	 * 
	 * @param point
	 * @return
	 */
	boolean zoomInFixing(final TwoDCoordinate point) {
		setMapCenter(point);
		return zoomIn();
	}

	/**
	 * 双击地图时会调用该函数，先设置点击的地图点为屏幕的中心点，然后再进行缩放，调用函数zoomIn()
	 * 
	 * @param pixelX 地图像素X坐标
	 * @param pixelY 地图像素Y坐标
	 * @return 缩放成功返回true 否则返回false
	 */
	boolean zoomInFixing(int pixelX, int pixelY) {
		setMapCenter(pixelX, pixelY);
		return zoomIn();
	}

	/**
	 * 用户点击缩小按钮以及双击屏幕时都会调用该函数。该函数主要是启动缩小的动画，
	 * 并改变zoomLevel的值。实际的缩放是在setZoomLevel中实现的。
	 * 
	 * @return
	 */
	boolean zoomOut() {
		if (canZoomOut()) {
			// 当前正在进行动画
			if (boolIsAnimating.get()) {
				// TODO extend zoom (and return true)
				return false;
			} else {
				// level减一缩小一倍
				targetZoomLevel.set(zoomLevel - 1);
				// 设置当前动画正在运行为true
				boolIsAnimating.set(true);
				// 启动缩小动画 缩小动画结束后会进行实际的地图缩放
				startAnimation(zoomOutAnimation);
				//this.setZoomLevel(targetZoomLevel.get());
				return true;
			}
		} else {
			return false;
		}

	}

	/**
	 * 先设置点击的地图点为屏幕的中心点，然后再进行缩放，调用函数zoomOut()
	 * 
	 * @param point
	 * @return
	 */
	boolean zoomOutFixing(final TwoDCoordinate point) {
		return false;
	}

	/**
	 * 获取当前地图在屏幕上的中心点
	 * 
	 * @return
	 */
	public TwoDCoordinate getMapCenter() {
		Rect rect = this.getScreenRect(null);
		return TileSystem.MapPixelsToMapXY(rect.left, rect.top, this
				.getZoomLevel());
	}

	/**
	 * 根据当前zoomLevel判断是否使能或禁用缩放控件
	 */
	private void checkZoomButtons() {
		this.zoomButtonController.setZoomInEnabled(canZoomIn());
		this.zoomButtonController.setZoomOutEnabled(canZoomOut());

	}

	/**
	 * 设置缩放按钮是否显示，true显示，否则不显示。
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
	}// 回调函数 动画开始时会调用该函数

	@Override
	protected void onAnimationEnd() {
		boolIsAnimating.set(false);
		clearAnimation();
		setZoomLevel(targetZoomLevel.get());
		super.onAnimationEnd();
	}// 回调函数，动画结束时会调用该函数

	/**
	 * 判断当前是否正在播放动画
	 * 
	 * @return 如果正在播放动画返回true 否则返回false
	 */
	public boolean isAnimating() {
		return boolIsAnimating.get();
	}

	/**
	 * 被onDetachedFromWindow函数调用
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

	}// 回调函数，当窗口附在窗口管理器上时会调用该函数

	/**
	 * 双击事件的监听者
	 * 
	 * @author 胡旭科
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
	 * 手势事件的监听者 当手势事件发生时会发出通知
	 * 
	 * @author 胡旭科
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
	 * zoombutton控件的监听者
	 * 
	 * @author 胡旭科
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
