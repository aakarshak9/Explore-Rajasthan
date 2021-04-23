package com.example.aakarshak.explore.cache;

import android.graphics.Bitmap;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.collection.LruCache;


public class Image_bitmap {
    //Constant for Cache size of the Memory Cache
    private static final int DEFAULT_CACHE_SIZE = 25 * 1024 * 1024; //25MB in bytes

    //For the Singleton instance of this
    private static volatile Image_bitmap INSTANCE;

    //Memory Cache to save the Bitmaps decoded
    private final LruCache<Integer, Bitmap> mMemoryCache;

    private Image_bitmap() {
        //Retrieving the current Max Memory available (in bytes)
        final int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //Calculating the safe usable Max Memory which is 1/8th of the current Max Memory available
        final int maxMemoryThreshold = maxMemory / 8;
        //Selecting the cache size based on the current availability
        final int cacheSizeSelected = Math.min(DEFAULT_CACHE_SIZE, maxMemoryThreshold);

        //Initializing the Memory Cache
        mMemoryCache = new LruCache<Integer, Bitmap>(cacheSizeSelected) {
            @Override
            protected int sizeOf(@NonNull Integer key, @NonNull Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    private static Image_bitmap getInstance() {
        if (INSTANCE == null) {
            synchronized (Image_bitmap.class) {
                if (INSTANCE == null) {
                    //Creating the instance when not available
                    INSTANCE = new Image_bitmap();
                }
            }
        }
        //Using the previously created instance
        return INSTANCE;
    }

    public static Bitmap getBitmapFromCache(@DrawableRes Integer imageResId) {
        return getInstance().mMemoryCache.get(imageResId);
    }

    public static void addBitmapToCache(@DrawableRes Integer imageResId, Bitmap bitmap) {
        if (getBitmapFromCache(imageResId) == null
                && bitmap != null) {
            getInstance().mMemoryCache.put(imageResId, bitmap);
        }
    }

    public static void clearCache() {
        getInstance().mMemoryCache.evictAll();
    }
}
