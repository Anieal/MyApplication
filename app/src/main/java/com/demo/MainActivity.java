package com.demo;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

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

    @OnClick({R.id.btn_clock_view, R.id.btn_bezier, R.id.id_left, R.id.id_right, R.id.btn_all_enter})
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
            case R.id.btn_all_enter:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    call();
                }
                break;
        }
    }

    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10086"));
            startActivity(intent);
        } catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(this, "You denied the Permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
