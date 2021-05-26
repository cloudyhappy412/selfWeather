package com.example.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.citymanage.CityManageActivity;
import com.example.myapplication.db.City;
import com.example.myapplication.utils.HttpUtil;
import com.example.myapplication.utils.Utility;
import com.example.myapplication.view.CityAdapter;

import org.jetbrains.annotations.NotNull;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment {

    private ProgressDialog progressDialog;
    private EditText editText;
    private CityAdapter adapter;

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        editText = (EditText) view.findViewById(R.id.edit_text);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new CityAdapter();
        adapter.setOnItemClickLister(city -> {

            if (!isDuplicated(city)) {
                city.save();
            }
            Intent intent = new Intent(getActivity(), CityManageActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        showKeyboard();

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("zyc", "onEditorAction: " + actionId);
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d("zyc", "onEditorAction: get here ! ");
                    String searchInfo = editText.getText().toString();
                    String address = "https://geoapi.qweather.com/v2/city/lookup?location=" + searchInfo + "&range=cn&key=51c36aefb4c04d3e873ee71e3d54625e";
                    showProgressDialog();
                    HttpUtil.sendOkHttpRequest(address, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            getActivity().runOnUiThread(() -> {
                                closeProgressDialog();
                                Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT);
                            });
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String responseText = response.body().string();
                            List<City> cityList = Utility.handleCitiesResponse(responseText);
                            getActivity().runOnUiThread(() -> {
                                closeProgressDialog();
                                if (cityList != null) {
                                    adapter.setDataList(cityList);
                                    adapter.notifyDataSetChanged();
                                    Log.d("zyc", "onResponse: cities got !");
                                } else {
                                    Log.d("zyc", "onResponse: not get cities !");
                                }
                                InputMethodManager im = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                if (im != null) {
                                    im.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                                }
                            });
                        }
                    });
                }
                return false;
            }
        });
        return view;
    }

    private void showKeyboard() {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
    private boolean isDuplicated(City city) {
        for (City c : DataSupport.findAll(City.class)) {
            if (city.getCityName().equals(c.getCityName()) && city.getCityId().equals(c.getCityId())) {
                return true;
            }
        }
        return false;
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }

        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
