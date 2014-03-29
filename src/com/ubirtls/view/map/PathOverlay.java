package com.ubirtls.view.map;

import java.util.ArrayList;

import com.ubirtls.Controller;
import com.ubirtls.LocationChangeListener;
import com.ubirtls.NavigateRequestListener;
import com.ubirtls.R;
import com.ubirtls.event.NavigateResult;
import com.ubirtls.util.OverlayItem;
import com.ubirtls.util.POI;

import coordinate.TwoDCoordinate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;

public class PathOverlay extends Overlay implements LocationChangeListener, TrackManangerListener,NavigateRequestListener{
	/**
	 * 一条路径上坐标的点
	 */
	private ArrayList<TwoDCoordinate> points;
	/**
	 * 一条路径上像素点
	 */
	private ArrayList<Point> pixels;
	/**
	 * 导航路径上的点
	 */
	private ArrayList<TwoDCoordinate> navigate_points;
	/***
	 * 一条路径上的点
	 */
	private ArrayList<Point>navigate_piexls;
	
	private Paint pointPaint = new Paint();
	/**
     * 
     */
	private int precomputerd;
	/**
	 * 用于绘制线的画笔
	 */
	protected Paint pathPaint = new Paint();
	/**
	 * 根据路径点绘制的路径
	 */
	private final Path path = new Path();
	/**
	 * 路径上的导航节点的图标
	 */
	private Bitmap bitmap,start_bitmap,end_bitmap;
	/**
	 * 构造函数
	 * 
	 * @param color 绘制路径的画笔的颜色
	 * @param context Context对象
	 */
	public PathOverlay(final Context context) {
		super(context);
		pathPaint.setColor(Color.BLUE);
		pathPaint.setStrokeWidth(8.0f);
		pathPaint.setStyle(Paint.Style.STROKE);
		
		pointPaint.setColor(Color.RED);
		pointPaint.setStrokeWidth(4.0f);
		pointPaint.setStyle(Paint.Style.STROKE);
		
		bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.stop_point);
		start_bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.qi);
		end_bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.zhong);
		
		clearPath();
		pathPaint.setPathEffect(new CornerPathEffect(5));

	}

	/**
	 * 设置画笔的颜色
	 * 
	 * @param color int 画笔的颜色
	 */
	public void setColor(final int color) {
		pathPaint.setColor(color);
	}

	/**
	 * 设置画笔透明度
	 * 
	 * @param alpha int 画笔透明度
	 */
	public void setAlpha(final int alpha) {
		pathPaint.setAlpha(alpha);
	}

	/**
	 * 返回画笔
	 * 
	 * @return 绘制路径的画笔
	 */
	public Paint getPaint() {
		return pathPaint;
	}

	/**
	 * 设置新的画笔
	 * 
	 * @param paint 新设置的画笔
	 */
	public void setPaint(Paint paint) {
		if (paint == null)
			throw new IllegalArgumentException("paint argument cannot be null");
		pathPaint = paint;
	}

	/**
	 * 清空路径
	 */
	public void clearPath() {
		this.points = new ArrayList<TwoDCoordinate>(24);
		this.pixels = new ArrayList<Point>(30);
		this.navigate_points=new ArrayList<TwoDCoordinate>(30);
		this.navigate_piexls=new ArrayList<Point>(35);
		this.precomputerd = 0;
	}

	/**
	 * 添加路径点
	 * 
	 * @param coordinate 添加的路径点
	 */
	public void addPoint(final TwoDCoordinate coordinate) {
		if (coordinate == null)
			throw new IllegalArgumentException(
					"coordinate argument cannot be null");
		this.points.add(coordinate);
	}

	public void addPoint(final double x, double y) {
		this.points.add(new TwoDCoordinate(x, y));
	}

	@Override
	protected void draw(final Canvas canvas, final MapView mapView,
			final boolean shadow) {
		if (shadow) {
			return;
		}
		// 路径点的个数
		final int size = this.points.size();
		Projection pj = mapView.getProjection();
		// 一条路径至少有两个点
		if (size >= 2) {
			// nothing to paint
			//return;
		
			// 地图像素坐标点
			while (precomputerd < size) {
				final TwoDCoordinate coordinate = this.points.get(precomputerd);
				pixels.add(precomputerd,TileSystem.MapXYToMapPixels(coordinate,mapView.getZoomLevel()));
				precomputerd++;
			}
			precomputerd = 0; 
			// 绘制路径和点
			path.rewind();
		    path.moveTo(pixels.get(0).x, pixels.get(0).y);
			for (int i = 1; i <= size - 1; i++){
				path.lineTo(pixels.get(i).x, pixels.get(i).y);
	/*		    Log.i("c1x-y", "("+pixels.get(i).x + "," + pixels.get(i).y + ")" );
	*/			//canvas.drawCircle(pixels.get(i-1).x, pixels.get(i-1).y, 1.0f, pointPaint);
			}
			canvas.drawPath(path, pathPaint);
		}
		
		int i=0;
		while(i<navigate_points.size())
		{
			navigate_piexls.add(i, TileSystem.MapXYToMapPixels(navigate_points.get(i), mapView.getZoomLevel()));
			if(i==1)
				canvas.drawBitmap(start_bitmap, navigate_piexls.get(i).x-start_bitmap.getWidth()/2, 
						navigate_piexls.get(i).y-start_bitmap.getHeight(), pointPaint);
			else if(i==navigate_points.size()-1)
				canvas.drawBitmap(end_bitmap, navigate_piexls.get(i).x-end_bitmap.getWidth()/2, 
						navigate_piexls.get(i).y-end_bitmap.getHeight(), pointPaint);			
			else if(i>1&&i<navigate_points.size()-1)
				canvas.drawBitmap(bitmap, (float) (navigate_piexls.get(i).x-bitmap.getWidth()/2+15.0),
						(float) (navigate_piexls.get(i).y-bitmap.getHeight()/2+16.5), pointPaint);
			i++;
		}
		if(i>0)
		{
			path.rewind();
			path.moveTo(navigate_piexls.get(1).x, navigate_piexls.get(1).y);
			for(i=1;i<navigate_points.size();i++)
			{
				path.lineTo(navigate_piexls.get(i).x+15, (float) (navigate_piexls.get(i).y+16.5));
			}
			canvas.drawPath(path, pathPaint);
		}
		// 及时释放资源
		pj = null;
	}

	@Override
	public void locationChanged(TwoDCoordinate coor) {
		// TODO Auto-generated method stub
	    if (coor != null)
			this.addPoint(coor);
	}

	@Override
	public void trackIsCleared() {
		// TODO Auto-generated method stub
		clearPath();
	}
	/*请求火灾点信息*/
	@Override
	public void firepointchanged(TwoDCoordinate coordinate) {
		// TODO Auto-generated method stub
		
	}
	/*请求导航路线*/
	@Override
	public void NavigateRoadRequest(NavigateResult result) {
		// TODO Auto-generated method stub
		if(result!=null)
		{
			navigate_points=new ArrayList<TwoDCoordinate>(20);
			navigate_piexls=new ArrayList<Point>(30);
			int Node_num=result.Node_num;
			for(int i=0;i<Node_num;i++)
				navigate_points.add(result.coordinate[i]);
		}
		
	}
}
