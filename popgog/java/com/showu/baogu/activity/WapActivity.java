package com.showu.baogu.activity;

import android.os.Bundle;

import com.showu.baogu.fragment.message.WapFragment;

/**
 * Created by showu on 13-8-7.
 */
public class WapActivity extends BaseActivity{
    public static final String EXT_TITLE="title";
    public static final String EXT_URL="url";
    private WapFragment wapFragment ;
    private String title ;
    private String url ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title=getIntent().getStringExtra(EXT_TITLE);
        url=getIntent().getStringExtra(EXT_URL) ;
        wapFragment = new WapFragment(title,url) ;
        addFragment(wapFragment,false);
    }
}
