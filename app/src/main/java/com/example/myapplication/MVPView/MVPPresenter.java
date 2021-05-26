package com.example.myapplication.MVPView;

import android.app.Activity;
import android.content.Context;
import com.example.myapplication.MVPView.IMVPPresenter;
import com.example.myapplication.MVPView.IMVPView;

public abstract class MVPPresenter<V extends IMVPView> implements IMVPPresenter {

    private V MVPView;
    private Context mContext;

    public MVPPresenter(V activity) {
        MVPView = activity;
        mContext = (Context) activity;
    }

    @Override
    public V getMvpView() {
        return MVPView == null ? null : MVPView;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

}
