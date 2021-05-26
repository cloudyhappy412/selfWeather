package com.example.myapplication.MVPView;

import android.content.Context;

public interface IMVPPresenter <V extends IMVPView>{
    V getMvpView();
    Context getContext();
}
