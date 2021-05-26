package com.example.myapplication.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.db.City;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    public interface OnItemListener {
        void onItemClick(City city);
    }

    private List<City> cityList;
    private OnItemListener onItemListener;

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_holder, parent, false);
        CityViewHolder viewHolder = new CityViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        if (cityList != null && cityList.size() > position) {
            City city = cityList.get(position);
            holder.setData(city);
            holder.itemView.setOnClickListener(v -> {
                if (onItemListener != null) {
                    onItemListener.onItemClick(city);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cityList != null ? cityList.size() : 0;
    }

    public void setDataList(List<City> cityList) {
        this.cityList = cityList;
    }

    public void setOnItemClickLister(OnItemListener onItemClickLister) {
        this.onItemListener = onItemClickLister;
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {
        private TextView cityName;
        private View divider;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.city_name);
            divider = itemView.findViewById(R.id.divider);
        }

        public void setData(City city) {
            int position = getAdapterPosition();
            if (position == getItemCount() - 1) {
                divider.setVisibility(View.GONE);
            } else {
                divider.setVisibility(View.VISIBLE);
            }
            if (city != null) {
                cityName.setText(city.getCityName());
            }
        }

    }
}
