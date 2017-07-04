package com.demo;

import android.app.Activity;
import android.os.Bundle;

import com.demo.view.MagicTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MagicTextViewActivity extends Activity {
    @BindView(R.id.magic_tv)
    MagicTextView textView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic_textview);
        ButterKnife.bind(this);
        textView.addInnerShadow(0, -1, 0, 0xFFffffff);
        textView.addOuterShadow(0, -1, 0, 0xffff0000);
        textView.setStroke(4, 0xFFff0000);
        textView.setForegroundDrawable(getResources().getDrawable(R.drawable.fake_luxury_tiled));
    }
}