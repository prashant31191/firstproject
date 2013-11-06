package com.qinnuan.common.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.ImageDetailActivity;
import com.qinnuan.engine.activity.film.FilmIndexActivity;
import com.qinnuan.engine.bean.film.FilmBean;
import com.qinnuan.common.http.image.util.ImageFetcher;
import com.qinnuan.common.util.ImageUtil;

/**
 * Created by showu on 13-8-27.
 */
public class FilmImagePop extends LinearLayout implements AdapterView.OnItemClickListener,View.OnClickListener {
    private GridView mGridView;
    private ImageView playBtn;
    private ImageView closeBtn;
    private ImageView filmFront;
    private ImageAdapter mAdapter;
    private ImageFetcher mImageFetcher;
    private String[] urls;
    private FilmBean bean;

    public FilmImagePop(Context context, ImageFetcher fetcher, FilmBean bean) {
        super(context);
        this.bean=bean;
        mAdapter = new ImageAdapter(context);
        this.mImageFetcher = fetcher;
        urls = bean.getImagelist().split(",");
        LayoutInflater.from(context).inflate(R.layout.pop_film_detail, this);
        mGridView = (GridView) findViewById(R.id.gridView);
        playBtn = (ImageView) findViewById(R.id.play_button);
        filmFront=(ImageView)findViewById(R.id.film_front) ;
        mImageFetcher.loadImage(bean.getFrontcover(),filmFront);
        playBtn.setOnClickListener(this);
        closeBtn = (ImageView) findViewById(R.id.close);
        closeBtn.setOnClickListener(this);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    mImageFetcher.setPauseWork(true);
                } else {
                    mImageFetcher.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final Intent i = new Intent(getContext(), ImageDetailActivity.class);
        i.putExtra(ImageDetailActivity.EXTRA_RULS,bean.getImagelist() );
        i.putExtra(ImageDetailActivity.EXTRA_RUL, urls[position]);
        getContext().startActivity(i);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play_button:
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.setDataAndType(Uri.parse(bean.getTrailer()), "video/mp4");
                getContext().startActivity(it);
                break;
            case R.id.close:
                ((FilmIndexActivity)getContext()).closePop();
                break;
        }
    }

    private class ImageAdapter extends BaseAdapter {

        private final Context mContext;
        private int mItemHeight = 0;
        private int mNumColumns = 0;
        private int mActionBarHeight = 0;
        private GridView.LayoutParams mImageViewLayoutParams;

        public ImageAdapter(Context context) {
            super();
            mContext = context;
            int size = getContext().getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
            mImageViewLayoutParams = new GridView.LayoutParams(size,size);
            // Calculate ActionBar height
            TypedValue tv = new TypedValue();
            if (context.getTheme().resolveAttribute(
                    android.R.attr.actionBarSize, tv, true)) {
                mActionBarHeight = TypedValue.complexToDimensionPixelSize(
                        tv.data, context.getResources().getDisplayMetrics());
            }
        }

        @Override
        public int getCount() {
            // Size + number of columns for top empty row
            return urls.length;
        }

        @Override
        public Object getItem(int position) {
            return urls[position];
        }

        @Override
        public long getItemId(int position) {
            return position < mNumColumns ? 0 : position - mNumColumns;
        }

        @Override
        public int getViewTypeCount() {
            // Two types of views, the normal ImageView and the top row of empty views
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return (position < mNumColumns) ? 1 : 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            // First check if this is the top row
//            if (position < mNumColumns) {
//                if (convertView == null) {
//                    convertView = new View(mContext);
//                }
//                // Set empty view with height of ActionBar
//                convertView.setLayoutParams(new AbsListView.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, mActionBarHeight));
//                return convertView;
//            }

            // Now handle the main ImageView thumbnails
            ImageView imageView;
            if (convertView == null) { // if it's not recycled, instantiate and initialize
                imageView = new RecyclingImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(mImageViewLayoutParams);
            } else { // Otherwise re-use the converted view
                imageView = (ImageView) convertView;
            }

            // Check the height matches our calculated column width
//            if (imageView.getLayoutParams().height != mItemHeight) {
//                imageView.setLayoutParams(mImageViewLayoutParams);
//            }

            // Finally load the image asynchronously into the ImageView, this also takes care of
            // setting a placeholder image while the background thread runs
            mImageFetcher.loadImage(ImageUtil.get2xUrl(urls[position]), imageView);
            return imageView;
        }

        /**
         * Sets the item height. Useful for when we know the column width so the height can be set
         * to match.
         *
         * @param height
         */
        public void setItemHeight(int height) {
            if (height == mItemHeight) {
                return;
            }
            mItemHeight = height;
            mImageViewLayoutParams =
                    new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight);
            mImageFetcher.setImageSize(height);
            notifyDataSetChanged();
        }

        public void setNumColumns(int numColumns) {
            mNumColumns = numColumns;
        }

        public int getNumColumns() {
            return mNumColumns;
        }
    }
}
