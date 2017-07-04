package com.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityListActivity extends AppCompatActivity {
    @BindView(R.id.rcl_activity)
    RecyclerView rclActivity;
    private List<String> mActivities;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mActivities = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            mActivities.add(getActivityName(i));
        }
        rclActivity.setLayoutManager(new LinearLayoutManager(this));
        rclActivity.setItemAnimator(new DefaultItemAnimator());
        rclActivity.setAdapter(new ActivityAdapter());
    }

    class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.MyHolder> {

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(ActivityListActivity.this).inflate(R.layout.item_activity, parent, false));
        }

        @Override
        public void onBindViewHolder(MyHolder holder, final int position) {
            holder.mTitle.setText(mActivities.get(position));
            holder.mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoActivity(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mActivities.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textView3)
            TextView mTitle;

            MyHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }
    }

    private void gotoActivity(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(ActivityListActivity.this, MainActivity.class));
                break;
            case 1:
                startActivity(new Intent(ActivityListActivity.this, CustomActivity.class));
                break;
            case 2:
                startActivity(new Intent(ActivityListActivity.this, AidlDemoActivity.class));
                break;
            case 3:
                startActivity(new Intent(ActivityListActivity.this, LightSensorActivity.class));
                break;
            case 4:
                startActivity(new Intent(ActivityListActivity.this, CircleViewActivity.class));
                break;
            case 5:
                startActivity(new Intent(ActivityListActivity.this, MagicTextViewActivity.class));
                break;
            case 6:
                startActivity(new Intent(ActivityListActivity.this, MouseWebViewActivity.class));
                break;
            case 7:
                startActivity(new Intent(ActivityListActivity.this, CustomTextViewActivity.class));
                break;
            default:
                startActivity(new Intent(ActivityListActivity.this, MainActivity.class));
                break;
        }
    }

    private String getActivityName(int position) {
        switch (position) {
            case 0:
                return "MainActivity";
            case 1:
                return "CustomActivity";
            case 2:
                return "AidlDemoActivity";
            case 3:
                return "LightSensorActivity";
            case 4:
                return "CircleViewActivity";
            case 5:
                return "MagicTextViewActivity";
            case 6:
                return "MouseWebViewActivity";
            case 7:
                return "CustomTextViewActivity";
            default:
                return "MainActivity";
        }
    }

}
