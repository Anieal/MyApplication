package com.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClockActivity extends AppCompatActivity {

    @BindView(R.id.tv_time)
    TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_view);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mHandler.sendEmptyMessage(0);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvTime.setText(getTime());
            mHandler.sendEmptyMessage(0);
        }
    };

    private String getTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR) + ":" +
                calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
    }

}
