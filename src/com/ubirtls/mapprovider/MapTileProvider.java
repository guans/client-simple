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
 * �ṩtile�Ļ��࣬��ؼ��ľ���getDrawable���� ͨ���첽�ķ�ʽ��ȡ��Ƭ����
 * 
 * @author �����
 * @version 0.1
 */
public abstract class MapTileProvider implements MapTileProviderConstants,
		MapInfoRequestListener {

	/** maptile���� ����tile���� */
	public static MapTileCache TILE_CACHE = null;

	/** �ļ�·�����������ļ������ɵ�ͼ������ */
	public static String REGION = "gym";

	/** tile������д�С */
	private static final int PENDING_QUEUE_SIZE = 40;

	private static final int THREAD_POOL_SIZE = 8;

	/** ������ģʽ�е���һ��Provider */
	protected MapTileProvider nextProvider = null;

	/** �ص� ������Ƭ�����첽��ȡ��ʽ */
	private IMapTileProviderCallback callback = null;

	/** ���������̵߳����� */
	private final ExecutorService executor;

	/* private final ConcurrentHashMap<MapTile, Integer> mWorking; */

	/** ��ǰ���ڱ�ִ�е����� */
	private final ConcurrentHashMap<MapTile, Integer> working;

	/** �ȴ���tile���� */
	final LinkedHashMap<MapTile, Integer> pending;

	/**
	 * ���캯��
	 * 
	 * @param handler Handler���� ����map����Ľ��
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
	 * ����������ģʽ�е���һ��Provider
	 * 
	 * @param nextProvider ��һ��Provider
	 */
	public void setNext(MapTileProvider nextProvider) {
		if (nextProvider != null)
			this.nextProvider = nextProvider;
	}

	/**
	 * ���ûص�����
	 * 
	 * @param callback IMapTileProviderCallback ����
	 */
	public void setCallback(IMapTileProviderCallback callback) {
		this.callback = callback;
	}

	/**
	 * ����MapTile���Drawable����
	 * 
	 * @param tile MapTile����
	 * @return ��Ӧ��Drawable����
	 */
	protected abstract Drawable getMapTile(final MapTile tile);

	/**
	 * ��ȡ�߳���
	 * 
	 * @return �߳���
	 */
	protected abstract String getThreadGroupName();

	/**
	 * ͨ���첽�ķ�ʽ������Ƭ����
	 * 
	 * @param tile ��Ҫ���ص���Ƭ��ʾ
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
	 * ��tile��pending�����Լ�working������ɾ��
	 * 
	 * @param mapTile MapTile����
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
	 * ���drawable���� �ȴӻ�����ȡ ͬ����ʽ
	 * 
	 * @param tile ��Ƭ ���ݸ�tile������Ӧ��drawable����
	 * @return Drawable����
	 */
	public Drawable getDrawableSynchronism(final MapTile tile) {
		// tileΪ��
		if (tile == null)
			return null;
		// ���ȴӻ�������ȡdrawable������������ڣ��򽻸�getMapTile����ͨ��������ʽ��ȡ
		Drawable drawable = TILE_CACHE.getMapTile(tile);
		if (drawable != null) {
			return drawable;
		}
		// �����в�������Ӧ��drawable���󣬵���getMapTile����������������
		return getMapTile(tile);
	}

	/**
	 * ͨ���첽�ķ�ʽ��ȡ��Ƭ����
	 * 
	 * @param tile ��Ƭ ���ݸ�tile������Ӧ��drawable����
	 * @return Drawable����
	 */
	public Drawable getDrawableAsynchronous(final MapTile tile) {
		// tileΪ��
		if (tile == null)
			return null;
		// ���ȴӻ�������ȡdrawable������������ڣ��򽻸�getMapTile����ͨ��������ʽ��ȡ
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
	 * ȷ������������㹻
	 * 
	 * @param capacity
	 */
	public void ensureCapacity(final int capacity) {
		TILE_CACHE.ensureCapacity(capacity);
	}

	/**
	 * ���tile����
	 */
	private void clearTileCache() {
		TILE_CACHE.clear();
	}

	/**
	 * ��յ�ͼ���� �����洢����ͼ�����Լ��ڴ��еĵ�ͼ����
	 */
	public void clearMapData() {
		File dir = new File(CLIENT_PATH, REGION);
		deleteAllFile(dir.getPath());
		clearTileCache();
	}

	/**
	 * ����ڴ��е����� ������ͼ�����Լ�pending��working�е�����
	 */
	public void clearRoomData() {
		clearTileCache();
		pending.clear();
		working.clear();
	}
	/**
	 * ��ñ��ػ�������е�ͼ������
	 * @return ���ػ���ĵ�ͼ������
	 */
	public String[] BrowserMap(){
		return browserFolder(CLIENT_PATH);
	}
	/**
	 * �����ļ����е������ļ�Ŀ¼
	 * @param file �ļ���
	 * @return �����ļ���Ŀ¼������
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
		/*��array��ֵ����*/
		String[] maps = new String[array.size()];
		for(int i = 0; i<= array.size() - 1; i++)
			maps[i] = array.get(i);
		return maps;
	}
	/**
	 * ɾ��path·���µ������ļ�
	 * 
	 * @param path �ļ���·����
	 */
	private void deleteAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		// ��ȡ�ļ��б�
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
				deleteAllFile(path + "/" + tempList[i]);// ��ɾ���ļ���������ļ�
				delFolder(path + "/" + tempList[i]);// ��ɾ�����ļ���
			}
		}
	}

	/**
	 * ɾ���ļ���
	 * 
	 * @param filePathAndName String �ļ���·�������� ��c:/fqf
	 */
	public void delFolder(String folderPath) {
		try {
			deleteAllFile(folderPath); // ɾ����������������
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // ɾ�����ļ���

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ�ļ���
	 * 
	 * @param tile ��Ƭ
	 * @return �ļ���
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
		 * ��mPending���ҵ�һ������mWorking�е�result��Ȼ���mPending���ҵ���Ӧ��MapTileRequestState
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
		 * encapsulated function. ���ϵ�ͨ��loadTile���drawable�����state����
		 * ���drawable����Ϊ�վ͵���tileLoaded���� �������tileLoadedFailed����
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
				// ��Ϊ�գ���֪ͨ��ͼ�������»��� ��Ƭ�����Ѿ��������ڴ滺���п���ֱ�ӷ���
				if (result != null) {
					callback.mapTileRequestCompleted(tile, result);
				} else {
					Log.i("file tile filled", tile.getZoomLevel() + " "
							+ tile.getX() + " " + tile.getY());
					callback.mapTileRequestFailed(tile);
				}
				// ��tile�Ӷ�����ɾ������Ϊ�������Ѿ�����
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
			// �û���������ı�
			REGION = region;
			clearTileCache();
			pending.clear();
			working.clear();
			// �����ͼ������Ϣ
			MapTileConfigure tileInfoLoader = new MapTileConfigure();
			tileInfoLoader.saveMapParams(region, tileWidthSize, tileHeightSize,
					wholeMapWidth, wholeMapHeight, minX, minY, maxX, maxY,
					minLevel, maxLevel);
		}
	}
}
