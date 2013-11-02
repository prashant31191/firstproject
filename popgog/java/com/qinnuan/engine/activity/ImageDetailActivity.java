/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qinnuan.engine.activity;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.qinnuan.engine.fragment.ImageDetailFragment;
import com.qinnuan.common.http.image.util.ImageCache;
import com.qinnuan.common.http.image.util.ImageFetcher;
import com.qinnuan.common.widget.TitleWidget;
import com.showu.baogu.R;

public class ImageDetailActivity extends BaseActivity implements OnClickListener, TitleWidget.IOnClick,ViewPager.OnPageChangeListener {
    private static final String IMAGE_CACHE_DIR = "images";
    public static final String EXTRA_IMAGE = "extra_image";
    public static final String EXTRA_RUL = "url";
    public static final String EXTRA_RULS = "urls";

    private ImagePagerAdapter mAdapter;
    private ImageFetcher mImageFetcher;
    private ViewPager mPager;
    private String url;
    private String[] urls;
    private TitleWidget titleWidget;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_pager);
        titleWidget = (TitleWidget) findViewById(R.id.titleBar);
        titleWidget.setTitleListener(this);
        getIntentDate();
        // Fetch screen height and width, to use as our max size when loading images as this
        // activity runs full screen
//        final DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        final int height = displayMetrics.heightPixels;
//        final int width = displayMetrics.widthPixels;

        // For this sample we'll use half of the longest width to resize our images. As the
        // image scaling ensures the image is larger than this, we should be left with a
        // resolution that is appropriate for both portrait and landscape. For best image quality
        // we shouldn't divide by 2, but this will use more memory and require a larger memory
        // cache.
//        final int longest = (height > width ? height : width) / 2;

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        int longest= getResources().getDimensionPixelSize(R.dimen.front_image) ;
        mImageFetcher = new ImageFetcher(this, longest);
        mImageFetcher.setLoadingImage(R.drawable.film_front_defalt);
        mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
        mImageFetcher.setImageFadeIn(false);

        // Set up ViewPager and backing adapter
        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setPageMargin((int) getResources().getDimension(R.dimen.image_detail_pager_margin));
        mPager.setOffscreenPageLimit(2);
        mPager.setOnPageChangeListener(this);
        // Set up activity to go full screen
//        getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN);

        // Enable some additional newer visibility and ActionBar features to create a more
        // immersive photo viewing experience


        // Set the current item based on the extra passed in to this activity
        mPager.setCurrentItem(returnFisrstIndex());
        titleWidget.setCenterViewText(returnFisrstIndex()+1+"/"+urls.length);
    }

    private int returnFisrstIndex(){
        for(int i=0;i<urls.length;i++){
            if(url.equals(urls[i])){
                return i ;
            }
        }
        return 0 ;
    }

    private void getIntentDate() {
        url = getIntent().getStringExtra(EXTRA_RUL);
        String moreUrl = getIntent().getStringExtra(EXTRA_RULS);
        if (moreUrl != null) {
            this.urls = moreUrl.split(",");
        } else {
            urls = new String[]{url};
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }


    /**
     * Called by the ViewPager child fragments to load images via the one ImageFetcher
     */
    public ImageFetcher getImageFetcher() {
        return mImageFetcher;
    }

    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {

    }

    @Override
    public void centerClick() {

    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        titleWidget.setCenterViewText((i+1)+"/"+urls.length);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    /**
     * The main adapter that backs the ViewPager. A subclass of FragmentStatePagerAdapter as there
     * could be a large number of items in the ViewPager and we don't want to retain them all in
     * memory at once but create/destroy them on the fly.
     */
    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
        private String[] images;

        public ImagePagerAdapter(FragmentManager fm, String[] urls) {
            super(fm);
            this.images = urls;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Fragment getItem(int position) {
            return ImageDetailFragment.newInstance(images[position]);
        }
    }

    /**
     * Set on the ImageView in the ViewPager children fragments, to enable/disable low profile mode
     * when the ImageView is touched.
     */
    @TargetApi(11)
    @Override
    public void onClick(View v) {
        final int vis = mPager.getSystemUiVisibility();
        if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        } else {
            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        }
    }
}
