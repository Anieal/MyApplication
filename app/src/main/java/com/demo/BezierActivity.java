package com.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.demo.view.MyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BezierActivity extends AppCompatActivity {
    @BindView(R.id.myTextView)
    MyTextView myTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activcity_bezier);
        ButterKnife.bind(this);
        myTextView.setText("22");
    }
}
