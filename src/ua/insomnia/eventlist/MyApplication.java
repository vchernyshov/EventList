package ua.insomnia.eventlist;

import android.app.Application;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// UNIVERSAL IMAGE LOADER SETUP
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisk(true)
				.cacheInMemory(true)
				.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				//.memoryCache(new WeakMemoryCache())
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.diskCacheSize(100 * 1024 * 1024).build();

		ImageLoader.getInstance().init(config);
		// END - UNIVERSAL IMAGE LOADER SETUP
	}
	
}
