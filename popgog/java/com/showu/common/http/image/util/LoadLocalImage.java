package com.showu.common.http.image.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.showu.baogu.R;
import com.showu.common.util.ImageUtil;

/**
 * Created by showu on 13-8-6.
 */
public class LoadLocalImage extends Thread {
    private int size ;
    private ImageView imageView ;
    private String localPath ;

    public LoadLocalImage(int size, ImageView imageView, String localPath) {
        this.size = size;
        this.imageView = imageView;
        this.localPath = localPath;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            imageView.setImageBitmap((Bitmap) msg.obj);
        }
    };

    @Override
    public void run() {
        Bitmap bitmap= ImageUtil.decodeSampledBitmapFromFile(localPath, size, size);
        Matrix matrix = new Matrix();
        matrix.postScale(0.2f,0.2f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        if(resizeBmp!=null){
            Message msg = new Message();
            msg.obj=resizeBmp ;
            handler.sendMessage(msg) ;
        }
    }
}
