package com.example.myapplication.db;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("updateTime")
    public String updateTime;

    @SerializedName("code")
    public String statusCode;

    @SerializedName("now")
    public Now now;

}
