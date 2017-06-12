package com.wutao.lovecontack.auto.view.loading;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

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

    /**
     * 循环方法获取到父节点
     * 找到合适的父布局
     *
     * @param parent
     * @return
     */

    private static ViewGroup findSuitableParent(View parent){
        if(null == parent){
            return null;
        }
        View suitableParent = parent;
        do {
            if (suitableParent instanceof FrameLayout || suitableParent instanceof RelativeLayout ||
                    "android.support.v4.widget.DrawerLayout".equals(suitableParent.getClass().getName()) ||
                    "android.support.design.widget.CoordinatorLayout".equals(suitableParent.getClass().getName()) ||
                    "android.support.v7.widget.CardView".equals(suitableParent.getClass().getName())){
                return (ViewGroup) suitableParent;
            }else {
                final ViewParent viewParent = suitableParent.getParent();
                suitableParent = viewParent instanceof View ? (View) viewParent : null;
                return (ViewGroup) suitableParent;
            }

        }while (suitableParent != null);//循环执行，一直到找到一个合适的父节点
    }
}
