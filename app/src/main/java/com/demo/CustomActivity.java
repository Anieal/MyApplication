package com.demo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.demo.view.MagicCircle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomActivity extends AppCompatActivity {

    @BindView(R.id.circle3)
    MagicCircle circle3;

    private Uri changeUri;
    private String newId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_right, R.id.btn_start, R.id.btn_insert, R.id.btn_query, R.id.btn_update, R.id.btn_delete})
    public void onViewClicked(View view) {
        Uri uri = Uri.parse("content://com.demo.database.provider/book");
        switch (view.getId()) {
            case R.id.btn_right:
                Intent intent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(this)
                        .setContentTitle("This is Title")
//                        .setContentText("This is text")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)//默认设置，铃声，振动...
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("This is long text.This is long text.This is long text.This is long text.This is long text.This is long text."))
                        .setPriority(NotificationCompat.PRIORITY_MAX)//优先级
//                        .setStyle(new NotificationCompat.BigPictureStyle().bigLargeIcon())//大图片
//                        .setVibrate(new long[]{0, 1500})//手机振动0静止，1500振动，静止，振动...
//                        .setLights(Color.GREEN, 1000, 1000)//设置手机前置led灯，颜色、亮起、暗去
                        .build();
                notificationManager.notify(1, notification);
                break;
            case R.id.btn_start:
                circle3.startAnimation();
                break;
            case R.id.btn_insert:
                ContentValues values = new ContentValues();
                values.put("name", "A Clash of Kings");
                values.put("author", "George Martin");
                values.put("pages", 1040);
                values.put("price", 22.85);
                Uri newUri = getContentResolver().insert(uri, values);
                newId = newUri.getPathSegments().get(1);
                break;
            case R.id.btn_query:
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("CustomActivity", "all msg:" + name + " : " + author + " : " + pages + " : " + price);
                    }
                    cursor.close();
                }
                break;
            case R.id.btn_update:
                changeUri = Uri.parse("content://com.demo.database.provider/book/" + newId);
                ContentValues values1 = new ContentValues();
                values1.put("name", "A Storm of Swords");
                values1.put("pages", 1216);
                values1.put("price", 24.05);
                getContentResolver().update(changeUri, values1, null, null);
                break;
            case R.id.btn_delete:
                getContentResolver().delete(changeUri, null, null);
                break;
        }
    }
}
