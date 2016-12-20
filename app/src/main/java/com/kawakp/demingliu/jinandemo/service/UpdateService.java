package com.kawakp.demingliu.jinandemo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.ProgressBar;

import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.http.OkHttpHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by deming.liu on 2016/12/19.
 */

public class UpdateService extends Service {
    private NotificationManager notificationManager;
    private Notification notification;
    private String apkUrl;

    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /**
     * err
     */
    private static final int DOWNLOAD_ERR = 0x123;

    /* 更新进度条 */
    private ProgressBar mProgress;
    /* 记录进度条数量 */
    private int progress;

    /* 下载保存路径 */
    private String mSavePath;
    private String time;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null){
            notifyUser(getString(R.string.update_download_failed), getString(R.string.update_download_failed), 0);
            stopSelf();
        }
        apkUrl = intent.getStringExtra("apkUrl");
        time = intent.getStringExtra("TIME");
        notifyUser(getString(R.string.update_download_start), getString(R.string.update_download_start), 0);
        downloadApk(apkUrl);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新notification
     * @param result
     * @param msg
     * @param progress
     */
    private void notifyUser(String result, String msg, int progress){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                .setContentTitle(getString(R.string.app_name));
        if(progress>0 && progress<=100){

            builder.setProgress(100,progress,false);

        }else{
            builder.setProgress(0, 0, false);
        }
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setTicker(result);
        builder.setContentIntent(progress>=100 ? getContentIntent() :
                PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
        notification = builder.build();
        notificationManager.notify(0, notification);


    }

    /**
     * 进入apk安装程序
     * @return
     */
    private PendingIntent getContentIntent() {
        Log.e("tag", "getContentIntent()");
        File apkfile = new File(mSavePath,time+"热力管家.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        startActivity(intent);
        return pendingIntent;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                  //  mProgress.setProgress(progress);
                    notifyUser(getString(R.string.update_download_progressing), getString(R.string.update_download_progressing), progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    //installApk();
                    notifyUser(getString(R.string.update_download_finish), getString(R.string.update_download_finish), 100);
                    stopSelf();
                    break;
                default:
                    break;
            }
        }


    };

    /**
     * 下载apk文件
     */

    private void downloadApk(String apkUrl) {

        OkHttpHelper okHttpHelper = OkHttpHelper.getInstance(getApplicationContext());

        okHttpHelper.download(apkUrl, new OkHttpHelper.Download() {

            @Override
            public void getInputStream(InputStream inputStream, long length) {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    InputStream is = null;
                    byte[] buf = new byte[1024 * 8];
                    int len = 0;
                    int allLen = 0;
                    int times = 0;
                    FileOutputStream fos = null;

                    try {
                        //is = response.body().byteStream();
                        is = inputStream;
                        long total = length;
                       // Log.d("TAG",total+"------");
                        File file = new File(mSavePath , time+"热力管家.apk");
                        fos = new FileOutputStream(file);
                        long sum = 0;

                        while ( (len = is.read(buf)) != -1 ) {
                            fos.write(buf, 0, len);
                            allLen += len;
                            if (times == 20 && total!=-1) {
                                progress = (int)((allLen * 100) / total);
                                mHandler.sendEmptyMessage(DOWNLOAD);
                                times = 0;
                            }
                            times++;
                        }
                        if (total == -1) {
                            mHandler.sendEmptyMessage(DOWNLOAD_ERR);
                            installApk();
                        } else {
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                        }
                        fos.flush();
                        Log.d("TAG", "文件下载成功");
                    } catch (Exception e) {
                        Log.d("TAG", "文件下载失败"+e.toString());
                    } finally {
                        try {
                            if (is != null)
                                is.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (IOException e) {
                            Log.d("TAG",e.toString());
                        }
                    }
                }else {
                    Log.d("TAG","内存卡不存在");
                }


            }
        });

    }

    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath,time+"热力管家.apk");
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        getApplicationContext().startActivity(i);
    }
}
