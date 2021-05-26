package com.example.myapplication.db;

import com.google.gson.annotations.SerializedName;

public class Now {

    @SerializedName("temp")
    public String temperature;

    //天气状况（多云、晴）
    @SerializedName("text")
    public String weatherCondition;

    @SerializedName("icon")
    public String weatherConditionBg;

}
