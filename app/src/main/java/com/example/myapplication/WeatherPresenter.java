package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.MVPView.MVPPresenter;
import com.example.myapplication.db.AQI;
import com.example.myapplication.db.Forecast;
import com.example.myapplication.db.HoursForecast;
import com.example.myapplication.db.Suggestion;
import com.example.myapplication.db.Weather;
import com.example.myapplication.utils.HttpUtil;
import com.example.myapplication.utils.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherPresenter extends MVPPresenter<WeatherActivity> {
    public static final String WEATHER_KEY = "&key=51c36aefb4c04d3e873ee71e3d54625e";
    public static final String ADDRESS_WEATHER_URL = "https://devapi.qweather.com/v7/weather/now?location=";
    public static final String ADDRESS_FORECAST_URL = "https://devapi.qweather.com/v7/weather/7d?location=";
    public static final String ADDRESS_AQI_URL = "https://devapi.qweather.com/v7/air/now?location=";
    public static final String ADDRESS_SUGGESTION_URL = "https://devapi.qweather.com/v7/indices/1d?type=1,2&location=";
    public static final String ADDRESS_HOURS_FORECAST_URL = "https://devapi.qweather.com/v7/weather/24h?location=";

    private String weatherId;

    public WeatherPresenter(WeatherActivity activity) {
        super(activity);
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public void requestWeather() {
        String weatherUrl = ADDRESS_WEATHER_URL + weatherId + WEATHER_KEY;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText, Weather.class);
                if (weather != null && "200".equals(weather.statusCode)) {
                    getMvpView().setTemp(weather.now.temperature);
                    getMvpView().showWeatherInfo(weather);
                } else {
                    getMvpView().toastErrorInfo("未获取到天气信息，请刷新重试！");
                }
                getMvpView().refreshCounter();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getMvpView().toastErrorInfo("网络错误，请刷新重试！");
                getMvpView().refreshCounter();
            }
        });
    }

    public void requestForecast() {
        String address = ADDRESS_FORECAST_URL + weatherId + WEATHER_KEY;
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("zyc", "onFailure: forecast !");
                getMvpView().refreshCounter();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = response.body().string();
                final Forecast forecast = Utility.handleWeatherResponse(responseText, Forecast.class);
                if (forecast != null && "200".equals(forecast.statusCode)) {
                    getMvpView().showForecastInfo(forecast);
                } else {
                    Log.d("zyc", "forecast get failed !");
                }
                getMvpView().refreshCounter();
            }
        });
    }

    public void requestAQI() {
        String address = ADDRESS_AQI_URL + weatherId + WEATHER_KEY;
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("zyc", "onFailure: air !");
                getMvpView().refreshCounter();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = response.body().string();
                final AQI aqi = Utility.handleWeatherResponse(responseText, AQI.class);
                if (aqi != null && "200".equals(aqi.statusCode)) {
                    getMvpView().showAQIInfo(aqi);
                } else {
                    Log.d("zyc", "aqi get failed !");
                }
                getMvpView().refreshCounter();
            }
        });
    }

    public void requestSuggestion() {
        String address = ADDRESS_SUGGESTION_URL + weatherId +
                WEATHER_KEY;
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("zyc", "onFailure: suggestion !");
                getMvpView().refreshCounter();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = response.body().string();
                final Suggestion suggustion = Utility.handleWeatherResponse(responseText, Suggestion.class);
                if (suggustion != null && "200".equals(suggustion.statusCode)) {
                    getMvpView().showSuggestionInfo(suggustion);
                } else {
                    Log.d("zyc", "suggestion get failed !");
                }
                getMvpView().refreshCounter();
            }
        });
    }

    public void requestHoursForecast() {
        String address = ADDRESS_HOURS_FORECAST_URL + weatherId + WEATHER_KEY;
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("zyc", "onFailure: get hours failed !");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                HoursForecast hoursForecast = Utility.handleWeatherResponse(responseText, HoursForecast.class);
                if (hoursForecast != null && "200".equals(hoursForecast.getStatusCode())) {
                    getMvpView().refreshHoursForecast(hoursForecast);
                }
            }
        });
    }
}
