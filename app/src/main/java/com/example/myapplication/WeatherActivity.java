package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bumptech.glide.Glide;
import com.example.myapplication.MVPView.MVPActivity;
import com.example.myapplication.citymanage.CityManageActivity;
import com.example.myapplication.db.AQI;
import com.example.myapplication.db.Forecast;
import com.example.myapplication.db.HoursForecast;
import com.example.myapplication.db.Suggestion;
import com.example.myapplication.db.Weather;
import com.example.myapplication.utils.HttpUtil;
import com.example.myapplication.utils.Utility;
import com.example.myapplication.view.HoursForecastAdapter;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends MVPActivity<WeatherPresenter> {

    private static final String TAG = "zyc";

    public static class WeatherRefreshLayout extends SwipeRefreshLayout {

        public WeatherRefreshLayout(@NonNull Context context) {
            super(context);
        }

        public WeatherRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        private float lastX,lastY;
        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = ev.getX();
                    lastY = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float distanceX = Math.abs(ev.getX() - lastX);
                    float distanceY = Math.abs(ev.getY() - lastY);
                    if (distanceX > 10.0f && distanceX / distanceY > 0.5f) {
                        return false;
                    }
                    break;
                default:
                    break;
            }
            return super.onInterceptTouchEvent(ev);
        }

    }

    public static class WeatherScrollView extends ScrollView {

        public WeatherScrollView(Context context) {
            super(context);
        }

        public WeatherScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public WeatherScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public WeatherScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        private float lastX,lastY;
        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = ev.getX();
                    lastY = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float distanceX = Math.abs(ev.getX() - lastX);
                    float distanceY = Math.abs(ev.getY() - lastY);
                    if (distanceX > 10.0f && distanceX / distanceY > 0.5f) {
                        return false;
                    }
                    break;
                default:
                    break;
            }
            return super.onInterceptTouchEvent(ev);
        }
    }

    public static final String ADDRESS_BING_HEADER_URL = "https://cn.bing.com/";
    public static final String ADDRESS_BING_PIC_URL = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&nc=1620485232573&pid=hp&uhd=1&uhdwidth=3840&uhdheight=2160";

    private SwipeRefreshLayout refreshLayout;
    private ScrollView weatherLayout;

    private TextView titleCity;
    private ImageView changeIcon, weatherConditionBg;
    private TextView titleUpdateTime, degreeText, weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView categoryText, aqiText, pm25Text, carWashText, sportText;
    private ImageView bingBg;

    private RecyclerView hoursForecast;
    private HoursForecastAdapter adapter;

    private String cityId, cityName, temp;

    @Override
    protected WeatherPresenter createPresenter() {
        return new WeatherPresenter(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_weather;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fitStatusBar();
        super.onCreate(savedInstanceState);
    }

    private void fitStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    protected void bindView() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        weatherConditionBg = (ImageView) findViewById(R.id.weather_condition_bg);
        forecastLayout = findViewById(R.id.forecast_layout);

        categoryText = findViewById(R.id.aqi_category_info);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
        changeIcon = findViewById(R.id.change_city);
        titleCity = findViewById(R.id.title_city);

        bingBg = findViewById(R.id.bing_bg);

        hoursForecast = findViewById(R.id.hours_forecast);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("zyc", "onPause: " + this.toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(this)
                .edit();
        editor.putString("city_id", cityId);
        editor.putString("city_name", cityName);
        editor.apply();
    }

    @Override
    protected void initData() {
        cityId = getIntent().getStringExtra("weather_id");
        cityName = getIntent().getStringExtra("city_name");
        if (cityId == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            cityId = preferences.getString("city_id", null);
            cityName = preferences.getString("city_name", null);
        }
    }

    @Override
    protected void initView() {

        if (cityId == null) {
            Intent intent = new Intent(this, ChooseAreaActivity.class);
            startActivity(intent);
            finish();
            return ;
        }
        forecastLayout.removeAllViews();

        changeIcon.setOnClickListener(v -> {
            Intent intent = new Intent(this, CityManageActivity.class);
            intent.putExtra("city_id", cityId);
            intent.putExtra("temperature", temp != null ? temp : "24");
            intent.putExtra("city_name", cityName);
            startActivity(intent);
            finish();
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hoursForecast.setLayoutManager(layoutManager);
        adapter = new HoursForecastAdapter();
        hoursForecast.setAdapter(adapter);
        hoursForecast.setHasFixedSize(true);
        hoursForecast.setNestedScrollingEnabled(false);

        titleCity.setText(cityName);
        weatherLayout.setVisibility(View.INVISIBLE);

        //服务端请求
        loadPic();
        getPresenter().setWeatherId(cityId);
        getPresenter().requestWeather();
        getPresenter().requestForecast();
        getPresenter().requestAQI();
        getPresenter().requestSuggestion();
        getPresenter().requestHoursForecast();

        refreshLayout.setColorSchemeResources(R.color.design_default_color_primary_dark);
        refreshLayout.setOnRefreshListener(() -> {
            getPresenter().requestWeather();
            getPresenter().requestForecast();
            getPresenter().requestAQI();
            getPresenter().requestSuggestion();
            getPresenter().requestHoursForecast();
        });



    }

    private int count = 0;
    public void refreshCounter() {
        if ((++ count) == 4) {
            refreshLayout.setRefreshing(false);
            count = 0;
        }
    }

    public void showSuggestionInfo(Suggestion suggustion) {
        runOnUiThread(() -> {
            for (Suggestion.DailySug sug : suggustion.dailyList) {
                switch (sug.type) {
                    case "1" : {
                        sportText.setText(sug.name + ": " + sug.suggestInfo);
                        break;
                    } case "2" : {
                        carWashText.setText(sug.name + ": " + sug.suggestInfo);
                        break;
                    }
                }
            }
        });
    }

    public void showAQIInfo(AQI aqi) {
        runOnUiThread(() -> {
            categoryText.setText(aqi.now.category);
            aqiText.setText(aqi.now.aqi);
            pm25Text.setText(aqi.now.pm25);
        });
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void showWeatherInfo(Weather weather) {
        runOnUiThread(() -> {
            String[] time = weather.updateTime.split("T");
            String updateTime = time[1].split("\\+")[0];
            String degree = weather.now.temperature + "°C";
            String iconId = weather.now.weatherConditionBg;
            String weatherCondition = weather.now.weatherCondition;
            titleUpdateTime.setText(updateTime);
            degreeText.setText(degree);
            weatherInfoText.setText(weatherCondition);
            int resId = parseBg(iconId);
            if (resId != -1) {
                weatherConditionBg.setBackgroundResource(resId);
            } else {
                weatherConditionBg.setVisibility(View.GONE);
            }

            weatherLayout.setVisibility(View.VISIBLE);
//            Intent intent = new Intent(this, AutoUpdateService.class);
        });
    }

    private int parseBg(String icon) {
        switch (icon) {
            case "100":
                return R.drawable.bg100;
            case "101":
                return R.drawable.bg101;
            case "103":
                return R.drawable.bg103;
            case "104":
                return R.drawable.bg104;
            case "300":
                return R.drawable.bg300;
            case "305":
                return R.drawable.bg305;
            case "306":
                return R.drawable.bg306;
            case "307":
                return R.drawable.bg307;
        }
        return -1;
    }

    public void toastErrorInfo(String info) {
        runOnUiThread(() -> {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        });
    }

    public void showForecastInfo(Forecast forecast) {
        runOnUiThread(() -> {
            forecastLayout.removeAllViews();
            for (Forecast.Daily daily : forecast.dailyList) {
                View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
                TextView dateText = (TextView) view.findViewById(R.id.date_text);
                TextView infoText = (TextView) view.findViewById(R.id.info_text);
                TextView maxText = (TextView) view.findViewById(R.id.max_text);
                TextView minText = (TextView) view.findViewById(R.id.min_text);
                dateText.setText(daily.date);
                infoText.setText(daily.dailyCondition);
                maxText.setText(daily.tempMax);
                minText.setText(daily.tempMin);
                forecastLayout.addView(view);
            }
        });
    }

    public void refreshHoursForecast(HoursForecast hoursForecast) {
        if (hoursForecast == null || hoursForecast.getHoursData() == null || hoursForecast.getHoursData().size() == 0) {
            return;
        }

        runOnUiThread(() -> {
            adapter.setDataList(hoursForecast.getHoursData());
            adapter.notifyDataSetChanged();
        });
    }

    private void loadPic() {
        String address = ADDRESS_BING_PIC_URL;
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("zyc", "onFailure: get pic failed !");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                String url = Utility.handleBingPicResponse(responseText);
                if (!TextUtils.isEmpty(url)) {
                    runOnUiThread(() -> {
                        String picUrl = ADDRESS_BING_HEADER_URL + url;
                        Glide.with(WeatherActivity.this)
                                .load(picUrl)
                                .into(bingBg);
                    });
                }
            }
        });
    }

}