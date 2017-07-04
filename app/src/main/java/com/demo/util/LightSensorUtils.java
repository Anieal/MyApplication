package com.demo.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.List;


public class LightSensorUtils implements SensorEventListener {
    private static final Object mLock = new Object();
    private static LightSensorUtils instance;

    private SensorManager mSensorManager;// 传感器管理器
    private boolean mIsContains = false;
    private  Boolean isBright ;//true 代表亮      false 代表暗


    private LightSensorUtils() {
    }

    //需要用光感器的地方在 init 初始化
    public void init(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //获取手机上支持的传感器
        List<Sensor> mList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : mList) {
            if (Sensor.TYPE_LIGHT ==  sensor.getType()){
                mIsContains = true ;
                return;
            }
        }
    }


    public static LightSensorUtils getInstance() {
        if (instance == null) {
            synchronized (mLock) {
                if (instance == null) {
                    instance = new LightSensorUtils();
                }
            }
        }
        return instance;
    }

    /*    第三个参数是传感器数据更新数据的速度
          有以下四个值可选，他们的速度是递增的
          SENSOR_DELAY_UI
          SENSOR_DELAY_NORMAL
          SENSOR_DELAY_GAME
          SENSOR_DELAY_FASTEST*/
    public void registerSensor() {
        // 获得光线传感器
        if ( null != mSensorManager){
            Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            if ( null != sensor && mIsContains) {
                mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
            }
        }

    }

    public void unRegisterSensor() {
        if ( null != mSensorManager){
            mSensorManager.unregisterListener(this);
        }

    }

    /*  onSensorChanged()在传感器数值发生变化已经注册监听器时调用，其更新频率就是注册中的参数三。
        对于光传感器，有效数值存放在values[0]中的，单位为SI lunx。
        光传感器通常位于上方（一般靠左侧）， 除了前置摄像头外还有一个孔，一般就是它。遮盖会触发onSensorChanged()  */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            Log.d("KeithXiaoY",event.values[0] + "");
            float criticalValue = 40.0f;
            isBright = event.values[0] > criticalValue;//  40.0f 代表人视觉的亮暗临界值,我司定义 > 40.0f 为亮，<= 40.0f 为暗

            // 下面就是每个人自己的逻辑了，根据拿到的 event.values[0] ,我写了一个把光的亮度分为 8 级的方法。
//            getBrightString(event.values[0]);

        }
    }

//    private int getBrightString(float value) {
//        if (value >= LIGHT_SUNLIGHT_MAX) {
//            currentMode = 8 ;
//            return currentMode;
//        } else if (value > LIGHT_SUNLIGHT) {
//            currentMode = 7 ;
//            return currentMode;
//        } else if (value > LIGHT_SHADE) {
//            currentMode = 6 ;
//            return currentMode;
//        } else if (value > LIGHT_OVERCAST) {
//            currentMode = 5 ;
//            return currentMode;
//        } else if (value > LIGHT_SUNRISE) {
//            currentMode = 4 ;
//            return currentMode;
//        } else if (value > LIGHT_CLOUDY) {
//            currentMode = 3 ;
//            return currentMode;
//        } else if (value > LIGHT_FULLMOON) {
//            currentMode = 2 ;
//            return currentMode;
//        } else if (value > LIGHT_NO_MOON) {
//            currentMode = 1 ;
//            return currentMode;
//        } else {
//            currentMode = -1 ;
//            return currentMode;
//        }
//    }


    /* onAccuracyChanged()会在精度改变或在注册监听器时调用。
        accuracy分为4档，0（unreliable），1（low），2（medium），3（high）。
        注意0并不代表有问题，同时是传感器需要校准。 */

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    public Boolean getBright() {
        return  isBright;
    }
}