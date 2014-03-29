package com.ubirtls.view.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.ubirtls.LocationChangeListener;
import com.ubirtls.QueryListener;
import com.ubirtls.R;
import com.ubirtls.event.Query_Result;

import coordinate.TwoDCoordinate;
/**
 * 对部分位置点进行标记
 * @author zhangx
 *
 */
public class MarkOverlay extends Overlay implements LocationChangeListener,QueryListener{
	/***/
	private TwoDCoordinate coordinate;
	/**标识点坐标序列*/
	private ArrayList<TwoDCoordinate> points;
	/***/
	private Bitmap bitmap,bitmap1,firebitmap;
	/***/
	private ArrayList<Point> pixels;
	/**绘制标识点的画笔*/
	private Paint paint;
	/**绘制火灾点的画笔*/
	private Paint firepaint;
	/**火灾点*/
	private ArrayList<TwoDCoordinate> firepoint;
	/***/
	private ArrayList<Point>firepointPiexls;
	
	public MarkOverlay(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.balloon);
		bitmap1=BitmapFactory.decodeResource(context.getResources(), R.drawable.stop_point);
		firebitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.fire);
		
		clear_point();
	}

	public void addpoint(TwoDCoordinate coordinate)
	{
		if(coordinate==null)
		{
			throw new IllegalArgumentException(
			"coordinate argument cannot be null");
		}
		else
		{
			this.points.add(coordinate);
		}
	}
	public void addpoint(final double x,double y)
	{
		this.points.add(new TwoDCoordinate(x,y));
	}
	private void clear_point()
	{
		points=new ArrayList<TwoDCoordinate>(20);
		pixels=new ArrayList<Point>(20);
		firepoint=new ArrayList<TwoDCoordinate>(20);
		firepointPiexls=new ArrayList<Point>(30);
	}

	@Override
	protected void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		if(shadow)return;
		
		Projection pj=mapView.getProjection();
		int i=0;
		while(i<points.size())
		{
			final TwoDCoordinate coordinate=this.points.get(i);
			pixels.add(i,TileSystem.MapXYToMapPixels(coordinate, mapView.getZoomLevel()));
			i++;
		}
		i=0;
		for(;i<points.size();i++)
		{
			canvas.drawBitmap(bitmap, pixels.get(i).x-bitmap.getWidth()/2+5, (float) (pixels.get(i).y
					-bitmap.getHeight()/2+16.5), paint);
		}
		
		
	//	Point fire=TileSystem.MapXYToMapPixels(this.firepoint.get(0), mapView.getZoomLevel());
	//	canvas.drawBitmap(firebitmap, fire.x-firebitmap.getWidth()/2, 
	//			fire.y-firebitmap.getHeight()/2,firepaint);
		i=0;
		while(i<firepoint.size())
		{
			final TwoDCoordinate coordinate=this.firepoint.get(i);
			firepointPiexls.add(i, TileSystem.MapXYToMapPixels(coordinate, mapView.getZoomLevel()));
			i++;
		}
		for(i=0;i<firepoint.size();i++)
		{
			canvas.drawBitmap(firebitmap, firepointPiexls.get(i).x-firebitmap.getWidth()/2, 
					firepointPiexls.get(i).y-firebitmap.getHeight()/2, firepaint);
		}
	}

	@Override
	public void locationChanged(TwoDCoordinate coor) {
		// TODO Auto-generated method stub
		//if (coor != null)
		//	this.addpoint(coor);
	}

	@Override
	public void firepointchanged(TwoDCoordinate coordinate) {
		// TODO Auto-generated method stub
		if(coordinate!=null)
		{
			firepoint.add(coordinate);
			//this.addpoint(coordinate);
		}
	}

	@Override
	public void query_point_name(Query_Result result) {
		// TODO Auto-generated method stub
		if (result != null)
			this.addpoint(new TwoDCoordinate(200,300));
	}

}
