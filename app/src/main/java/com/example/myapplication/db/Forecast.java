package com.example.myapplication.db;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {

    @SerializedName("code")
    public String statusCode;

    @SerializedName("daily")
    public List<Daily> dailyList;

    public class Daily {
        @SerializedName("fxDate")
        public String date;

        @SerializedName("textDay")
        public String dailyCondition;

        @SerializedName("tempMax")
        public String tempMax;

        @SerializedName("tempMin")
        public String tempMin;
    }
}

