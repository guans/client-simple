package com.ubirtls.mapprovider;

import java.util.LinkedHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 *MapTile��drawable�Ļ��棬�û��ƿ���һ���̶��Ͻ���ͨ��MapProvider��Զ�̷������������ݵĴ�����
 * ͨ��LinkedHashMap��ʹ�ÿ��Է��������LRU�㷨�����л������ݵ��滻
 * 
 * @author �����
 * @version 1.0
 */
public class MapTileCache extends LinkedHashMap<MapTile, Drawable> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * cache������
	 */
	private int capacity;

	/**
	 * ��д�������ڷ��ʻ�������ʱ������������ͬʱ��д���档
	 */
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	/**
	 * ���캯��
	 * 
	 * @param maximumCacheSize ���Ļ�������
	 */
	public MapTileCache(final int maximumCacheSize) {
		super(maximumCacheSize + 2, 0.1f, true);
		capacity = maximumCacheSize;
	}

	/**
	 * ȷ�����������
	 * 
	 * @param capacity ��Ҫ��֤�Ļ�������
	 */
	public void ensureCapacity(final int capacity) {
		readWriteLock.readLock().lock();
		try{
			if (capacity > this.capacity)
				this.capacity = capacity;
		}finally{
			readWriteLock.readLock().unlock();
		}
	}
	
    // ��ջ���
	@Override
	public void clear() { 
		readWriteLock.writeLock().lock();
		try{
		while (size() > 0) {
			remove(keySet().iterator().next());
		}
		super.clear();
		}finally{
			readWriteLock.writeLock().unlock();
		}
		readWriteLock.writeLock().lock();
		super.clear();
		readWriteLock.writeLock().unlock();
	}

	@Override
	public Drawable remove(final Object aKey) {// ����key�Ƴ�ĳһ������

		readWriteLock.writeLock().lock();
		try {
			final Drawable drawable = super.remove(aKey);
			if (drawable instanceof BitmapDrawable) {
				final Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
				if (bitmap != null) {
					bitmap.recycle();
				}
			}
			return drawable;
		} finally {
			readWriteLock.writeLock().unlock();
		}
	}

	/*
	 * �����ӳ���Ƴ�����ɵ���Ŀ���򷵻� true���ڽ�����Ŀ���뵽ӳ���put �� putAll
	 * �����ô˷������˷��������ṩ��ÿ���������Ŀʱ�Ƴ������Ŀ��ʵ�ֳ������ӳ���ʾ���棬��˷����ǳ����ã�������ӳ��ͨ��ɾ������Ŀ�������ڴ���ġ�
	 * ʾ���÷�������д����ӳ�����ӵ� 100 ����Ŀ��Ȼ��ÿ���������Ŀʱɾ����ɵ���Ŀ��ʼ��ά�� 100 ����Ŀ���ȶ�״̬�� private
	 * static final int MAX_ENTRIES = 100; protected boolean
	 * removeEldestEntry(Map.Entry eldest) { return size() > MAX_ENTRIES; } ������
	 * eldest - ��ӳ��������������Ŀ������Ƿ���˳��ӳ�䣬��Ϊ������ʵ���Ŀ������˷������� true�����Ϊ���Ƴ�����Ŀ��������´˵��õ�
	 * put �� putAll ����֮ǰӳ��Ϊ�գ������Ŀ���Ǹող������Ŀ�����仰˵�����ӳ��ֻ����������Ŀ������ɵ���ĿҲ�����µ���Ŀ�� ���أ�
	 * ���Ӧ�ô�ӳ���Ƴ���ɵ���Ŀ���򷵻� true�����Ӧ�ñ������򷵻� false��
	 */
	@Override
	protected boolean removeEldestEntry(final Entry<MapTile, Drawable> eldest) {
/*		if (size() > capacity) {
			remove(eldest.getKey());
			// �����ﲻ�÷���true����ΪΪż���Ѿ��ֶ�ɾ������Ŀ
			// don't return true because we've already removed it
			Log.i("remove", "remove");
		}
		return false;*/
		return (size() > capacity);
	}

	/**
	 * ����tile��û�����Drawable����
	 * 
	 * @param tile ����һ����Ƭ�ı�ʾ
	 * @return Drawable����
	 */
	public Drawable getMapTile(final MapTile tile) {
		readWriteLock.readLock().lock();
		try {
			return this.get(tile);
		} finally {
			readWriteLock.readLock().unlock();
		}
	}

	/**
	 * �򻺴�������ݶ�<tile,drawable>
	 * 
	 * @param tile Tile����
	 * @param drawable Drawable����
	 */
	public void putTile(final MapTile tile, final Drawable drawable) {
		readWriteLock.writeLock().lock();
		try {
			this.put(tile, drawable);
		} finally {
			readWriteLock.writeLock().unlock();
		}
	}

	/**
	 * �жϻ������Ƿ�����ؼ���Ϊtile�����ݣ�������ڿ���ֱ��ʹ�ã�
	 * ������Ҫ��MapTileProvider������Ϊ�����˷�ʱ�䡣�жϻ������Ƿ������tile
	 * 
	 * @param tile Tile����
	 * @return ���������tile����true ���򷵻�false
	 */
	public boolean containsTile(final MapTile tile) {
		readWriteLock.readLock().lock();
		try {
			return this.containsKey(tile);
		} finally {
			readWriteLock.readLock().unlock();
		}
	}

}
