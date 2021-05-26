package com.example.myapplication;

import android.widget.LinearLayout;

import com.example.myapplication.MVPView.IMVPPresenter;
import com.example.myapplication.MVPView.MVPActivity;

public class ChooseAreaActivity extends MVPActivity {

    private LinearLayout root;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected IMVPPresenter createPresenter() {
        return null;
    }

    @Override
    protected void bindView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        if (prefs.getString("weather_id", null) != null) {
//            Intent intent = new Intent(this, WeatherActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }
}