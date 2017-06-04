package com.wutao.lovecontack.auto.view.loading;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mingyue on 2017/6/1.
 */

public class LoadingBar implements IloadingBar{

    private ViewGroup mParent;
    private View mView;
    private OnLoadingBarListener mListener;

    private LoadingBar(ViewGroup parent, LoadingFactory factory) {
        mParent = parent;
        mView = factory.onCreateView(mParent);
    }

    /**
     * 显示loading
     */
    @Override
    public void show() {
        if (mView != null) {
            mView.setVisibility(View.VISIBLE);
            if (mView.getParent() != null) {
                mParent.removeView(mView);
            }
            mParent.addView(mView);
        }
    }

    /**
     * 取消loading
     */
    @Override
    public void cancel() {
        if (mView != null) {
            mView.setVisibility(View.GONE);
            mParent.removeView(mView);
            mView = null;
            if (this.mListener != null) {
                this.mListener.onCancel(mParent);
            }
        }
    }




}
