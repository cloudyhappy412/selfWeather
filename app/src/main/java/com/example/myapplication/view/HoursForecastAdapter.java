package com.example.myapplication.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.db.HoursForecast;
import com.example.myapplication.utils.Utility;

import java.util.List;

public class HoursForecastAdapter extends RecyclerView.Adapter<HoursForecastAdapter.HoursForecastViewHolder> {

    private List<HoursForecast.HourData> hoursData;

    @NonNull
    @Override
    public HoursForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hours_forecast, parent, false);
        HoursForecastViewHolder holder = new HoursForecastViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HoursForecastViewHolder holder, int position) {
        if (hoursData.size() > position) {
            holder.setData(hoursData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return hoursData != null ? hoursData.size() : 0;
    }

    public void setDataList(List<HoursForecast.HourData> hoursData) {
        this.hoursData = hoursData;
    }

    public class HoursForecastViewHolder extends RecyclerView.ViewHolder {

        private IconTextView iconText;
        private TextView time;

        public HoursForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            iconText = (IconTextView) itemView.findViewById(R.id.weather_condition_icon);
            time = (TextView) itemView.findViewById(R.id.hours_forecast_time);
        }

        public void setData(HoursForecast.HourData hourData) {
            if (hourData == null) {
                return;
            }

            iconText.setText(Utility.getResId("w" + hourData.getIconId(), R.string.class));
            time.setText(hourData.getTime().split("T")[1].split("\\+")[0]);
        }
    }

}
