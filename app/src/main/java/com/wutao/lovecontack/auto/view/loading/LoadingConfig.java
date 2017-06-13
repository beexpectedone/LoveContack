package com.wutao.lovecontack.auto.view.loading;

/**
 * Created by mingyue on 2017/6/13.
 */

public class LoadingConfig {
    private final static LoadingFactory DEFAULT_LOADING_FACTORY = new MaterialFactory();

    private static LoadingFactory mLoadingFactory = DEFAULT_LOADING_FACTORY;

    /**
     * 全局配置
     * <p>在程序入口调用</p>
     * @param loadingFactory
     *
     */
    public static void setFactory(LoadingFactory loadingFactory/*,DialogFactory dialogFactory*/) {
        if (loadingFactory != null) {
            mLoadingFactory = loadingFactory;
        }
//        if (dialogFactory != null) {
//            mDialogFactory = dialogFactory;
//        }
    }

    public static LoadingFactory getLoadingFactory() {
        return mLoadingFactory;
    }

    public static void defaultFactory() {
        setFactory(new MaterialFactory()/*,new MaterialDialogFactory()*/);
    }
}
