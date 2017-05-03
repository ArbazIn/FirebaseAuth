package com.tech.firebasetestapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.tech.firebasetestapp.model.DeviceInfo;

/**
 * Created by arbaz on 13/4/17.
 */

public class FirebaseApplication extends Application {

    public static DeviceInfo deviceInfo;
    public static SharedPreferences sharedPref;

    @Override
    public void onCreate() {
        super.onCreate();


        //fresco Image viewer
        Fresco.initialize(this);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
        imagePipeline.clearDiskCaches();

        deviceInfo = new DeviceInfo(this);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
