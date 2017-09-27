package com.example.remin.knowweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by remin.
 * Created Time 2017/7/13 ${Time}
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

}
