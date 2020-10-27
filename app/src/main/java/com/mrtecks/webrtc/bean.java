package com.mrtecks.webrtc;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.mrtecks.webrtc.videoListPOJO.Datum;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class bean extends Application {

    public static ArrayList<String> mylist;
    private static Context context;
    private static bean mInstance;

    List<videoTypes> plist = new ArrayList<>();
    public bean() {
        mylist = new ArrayList<>();
    }
    public static Context getContext() {
        return context;
    }

    public static synchronized bean getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = getApplicationContext();
        String TAG = "myApp";
        Log.e(TAG, "  myapp stater");

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
    }

}
