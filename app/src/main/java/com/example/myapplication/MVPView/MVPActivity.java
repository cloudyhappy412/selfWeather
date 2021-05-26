package com.example.myapplication.MVPView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class MVPActivity<P extends IMVPPresenter> extends AppCompatActivity implements IMVPView<P>{

    private P mPresenter;
    protected abstract P createPresenter();

    protected abstract int getContentView();

    @Override
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mPresenter = createPresenter();
        bindView();
        initData();
        initView();

    }

    /**
     * bindView 绑定View
     * initData 获取回传的数据
     * initView view相关的操作
     */
    protected abstract void bindView();
    protected abstract void initData();
    protected abstract void initView();

}
