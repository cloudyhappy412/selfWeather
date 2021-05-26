package com.example.myapplication.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.db.City;

import java.util.List;

public class OveruseCityAdapter extends RecyclerView.Adapter<OveruseCityAdapter.OveruseCityViewHolder> {

    public interface OnCitySelectListener {
        void onSelect(String cityName, String cityId);
    }

    public interface OnCityDeleteListener {
        void onDelete(int position);
    }

    private List<City> dataList;
    private OnCitySelectListener listener;
    private String selectCityId;
    private String temperature;
    private OnCityDeleteListener onCityDeleteListener;

    @NonNull
    @Override
    public OveruseCityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.overuse_city, parent, false);
        OveruseCityViewHolder holder = new OveruseCityViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OveruseCityViewHolder holder, int position) {
        City city = dataList.get(position);
        holder.setData(city.getCityName(), city.getCityId().equals(selectCityId) ? temperature : "" + (18 + System.currentTimeMillis() % 8), city.getCityId().equals(selectCityId));
        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onSelect(city.getCityName(), city.getCityId());
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            SlideUpDialog dialog = new SlideUpDialog(v.getContext(), R.style.normalDialogStyle);

            dialog.setOnDeleteClickListener(() -> {
                if (onCityDeleteListener != null) {
                    onCityDeleteListener.onDelete(position);
                }
            });

            dialog.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public void setDataList(List<City> cityList) {
        this.dataList = cityList;
    }

    public class OveruseCityViewHolder extends RecyclerView.ViewHolder {

        private TextView cityName;
        private ImageView currentCityIcon;
        private TextView temp;

        public OveruseCityViewHolder(@NonNull View itemView) {
            super(itemView);
            init(itemView);
        }

        private void init(View itemView) {
            cityName = itemView.findViewById(R.id.overuse_city_name);
            currentCityIcon = itemView.findViewById(R.id.current_city);
            temp = itemView.findViewById(R.id.temp);
        }

        public void setData(String name, String t, boolean isCurrentCity) {
            cityName.setText(name);
            currentCityIcon.setVisibility(isCurrentCity ? View.VISIBLE : View.GONE);
            temp.setText(t + "˚C");
        }
    }

    public OnCityDeleteListener getOnCityDeleteListener() {
        return onCityDeleteListener;
    }

    public void setOnCityDeleteListener(OnCityDeleteListener onCityDeleteListener) {
        this.onCityDeleteListener = onCityDeleteListener;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setSelectCityId(String selectCityId) {
        this.selectCityId = selectCityId;
    }

    public OnCitySelectListener getOnCitySelectListener() {
        return listener;
    }

    public void setOnCitySelectListener(OnCitySelectListener listener) {
        this.listener = listener;
    }
}
