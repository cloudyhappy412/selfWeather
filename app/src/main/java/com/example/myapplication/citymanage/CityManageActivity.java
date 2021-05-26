package com.example.myapplication.citymanage;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;

import com.example.myapplication.ChooseAreaActivity;
import com.example.myapplication.MVPView.MVPActivity;
import com.example.myapplication.R;
import com.example.myapplication.WeatherActivity;
import com.example.myapplication.db.City;
import com.example.myapplication.view.OveruseCityAdapter;
import com.example.myapplication.view.TitleView;

import org.litepal.crud.DataSupport;

import java.util.List;

public class CityManageActivity extends MVPActivity<CityManagePresenter> {

    private TitleView titleView;
    private RecyclerView recyclerView;
    private OveruseCityAdapter adapter;
    private String cityId, temp, cityName;
    private int selectPosition;

    @Override
    protected CityManagePresenter createPresenter() {
        return new CityManagePresenter(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_city_manage;
    }

    @Override
    protected void bindView() {
        titleView = (TitleView) findViewById(R.id.title_view);
        recyclerView = (RecyclerView) findViewById(R.id.over_recycler_view);
    }

    @Override
    protected void initData() {
        cityId = getIntent().getStringExtra("city_id");
        cityName = getIntent().getStringExtra("city_name");
        temp = getIntent().getStringExtra("temperature");
        if (cityId == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            cityId = preferences.getString("city_id", null);
            cityName = preferences.getString("city_name", null);
            temp = "24";
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("weather_id", cityId);
            intent.putExtra("city_name", cityName);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new OveruseCityAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnCitySelectListener((cityName, weatherId) -> {
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("weather_id", weatherId);
            intent.putExtra("city_name", cityName);
            startActivity(intent);
            finish();
        });
        List<City> cityList = DataSupport.findAll(City.class);

        adapter.setOnCityDeleteListener(pos -> {
            if (cityList != null && cityList.size() > pos) {
                City city = cityList.remove(pos);
                city.delete();
                if (cityList.size() > 0) {
                    city = cityList.get(0);
                    cityId = city.getCityId();
                    cityName = city.getCityName();
                } else {
                    cityId = cityName = null;
                }
                adapter.setDataList(cityList);
                adapter.notifyDataSetChanged();
            }
        });
        titleView.setOnBackListener(() -> {
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("weather_id", cityId);
            intent.putExtra("city_name", cityName);
            startActivity(intent);
            finish();
        });
        titleView.setOnAddClickListener(() -> {
            Intent intent = new Intent(this, ChooseAreaActivity.class);
            startActivity(intent);
            finish();
        });


        if (cityList != null && cityList.size() != 0) {
            adapter.setSelectCityId(cityId);
            adapter.setTemperature(temp);
            adapter.setDataList(cityList);
            adapter.notifyDataSetChanged();
        } else {
            Log.d("zyc", "cityManager failed !");
        }
    }
}