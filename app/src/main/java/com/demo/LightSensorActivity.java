package com.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;

import com.demo.util.LightSensorUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LightSensorActivity extends AppCompatActivity {
    @BindView(R.id.tv_light_sensor)
    AppCompatTextView mLightSensor;

    private LightSensorUtils mLightSensorUtils;
    private Boolean isBright;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_sensor);
        ButterKnife.bind(this);
        mLightSensorUtils = LightSensorUtils.getInstance();
        mLightSensorUtils.init(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLightSensorUtils.registerSensor();
        isBright = mLightSensorUtils.getBright();
        if (isBright != null) {
            checkTheme();
        }
    }

    private void checkTheme() {
        if (!isBright)
            System.out.println("~~~~~~~~~~~~~~同学您好，光线不足，切换到夜间模式吧");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLightSensorUtils.unRegisterSensor();
    }
}
