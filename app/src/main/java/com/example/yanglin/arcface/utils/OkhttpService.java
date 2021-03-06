package com.example.yanglin.arcface.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.example.yanglin.arcface.controllers.UserCtrl;
import com.example.yanglin.arcface.models.User;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yanglin on 18-4-10.
 */

public class OkhttpService {

    public interface OnResponseListener{
        void onSuccess(String result);
        void onFailure(IOException error);
    }

    Cache cache = new Cache();
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String TAG = "1";
    public static String basePath = "http://192.168.1.172:8000";

    protected String doGet(OkHttpClient okHttpClient, String url, final OnResponseListener listener) {
        url = basePath+url;
        Request request = new Request.Builder()
                .header("authorization", cache.getToken())
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                listener.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                int index1 = result.indexOf("调用失败");
                int index2 = result.indexOf("JsonWebToken");
                if(index1 != -1 || index2 != -1) {
                    cache.refreshToken(cache.getUser());
                    return;
                }
                listener.onSuccess(result);
            }
        });
        return null;
    }

    protected String doPost(OkHttpClient okHttpClient, String url, String data, final OnResponseListener listener) {
        url = basePath+url;
        RequestBody body = RequestBody.create(JSON, data);

        Request request = new Request.Builder()
                .header("authorization", cache.getToken())
                .url(url)
                .post(body)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                listener.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                int index1 = result.indexOf("调用失败");
                int index2 = result.indexOf("JsonWebToken");
                if(index1 != -1 || index2 != -1) {
                    cache.refreshToken(cache.getUser());
                    return;
                }
                listener.onSuccess(result);
            }
        });
        return null;
    }

    protected String doPut(OkHttpClient okHttpClient, String url, String data, final OnResponseListener listener) {
        url = basePath+url;
        RequestBody body = RequestBody.create(JSON, data);

        Request request = new Request.Builder()
                .header("authorization", cache.getToken())
                .url(url)
                .put(body)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                listener.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                int index1 = result.indexOf("调用失败");
                int index2 = result.indexOf("JsonWebToken");
                if(index1 != -1 || index2 != -1) {
                    cache.refreshToken(cache.getUser());
                    return;
                }
                listener.onSuccess(result);
            }
        });
        return null;
    }

    protected String doDelete(OkHttpClient okHttpClient, String url, String data, final OnResponseListener listener) {
        url = basePath+url;
        RequestBody body = RequestBody.create(JSON, data);

        Request request = new Request.Builder()
                .header("authorization", cache.getToken())
                .url(url)
                .delete(body)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                listener.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                int index1 = result.indexOf("调用失败");
                int index2 = result.indexOf("JsonWebToken");
                if(index1 != -1 || index2 != -1) {
                    cache.refreshToken(cache.getUser());
                    return;
                }
                listener.onSuccess(result);
            }
        });
        return null;
    }

    protected String upLoad(String url, File file, final OnResponseListener listener) {
        url = basePath+url;
        String path = file.getPath();
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", path, fileBody)
                .build();
        Request request = new Request.Builder()
                .header("authorization", cache.getToken())
                .url(url)
                .post(requestBody)
                .build();


        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient  = httpBuilder
                //设置超时
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure(e);
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                int index1 = result.indexOf("调用失败");
                int index2 = result.indexOf("JsonWebToken");
                if(index1 != -1 || index2 != -1) {
                    cache.refreshToken(cache.getUser());
                    return;
                }
                listener.onSuccess(result);
            }
        });
        return null;
    }

    public Bitmap returnBitMap(String url, Handler handler) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setRequestProperty("Authorization", cache.getToken());
            conn.setRequestProperty("Content-type", "application/json");
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            Message msg = Message.obtain();
            msg.obj = bitmap;
            msg.what = 1;
            handler.sendMessage(msg);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
