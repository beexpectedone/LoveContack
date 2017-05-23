package com.wutao.lovecontack.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mingyue on 2017/5/21.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(getLayoutId());
        
        initView();
    }

    protected abstract void initView();

    public abstract int getLayoutId();
}
