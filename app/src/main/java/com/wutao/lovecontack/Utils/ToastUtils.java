package com.wutao.lovecontack.Utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by qianyue.wang on 2017/5/23.
 */

public class ToastUtils {

    /**
     * 长时间显示消息
     *
     * @param context 上下文对象
     * @param message 消息内容
     */
    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时间显示消息
     *
     * @param context 上下文对象
     * @param message 消息内容
     */
    public static void showShortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

//    public static void setImage(Context context, int imageResourceId) {
//        if (toast == null) {
//            toast = new CustomToast(context);
//        }
////        toast.setImage(imageResourceId);
//    }

    public static void showShortSnackbar(View view, String content){
        Snackbar.make(view,content, Snackbar.LENGTH_SHORT)
                .show();
    }
    //
    public static void showLongSnackbar(View view, String content){
        Snackbar.make(view,content, Snackbar.LENGTH_LONG)
                .show();
    }
}
