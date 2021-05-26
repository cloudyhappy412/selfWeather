package com.example.myapplication.db;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HoursForecast {
    @SerializedName("code")
    private String statusCode;

    @SerializedName("hourly")
    private List<HourData> hoursData;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<HourData> getHoursData() {
        return hoursData;
    }

    public void setHoursData(List<HourData> hoursData) {
        this.hoursData = hoursData;
    }

    public class HourData {
        @SerializedName("fxTime")
        private String time;

        @SerializedName("temp")
        private String temp;

        @SerializedName("icon")
        private String iconId;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getIconId() {
            return iconId;
        }

        public void setIconId(String iconId) {
            this.iconId = iconId;
        }
    }
}
