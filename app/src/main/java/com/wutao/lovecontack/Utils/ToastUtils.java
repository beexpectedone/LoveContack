package com.wutao.lovecontack.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
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

    public static void showShortToastOnUIThrea(final Activity context, final String message){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
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

    public static void dismissDialog(final Activity mAct, final ProgressDialog mDialog,
                                     final String message, final boolean needFinishContext){
        mAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null!= mDialog && mDialog.isShowing()){
                    mDialog.dismiss();
                }
                if(!TextUtils.isEmpty(message)){
                    ToastUtils.showShortToast(mAct,message);
                }
                if(needFinishContext){
                    mAct.finish();
                }
            }
        });
    }
}
