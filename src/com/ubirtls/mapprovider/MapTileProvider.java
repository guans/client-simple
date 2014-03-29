package com.ubirtls.mapprovider;

import java.io.File;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import com.ubirtls.MapInfoRequestListener;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * 提供tile的基类，最关键的就是getDrawable对象 通过异步的方式获取瓦片数据
 * 
 * @author 胡旭科
 * @version 0.1
 */
public abstract class MapTileProvider implements MapTileProviderConstants,
		MapInfoRequestListener {

	/** maptile缓存 缓存tile数据 */
	public static MapTileCache TILE_CACHE = null;

	/** 文件路径，不包括文件名，由地图名决定 */
	public static String REGION = "gym";

	/** tile请求队列大小 */
	private static final int PENDING_QUEUE_SIZE = 40;

	private static final int THREAD_POOL_SIZE = 8;

	/** 责任链模式中的下一个Provider */
	protected MapTileProvider nextProvider = null;

	/** 回调 用在瓦片数据异步获取方式 */
	private IMapTileProviderCallback callback = null;

	/** 用来启动线程的运行 */
	private final ExecutorService executor;

	/* private final ConcurrentHashMap<MapTile, Integer> mWorking; */

	/** 当前这在被执行的请求 */
	private final ConcurrentHashMap<MapTile, Integer> working;

	/** 等待的tile请求 */
	final LinkedHashMap<MapTile, Integer> pending;

	/**
	 * 构造函数
	 * 
	 * @param handler Handler对象 处理map请求的结果
	 */
	public MapTileProvider() {
		TILE_CACHE = new MapTileCache(30);
		executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE,
				new ConfigurablePriorityThreadFactory(Thread.NORM_PRIORITY,
						getThreadGroupName()));/**/
		working = new ConcurrentHashMap<MapTile, Integer>();
		pending = new LinkedHashMap<MapTile, Integer>(PENDING_QUEUE_SIZE + 2,
				0.1f, true) {

			private static final long serialVersionUID = 6455337315681858866L;

			@Override
			protected boolean removeEldestEntry(
					final Map.Entry<MapTile, Integer> pEldest) {
				return size() > PENDING_QUEUE_SIZE;
			}
		};
	}

	/**
	 * 设置责任链模式中的下一个Provider
	 * 
	 * @param nextProvider 下一个Provider
	 */
	public void setNext(MapTileProvider nextProvider) {
		if (nextProvider != null)
			this.nextProvider = nextProvider;
	}

	/**
	 * 设置回调处理
	 * 
	 * @param callback IMapTileProviderCallback 对象
	 */
	public void setCallback(IMapTileProviderCallback callback) {
		this.callback = callback;
	}

	/**
	 * 根据MapTile获得Drawable对象
	 * 
	 * @param tile MapTile对象
	 * @return 对应的Drawable对象
	 */
	protected abstract Drawable getMapTile(final MapTile tile);

	/**
	 * 获取线程名
	 * 
	 * @return 线程名
	 */
	protected abstract String getThreadGroupName();

	/**
	 * 通过异步的方式加载瓦片数据
	 * 
	 * @param tile 需要加载的瓦片标示
	 */
	private void loadMapTileAsynchronous(final MapTile tile) {
		synchronized (pending) {
			// this will put the tile in the queue, or move it to the front of
			// the queue if it's already present
			pending.put(tile, tile.getZoomLevel());
		}
		try {
			executor.execute(new TileLoader());
		} catch (RejectedExecutionException e) {

		}
	}

	/**
	 * 将tile从pending队列以及working队列中删除
	 * 
	 * @param mapTile MapTile对象
	 */
	private void removeTileFromQueues(final MapTile mapTile) {
		synchronized (pending) {
			pending.remove(mapTile);
		}
		synchronized(working){
			working.remove(mapTile);
		}
		/*
		 * mWorking.remove(mapTile);
		 */}

	/**
	 * 获得drawable对象 先从缓存中取 同步方式
	 * 
	 * @param tile 瓦片 根据该tile返回相应的drawable对象
	 * @return Drawable对象
	 */
	public Drawable getDrawableSynchronism(final MapTile tile) {
		// tile为空
		if (tile == null)
			return null;
		// 首先从缓存中提取drawable对象，如果不存在，则交给getMapTile函数通过其他方式获取
		Drawable drawable = TILE_CACHE.getMapTile(tile);
		if (drawable != null) {
			return drawable;
		}
		// 缓存中不存在相应的drawable对象，调用getMapTile方法，启动责任链
		return getMapTile(tile);
	}

	/**
	 * 通过异步的方式获取瓦片数据
	 * 
	 * @param tile 瓦片 根据该tile返回相应的drawable对象
	 * @return Drawable对象
	 */
	public Drawable getDrawableAsynchronous(final MapTile tile) {
		// tile为空
		if (tile == null)
			return null;
		// 首先从缓存中提取drawable对象，如果不存在，则交给getMapTile函数通过其他方式获取
		Drawable drawable = TILE_CACHE.getMapTile(tile);
		if (drawable != null) {
			return drawable;
		} else {
			/*
			 * boolean alreadyInProgress = false; synchronized (mWorking) {
			 * alreadyInProgress = mWorking.containsValue(tile); } if
			 * (!alreadyInProgress) { synchronized (mWorking) { // Check again
			 * alreadyInProgress = mWorking.containsValue(tile); if
			 * (alreadyInProgress) return null; mWorking.put(tile,
			 * tile.getZoomLevel()); } loadMapTileAsynchronous(tile); } return
			 * null;
			 */
			loadMapTileAsynchronous(tile);
			return null;
		}
	}

	/**
	 * 确保缓存的容量足够
	 * 
	 * @param capacity
	 */
	public void ensureCapacity(final int capacity) {
		TILE_CACHE.ensureCapacity(capacity);
	}

	/**
	 * 清空tile缓存
	 */
	private void clearTileCache() {
		TILE_CACHE.clear();
	}

	/**
	 * 清空地图数据 包括存储卡地图数据以及内存中的地图数据
	 */
	public void clearMapData() {
		File dir = new File(CLIENT_PATH, REGION);
		deleteAllFile(dir.getPath());
		clearTileCache();
	}

	/**
	 * 清除内存中的数据 包括地图缓存以及pending和working中的请求
	 */
	public void clearRoomData() {
		clearTileCache();
		pending.clear();
		working.clear();
	}
	/**
	 * 获得本地缓存的所有地图区域名
	 * @return 本地缓存的地图区域名
	 */
	public String[] BrowserMap(){
		return browserFolder(CLIENT_PATH);
	}
	/**
	 * 遍历文件夹中的所有文件目录
	 * @param file 文件夹
	 * @return 所有文件夹目录的名字
	 */
	private String[] browserFolder(File file){
		Vector<String> array = new Vector<String>();
		try{
			File[]files = file.listFiles();
			if(files.length > 0){
				 for(int j=0;j<files.length;j++){
					 if(files[j].isDirectory())
						 array.add(files[j].getName());
				 }
			}
		}
		catch(Exception e){
			return null;
		}
		/*将array的值返回*/
		String[] maps = new String[array.size()];
		for(int i = 0; i<= array.size() - 1; i++)
			maps[i] = array.get(i);
		return maps;
	}
	/**
	 * 删除path路径下的所有文件
	 * 
	 * @param path 文件夹路径名
	 */
	private void deleteAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		// 获取文件列表
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				deleteAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}

	/**
	 * 删除文件夹
	 * 
	 * @param filePathAndName String 文件夹路径及名称 如c:/fqf
	 */
	public void delFolder(String folderPath) {
		try {
			deleteAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取文件名
	 * 
	 * @param tile 瓦片
	 * @return 文件名
	 */
	protected String getTileURLString(MapTile tile) {
		/*
		 * return REGION + "/" + tile.getZoomLevel() + "/" + tile.getX() + "_" +
		 * tile.getY() + EXTENTION;
		 */
		return REGION + "/" + tile.getZoomLevel() + "/" + tile.getX() + "/"
				+ tile.getY() + EXTENTION;
	}

	protected class TileLoader implements Runnable {

		/**
		 * 从mPending中找到一个不在mWorking中的result，然后从mPending中找到对应的MapTileRequestState
		 * 
		 * @return MapTileRequestState
		 */
		private MapTile nextTile() {

			synchronized (pending) {
				MapTile result = null;

				// get the most recently accessed tile
				// - the last item in the iterator that's not already being
				// processed
				Iterator<MapTile> iterator = pending.keySet().iterator();

				// TODO this iterates the whole list, make this faster...
				while (iterator.hasNext()) {
					try {
						final MapTile tile = iterator.next();
						if (!working.containsKey(tile)) {
							result = tile;
						}
					} catch (final ConcurrentModificationException e) {
						// if we've got a result return it, otherwise try again
						if (result != null) {
							break;
						} else {
							iterator = pending.keySet().iterator();
						}
					}
				}

				if (result != null) {
					working.put(result, pending.get(result));
				}

				return result;
			}
		}

		/**
		 * This is a functor class of type Runnable. The run method is the
		 * encapsulated function. 不断的通过loadTile获得drawable对象和state对象
		 * 如果drawable对象不为空就调用tileLoaded方法 否则调用tileLoadedFailed方法
		 */
		@Override
		final public void run() {

			MapTile tile;
			Drawable result = null;
			while ((tile = nextTile()) != null) {
				try {
					result = null;
					result = getMapTile(tile);
				} catch (final Throwable e) {
				}
				// 不为空，则通知视图更新重新绘制 瓦片数据已经保存在内存缓存中可以直接访问
				if (result != null) {
					callback.mapTileRequestCompleted(tile, result);
				} else {
					Log.i("file tile filled", tile.getZoomLevel() + " "
							+ tile.getX() + " " + tile.getY());
					callback.mapTileRequestFailed(tile);
				}
				// 将tile从队列中删除，因为该请求已经处理
				removeTileFromQueues(tile);
			}
		}
	}

	@Override
	public void mapInfoRequested(String regionID, int tileWidthSize,
			int tileHeightSize, int wholeMapWidth, int wholeMapHeight,
			double minX, double minY, double maxX, double maxY, int minLevel,
			int maxLevel) {
		// TODO Auto-generated method stub

		String region = regionID;
		if (!REGION.equals(region)) {
			// 用户所在区域改变
			REGION = region;
			clearTileCache();
			pending.clear();
			working.clear();
			// 保存地图描述信息
			MapTileConfigure tileInfoLoader = new MapTileConfigure();
			tileInfoLoader.saveMapParams(region, tileWidthSize, tileHeightSize,
					wholeMapWidth, wholeMapHeight, minX, minY, maxX, maxY,
					minLevel, maxLevel);
		}
	}
}
