package com.wutao.lovecontack.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wutao.lovecontack.model.source.function.ProgressDialogHandler;

/**
 * Created by mingyue on 2017/5/21.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public ProgressDialogHandler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new ProgressDialogHandler(this);
        
        setContentView(getLayoutId());
        
        initView();
    }

    protected abstract void initView();

    public abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null !=mHandler.mDialog){   //处理Activity被Finish掉，但dialog依然还保存activity引用，造成内存泄露的情况
            mHandler.mDialog.dismiss();
            mHandler.mDialog = null;
        }
    }
}
