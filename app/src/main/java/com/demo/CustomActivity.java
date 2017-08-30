package com.demo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

    @OnClick({R.id.btn_start, R.id.btn_insert, R.id.btn_query, R.id.btn_update, R.id.btn_delete})
    public void onViewClicked(View view) {
        Uri uri = Uri.parse("content://com.demo.database.provider/book");
        switch (view.getId()) {
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
                System.out.println("~~~~~~~~~~~~CustomActivity" + newUri.getPathSegments().size());
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
