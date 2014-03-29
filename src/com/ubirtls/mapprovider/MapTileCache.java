package com.ubirtls.mapprovider;

import java.util.LinkedHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 *MapTile，drawable的缓存，该机制可以一定程度上较少通过MapProvider向远程服务器请求数据的次数。
 * 通过LinkedHashMap的使用可以方便的引入LRU算法，进行缓存数据的替换
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class MapTileCache extends LinkedHashMap<MapTile, Drawable> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * cache的容量
	 */
	private int capacity;

	/**
	 * 读写锁，用在访问缓存数据时，避免多个进程同时读写缓存。
	 */
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	/**
	 * 构造函数
	 * 
	 * @param maximumCacheSize 最大的缓存容量
	 */
	public MapTileCache(final int maximumCacheSize) {
		super(maximumCacheSize + 2, 0.1f, true);
		capacity = maximumCacheSize;
	}

	/**
	 * 确保缓存的容量
	 * 
	 * @param capacity 需要保证的缓存容量
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
	
    // 清空缓存
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
	public Drawable remove(final Object aKey) {// 根据key移除某一项数据

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
	 * 如果此映射移除其最旧的条目，则返回 true。在将新条目插入到映射后，put 和 putAll
	 * 将调用此方法。此方法可以提供在每次添加新条目时移除最旧条目的实现程序。如果映射表示缓存，则此方法非常有用：它允许映射通过删除旧条目来减少内存损耗。
	 * 示例用法：此重写允许映射增加到 100 个条目，然后每次添加新条目时删除最旧的条目，始终维持 100 个条目的稳定状态。 private
	 * static final int MAX_ENTRIES = 100; protected boolean
	 * removeEldestEntry(Map.Entry eldest) { return size() > MAX_ENTRIES; } 参数：
	 * eldest - 在映射中最早插入的条目；如果是访问顺序映射，则为最早访问的条目。如果此方法返回 true，则此为将移除的条目。如果导致此调用的
	 * put 或 putAll 调用之前映射为空，则该条目就是刚刚插入的条目；换句话说，如果映射只包含单个条目，则最旧的条目也是最新的条目。 返回：
	 * 如果应该从映射移除最旧的条目，则返回 true；如果应该保留，则返回 false。
	 */
	@Override
	protected boolean removeEldestEntry(final Entry<MapTile, Drawable> eldest) {
/*		if (size() > capacity) {
			remove(eldest.getKey());
			// 在这里不用返回true，因为为偶们已经手动删除该条目
			// don't return true because we've already removed it
			Log.i("remove", "remove");
		}
		return false;*/
		return (size() > capacity);
	}

	/**
	 * 根据tile获得缓存中Drawable对象
	 * 
	 * @param tile 描述一个瓦片的标示
	 * @return Drawable对象
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
	 * 向缓存添加数据对<tile,drawable>
	 * 
	 * @param tile Tile对象
	 * @param drawable Drawable对象
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
	 * 判断缓存中是否包含关键字为tile的数据，如果存在可以直接使用，
	 * 而不需要向MapTileProvider请求，因为这样浪费时间。判断缓存中是否包含该tile
	 * 
	 * @param tile Tile对象
	 * @return 如果包含该tile返回true 否则返回false
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
