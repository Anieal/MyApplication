package com.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityListActivity extends AppCompatActivity {

    @BindView(R.id.rcl_activity)
    RecyclerView rclActivity;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    private List<String> mActivities;
    private ActivityAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolbar.setTitle("首页");
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));

        mActivities = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            mActivities.add(getActivityName(i));
        }
        rclActivity.setLayoutManager(new LinearLayoutManager(this));
        rclActivity.setItemAnimator(new DefaultItemAnimator());
        rclActivity.setAdapter(adapter = new ActivityAdapter());

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
        public void onBindViewHolder(MyHolder holder, int position, List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);
            if (payloads.isEmpty()) {
                onBindViewHolder(holder, position);
            } else {
                //文艺青年中的文青
                Bundle payload = (Bundle) payloads.get(0);
                String bean = mActivities.get(position);
                for (String key : payload.keySet()) {
                    switch (key) {
                        case "KEY_DESC":
                            holder.mTitle.setText(bean);
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        void resetData(List<String> newString) {
            mActivities = newString;
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

//    @OnClick(R.id.tv_main)
    void maimClick() {
        List<String> newString = new ArrayList<>();
        for (int i = 2; i < 8; i++) {
            newString.add(getActivityName(i));
        }
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new AdapterDiffCallback(mActivities, newString));
        diffResult.dispatchUpdatesTo(adapter);
        adapter.resetData(newString);
    }

    private class AdapterDiffCallback extends DiffUtil.Callback {

        private List<String> mOldList;
        private List<String> mNewList;

        AdapterDiffCallback(List<String> oldList, List<String> newList) {
            mOldList = oldList;
            mNewList = newList;
        }

        @Override
        public int getOldListSize() {
            return mOldList.size();
        }

        @Override
        public int getNewListSize() {
            return mNewList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldList.get(oldItemPosition).getClass().equals(mNewList.get(newItemPosition).getClass());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            String oldStr = mOldList.get(oldItemPosition);
            String newStr = mOldList.get(newItemPosition);
            return oldStr.equals(newStr);
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            String oldStr = mOldList.get(oldItemPosition);
            String newStr = mOldList.get(newItemPosition);
            Bundle payload = new Bundle();
            if (!oldStr.equals(newStr)) {
                payload.putString("KEY_DESC", newStr);
            }
            if (payload.size() == 0) return null;
            return payload;
        }
    }

}
