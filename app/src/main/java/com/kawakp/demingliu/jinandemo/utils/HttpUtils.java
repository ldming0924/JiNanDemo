package com.kawakp.demingliu.jinandemo.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by deming.liu on 2016/8/8.
 */
public class HttpUtils {
    public static byte[] loadByteFromURL1(String url,String cookie) {

        HttpURLConnection httpConn = null;
        BufferedInputStream bis = null;
        try {
            URL urlObj = new URL(url);
            httpConn = (HttpURLConnection) urlObj.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setDoInput(true);
            httpConn.setConnectTimeout(5000);
            httpConn.setRequestProperty("Cookie",cookie); //设置cookie

            httpConn.connect();

            if (httpConn.getResponseCode() == 200) {
                bis = new BufferedInputStream(httpConn.getInputStream());
                return streamToByte(bis);
            }else {
                bis = new BufferedInputStream(httpConn.getInputStream());
                byte[] b= streamToByte(bis);
                Log.d("TAG","err:"+new String(b));
                return  null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG","err:"+e.toString());
            // return "err"+e.getMessage().toString();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                httpConn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] streamToByte(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int c = 0;
        byte[] buffer = new byte[8 * 1024];
        try {
            while ((c = is.read(buffer)) != -1) {
                baos.write(buffer, 0, c);
                baos.flush();
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
