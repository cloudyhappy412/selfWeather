package com.example.myapplication.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.example.myapplication.R;
import com.example.myapplication.db.City;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static List<City> handleCitiesResponse(String response) {
        if (TextUtils.isEmpty(response)) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray allCities = jsonObject.getJSONArray("location");
            List<City> cities = new ArrayList<>();
            for (int i = 0; i < allCities.length(); ++i) {
                JSONObject cityObject = allCities.getJSONObject(i);
                City city = new City();
                city.setCityName(cityObject.getString("name"));
                city.setCityId(cityObject.getString("id"));
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static <T> T handleWeatherResponse(String response, Class<T> classOfT) {
        if (TextUtils.isEmpty(response)) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject(response);
            T t = new Gson().fromJson(jsonObject.toString(), classOfT);
            Log.d("zyc", "get: ");
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("zyc", "except");
        }
        return null;
    }

    public static String handleBingPicResponse(String response) {
        if (TextUtils.isEmpty(response)) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("images");
            if (jsonArray != null && jsonArray.length() >= 0) {
                JSONObject object = jsonArray.getJSONObject(0);
                String url = object.getString("url");
                return url;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("zyc", "handleBingPicResponse: failed !");
        }
        return null;
    }

    public static int getResId(String name, Class<?> cls) {
        try {
            Field idField = cls.getDeclaredField(name);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return R.string.w100;
        }
    }

    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        if (manager != null) {
            manager.getDefaultDisplay().getRealMetrics(dm);
            return dm.heightPixels;
        }

        return 0;
    }

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        if (manager != null) {
            manager.getDefaultDisplay().getRealMetrics(dm);
            return dm.widthPixels;
        }

        return 0;
    }
}
