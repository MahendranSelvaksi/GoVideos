package mindmade.com.mvp.utils;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;



public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient okHttpClient=new OkHttpClient().newBuilder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .connectTimeout(10,TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(this,okHttpClient);
    }
}
