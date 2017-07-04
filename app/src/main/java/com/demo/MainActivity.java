package com.demo;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.demo.view.ColorTrackView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.id_changeTextColorView)
    ColorTrackView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_clock_view, R.id.btn_bezier, R.id.id_left, R.id.id_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_clock_view:
                startActivity(new Intent(MainActivity.this, ClockActivity.class));
                break;
            case R.id.btn_bezier:
                startActivity(new Intent(MainActivity.this, BezierActivity.class));
                break;
            case R.id.id_left:
                mView.setDirection(0);
                ObjectAnimator.ofFloat(mView, "progress", 0, 1).setDuration(2000)
                        .start();
                break;
            case R.id.id_right:
                mView.setDirection(1);
                ObjectAnimator.ofFloat(mView, "progress", 0, 1).setDuration(2000)
                        .start();
                break;
        }
    }
}
