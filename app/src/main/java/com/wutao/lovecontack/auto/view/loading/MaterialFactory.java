package com.wutao.lovecontack.auto.view.loading;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wutao.lovecontack.R;

/**
 * Created by mingyue on 2017/6/13.
 */

public class MaterialFactory implements LoadingFactory {
    @Override
    public View onCreateView(ViewGroup parent) {
        View loadingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_progressbar_vertical_material, parent,false);
        return loadingView;
    }
}
