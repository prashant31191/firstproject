package com.qinnuan.engine.fragment;

import android.os.Bundle;

import com.qinnuan.common.http.image.util.ImageCache;
import com.qinnuan.common.http.image.util.ImageFetcher;
import com.qinnuan.engine.R;

/**
 * Created by showu on 13-7-11.
 */
public abstract class FetcherFragment extends BaseFragment {
    private static final String IMAGE_CACHE_DIR = "thumbs";
    protected  ImageFetcher mImageFetcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory
        int size = getResources().getDimensionPixelSize(R.dimen.image_size) ;
        mImageFetcher = new ImageFetcher(getActivity(), size);
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }
}
