package com.qinnuan.common.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.qinnuan.engine.R;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.engine.xmpp.message.MessageSRC;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RecoderVoice {
    /* 录制的音频文件 */
    private File mRecAudioFile;
    // private File mRecAudioPath;
    /* MediaRecorder对象 */
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mediaPlayer;
    /* 零时文件的前缀 */
    private String strTempFile = "recaudio_";
    private View showPlayView;
    private Handler handler = new Handler();
    private Set<View> disImageList = new HashSet<View>();
    private BaseMessage currentMessage;
    private RecorderListener recorderListener;
    private boolean hasStop = false;

    public interface RecorderListener {
        public void startRecorder(MediaRecorder recorder);

        public void stopedRecorder(MediaRecorder mr);
    }

    /**
     * Called when the activity is first created.
     */
    public RecoderVoice(RecorderListener recorderListener) {
        this.recorderListener = recorderListener;
    }

    public RecoderVoice() {
    }

    public void  start(Context mContext)  {
        try {
            recorderListener.startRecorder(mMediaRecorder);
            File dir = new File(getCachDir(mContext));
            dir.mkdirs();
            /* 创建录音文件 */
            mRecAudioFile = new File(dir.getAbsolutePath() + File.separator
                    + UUID.randomUUID().toString() + ".amr");
            mRecAudioFile.createNewFile();
            if (mMediaRecorder != null) {
                mMediaRecorder.reset();
            }
			/* 实例化MediaRecorder对象 */
                mMediaRecorder = new MediaRecorder();
			/* 设置麦克风 */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			/* 设置输出文件的格式 */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			/* 设置音频文件的编码 */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			/* 设置输出文件的路径 */
            mMediaRecorder.setOutputFile(mRecAudioFile.getAbsolutePath());
            mMediaRecorder.setMaxDuration(90000);
			/* 准备 */
            mMediaRecorder.prepare();
			/* 开始 */
            mMediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String stop() {

        if (mRecAudioFile != null && mMediaRecorder != null) {
            mMediaRecorder.reset();
            recorderListener.stopedRecorder(mMediaRecorder);
            mMediaRecorder = null;
            return mRecAudioFile.getAbsolutePath();
        }
        return null;
    }

    /* 播放录音文件 */
    public void playMusic(File file, View display, BaseMessage message) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            LogUtil.e(getClass(), mediaPlayer);
        }
        if (this.showPlayView != null) {
            disImageList.add(showPlayView);
            handler.removeCallbacks(playVoice);
        }
        this.showPlayView = display;
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            if (!hasStop) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                ImageView playImage = (ImageView) showPlayView
                        .findViewById(R.id.voice);
                playImage.setImageResource(R.drawable.play);
            }
        }
        if (currentMessage == null) {
            this.currentMessage = message;
        } else if (!(currentMessage.getId() == message.getId())) {
            this.currentMessage = message;
        } else {// 点的是当前的message
            if (!hasStop) {// 如果没有停止那么停止返回.
                hasStop = true;
                return;
            } else {
                // 已经停止是另一次的点击播放
                LogUtil.i(getClass(), "Message Next Click !");
            }
        }
        // mediaPlayer = new MediaPlayer();

        try {
            LogUtil.e(RecoderVoice.class, file.getAbsolutePath());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.setOnPreparedListener(onPreparedListener);
            mediaPlayer.setOnCompletionListener(onCompletionListener);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 播放录音文件 */
    public void playMusic(File file) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer();
        try {
            LogUtil.e(RecoderVoice.class, file.getAbsolutePath());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(file.getAbsolutePath());

            mediaPlayer.prepareAsync();
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        if (mediaPlayer == null) {
            return false;
        } else {
            return mediaPlayer.isPlaying();
        }
    }

    private int playPostion = 0;
    float len = 0;
    private Runnable playVoice = new Runnable() {
        @Override
        public void run() {
            ImageView playImage = (ImageView) showPlayView
                    .findViewById(R.id.voice);
            resetDisplay();
            if (playPostion == 0) {
                playImage.setImageResource(R.drawable.message_voice_1);
            } else if (playPostion == 1) {
                playImage.setImageResource(R.drawable.message_voice_2);
            } else if (playPostion == 2) {
                playImage.setImageResource(R.drawable.message_voice_3);
            }
            if (playPostion == 2) {
                playPostion = 0;
            } else {
                playPostion++;
            }
            if (currentMessage.getMessageSRC() == MessageSRC.TO
                    && mediaPlayer.isPlaying()) {
                handler.postDelayed(playVoice, 500);
            } else if (currentMessage.getMessageSRC() == MessageSRC.FROME) {
                if (!hasStop) {
                    handler.postDelayed(playVoice, 500);
                } else {
                    playImage.setImageResource(R.drawable.play);
                    len = 0;
                }
            } else {
                LogUtil.v(RecoderVoice.class, "playing over");
                playImage.setImageResource(R.drawable.play);
                playPostion = 0;
            }
        }
    };

    private String getCachDir(Context mContext) {
        StringBuilder sb = new StringBuilder();
        String sdcardpath = Environment.getExternalStorageDirectory().getPath();
        if (sdcardpath != null) {
            return sb.append(sdcardpath).append(File.separator).append("engine")
                    .append(File.separator).append("voice").toString();
        } else {
            return sb.append(mContext.getCacheDir().getPath())
                    .append(File.separator).append("engine")
                    .append(File.separator).append("voice").toString();
        }
    }

    public void stopVoice() {
        if (!hasStop && mediaPlayer != null) {// 没有停止
            mediaPlayer.stop();
            mediaPlayer.reset();
            hasStop = true;
        }
    }

    private void resetDisplay() {
        for (View view : disImageList) {
            if (view != showPlayView) {
                ImageView playImage = (ImageView) view.findViewById(R.id.voice);
                playImage.setImageResource(R.drawable.play);
            }
        }
        disImageList.clear();
    }

    private OnPreparedListener onPreparedListener = new OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mp) {
            LogUtil.i(OnPreparedListener.class, "MediaPlayer onPrepared");
            hasStop = false;
            handler.post(playVoice);
        }
    };

    private OnCompletionListener onCompletionListener = new OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            LogUtil.i(OnCompletionListener.class, "MediaPlayer onCompletion");
            mediaPlayer.stop();
            mediaPlayer.reset();
            hasStop = true;
        }
    };

}

