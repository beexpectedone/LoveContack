package com.wutao.lovecontack.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.wutao.lovecontack.R;
import com.wutao.lovecontack.model.source.function.ProgressDialogHandler;

/**
 * Created by mingyue on 2017/5/21.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public ProgressDialogHandler mHandler;
    public Toolbar toolbar;
    private LinearLayout root_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //调用父类的方法
        super.setContentView(R.layout.activity_base); //这里是具体的activity实现类调用，即调用BaseActivity中的这行代码
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){ //如果API版本大于19
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        initToolbar();
        mHandler = new ProgressDialogHandler(this);
        
        setContentView(getLayoutId());
        
        initView();
    }

    //Set the activity content from a layout resource.
    // The resource will beinflated, adding all top-level views to the activity.
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(View.inflate(this,layoutResID,null));  //源码当中说“形成这个方法调用后会”
    }


    @Override
    public void setContentView(View view) {
        root_layout = (LinearLayout) findViewById(R.id.root_layout);
        if(null == root_layout){
            return;
        }
        root_layout.addView(view,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(null != toolbar){
            setSupportActionBar(toolbar);
        }
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
