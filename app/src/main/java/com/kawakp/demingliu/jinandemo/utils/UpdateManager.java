package com.kawakp.demingliu.jinandemo.utils;

import android.Manifest;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.http.OkHttpHelper;
import com.kawakp.demingliu.jinandemo.http.SimpleCallback;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @author coolszy
 * @date
 * @blog http://blog.92coding.com
 */

public class UpdateManager {
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /**
     * err
     */
    private static final int DOWNLOAD_ERR = 0x123;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;

    private long time;
    private String appName;

    private ClearableCookieJar cookieJar1;
    private OkHttpClient mHttpClient;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    mDownloadDialog.dismiss();
                    cancelUpdate = true;
                    // 安装文件
                    installApk();
                    break;
                default:
                    break;
            }
        }


    };

    public UpdateManager(Context context,  long time, String appName) {
        this.mContext = context;
        this.time = time;
        this.appName = appName;
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate(boolean flag) {
        if (flag)//是否有新版本
        {
            // 显示提示对话框
            showNoticeDialog();
        } else {
            Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {
        // 构造对话框
        Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_update_title);
        builder.setMessage(R.string.soft_update_info);
        // 更新
        builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton(R.string.soft_update_later, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 显示软件下载对话框
     */
    public void showDownloadDialog() {
        // 构造软件下载对话框
        Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_updating);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        // 取消更新
        builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
            }
        });

        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 现在文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */

    private void downloadApk() {
        String urlPath = "http://kawakp.chinclouds.com:60034/userconsle/clientApps/"+appName+"/file";
        OkHttpHelper okHttpHelper = OkHttpHelper.getInstance(mContext);

        okHttpHelper.download(urlPath, new OkHttpHelper.Download() {

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
                        Log.d("TAG",total+"------");
                        File file = new File(mSavePath , time+"热力管家.apk");
                        fos = new FileOutputStream(file);
                        long sum = 0;

                        while ( (len = is.read(buf)) != -1 && !cancelUpdate) {
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

                // 取消下载对话框显示
                mDownloadDialog.dismiss();
            }
        });

    }

    /**
     * 下载文件线程
     *
     * @author coolszy
     * @date
     * @blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {

                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    try {
                        String urlPath = "http://kawakp.chinclouds.com:58010/userconsle/clientApps/"+appName+"/file";
                       // URL url = new URL("http://erp.luxuriesclub.com/AndroidDownload/SPH.apk ");
                        Log.d("TAG",urlPath);
                        URL url = new URL(urlPath);

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                       // conn.addRequestProperty("Cookie",cookie );
                        //conn.setRequestProperty("Accept-Encoding", "identity");
                        conn.setRequestMethod("GET");
                        conn.connect();
                        int length = conn.getContentLength();
                        InputStream is = conn.getInputStream();
                        File file = new File(mSavePath , time+"泰瑞.apk");
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                        byte[] buf = new byte[1024 * 8];
                        int len = 0;
                        int allLen = 0;
                        int times = 0;
                        while ( (len = is.read(buf)) != -1 && !cancelUpdate) {
                            bos.write(buf, 0, len);
                            allLen += len;
                            if (times == 20 && length!=-1) {
                                progress = (allLen * 100) / length;
                                mHandler.sendEmptyMessage(DOWNLOAD);
                                times = 0;
                            }
                            //System.out.println("xiaoliu---"+len);
                            times++;

                        }
                        //System.out.println("xiaoliu>>>"+allLen);
                        if (length == -1) {
                            mHandler.sendEmptyMessage(DOWNLOAD_ERR);
                            installApk();
                        } else {
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                        }

                        //System.out.println("end" + length / 1024 / 1024);
                        bos.flush();
                        bos.close();
                        is.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // 取消下载对话框显示
                mDownloadDialog.dismiss();
            }


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
            mContext.startActivity(i);
        }
    }
