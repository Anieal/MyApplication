package com.demo;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.demo.service.DownloadService;
import com.demo.service.MyService;
import com.demo.view.MyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BezierActivity extends AppCompatActivity {
    @BindView(R.id.myTextView)
    MyTextView myTextView;

    private DownloadService.DownloadBinder downloadBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activcity_bezier);
        ButterKnife.bind(this);
        myTextView.setText("22");
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, connection, BIND_ABOVE_CLIENT);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @OnClick({R.id.btn_start_service, R.id.btn_stop_service, R.id.btn_bind_service, R.id.btn_un_bind_service, R.id.btn_start_download, R.id.btn_pause_download, R.id.btn_cancel_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start_service:
                startService(new Intent(this, MyService.class));
                break;
            case R.id.btn_stop_service:
                stopService(new Intent(this, MyService.class));
                break;
            case R.id.btn_bind_service:
                break;
            case R.id.btn_un_bind_service:
                break;
            case R.id.btn_start_download:
                String url = "http://bos.pgzs.com/rbreszy/aladdin/697aaed0b573408f860ea136a6bff78c/91Assistant_PC_V6_16/%E6%9C%80%E5%9B%A7%E6%B8%B8%E6%88%8F2_%E6%9E%81%E9%80%9F%E5%AE%89%E8%A3%85.exe";
                downloadBinder.startDownload(url);
                break;
            case R.id.btn_pause_download:
                downloadBinder.pauseDownload();
                break;
            case R.id.btn_cancel_download:
                downloadBinder.cancelDownload();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
