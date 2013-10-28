package com.showu.common.http;

import java.io.File;

/**
 * Created by showu on 13-7-5.
 */
public interface IDownLoadListener {
    public void startDownload(int length) ;
    public void downloading(int len) ;
    public void downLoadEnd(File file) ;
}
