package com.wutao.lovecontack.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wutao.lovecontack.application.LoveApplication;

import butterknife.ButterKnife;
import me.qianyue.dao.ContactDao;
import me.qianyue.dao.DaoSession;

/**
 * Created by mingyue on 2017/5/31.
 */

public abstract class BaseFragment extends Fragment {

    public ContactDao contactDao;
    protected Activity mAct;

    public BaseFragment(){
        initDao();
    }

    /**
     * 获取到数据库的到文件
     */
    protected void initDao() {
        DaoSession daoSession = LoveApplication.mApplication.getDaoSession();
        contactDao = daoSession.getContactDao();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAct = getActivity();

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutId(),null);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    protected abstract void initView();

    protected abstract int getLayoutId();
}
