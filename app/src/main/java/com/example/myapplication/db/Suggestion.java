package com.example.myapplication.db;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Suggestion {

    @SerializedName("code")
    public String statusCode;

    @SerializedName("daily")
    public List<DailySug> dailyList;

    public class DailySug {

        @SerializedName("type")
        public String type;

        @SerializedName("name")
        public String name;

        @SerializedName("text")
        public String suggestInfo;

    }
}
