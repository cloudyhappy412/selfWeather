package com.example.myapplication.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.myapplication.ChooseAreaActivity;
import com.example.myapplication.R;

public class TitleView extends RelativeLayout {

    public interface OnBackListener {
        void onBack();
    }

    public interface OnAddClickListener {
        void onAdd();
    }

    private ImageView backIcon;
    private ImageView addIcon;
    private OnBackListener onBackListener;
    private OnAddClickListener onAddClickListener;

    public TitleView(Context context) {
        super(context);
        init();
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.main_title_view, this, true);
        backIcon = (ImageView) findViewById(R.id.back_icon);
        addIcon = (ImageView) findViewById(R.id.add_icon);
        backIcon.setOnClickListener(view -> {
            if (onBackListener != null) {
                onBackListener.onBack();
            }
        });
        addIcon.setOnClickListener(view -> {
            if (onAddClickListener != null) {
                onAddClickListener.onAdd();
            }
        });
    }

    public void setOnBackListener(OnBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    public void setOnAddClickListener(OnAddClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }
}
