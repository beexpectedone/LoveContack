package com.wutao.lovecontack.auto.view.loading;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mingyue on 2017/6/1.
 *
 *
 */

public interface LoadingFactory {

    /**
     * 返回的view，决定了loading长什么样
     * @param parent
     * @return
     */
    View onCreateView(ViewGroup parent);
}
