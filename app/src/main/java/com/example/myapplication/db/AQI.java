package com.example.myapplication.db;

import com.google.gson.annotations.SerializedName;

public class AQI {

    @SerializedName("code")
    public String statusCode;

    @SerializedName("now")
    public Now now;

    public class Now {

        @SerializedName("category")
        public String category;

        @SerializedName("aqi")
        public String aqi;

        @SerializedName("pm2p5")
        public String pm25;
    }
}
