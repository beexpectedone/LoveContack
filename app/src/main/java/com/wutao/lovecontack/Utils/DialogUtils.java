package com.wutao.lovecontack.Utils;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by mingyue on 2017/5/31.
 */

public class DialogUtils {

    public static void showDialog(Context context, String title, String message, DialogInterface.OnClickListener mPostiveListener, DialogInterface.OnClickListener mNegativeListener) {
  /*
  这里使用了 android.support.v7.app.AlertDialog.Builder
  可以直接在头部写 import android.support.v7.app.AlertDialog
  那么下面就可以写成 AlertDialog.Builder
  */
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("取消", mNegativeListener);
        builder.setPositiveButton("确定", mPostiveListener);
        builder.show();
    }
}
