package com.wutao.lovecontack.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by qianyue.wang on 2017/6/7.
 */

public class SharedPreferencesUtils {
    public static String getValue(Context context, String key) {
        SharedPreferences sharedata = context.getSharedPreferences("message_save",Context.MODE_PRIVATE);
        String phonenumber = sharedata.getString(key, "");
        return phonenumber;
    }

    public static Boolean getTrueOrFalse(Context context,String key){
        SharedPreferences sharedata = context.getSharedPreferences("message_save",Context.MODE_PRIVATE);
        Boolean b=sharedata.getBoolean(key, false);
        return b;
    }

    public static void setTrueOrFalse(Context context,String key,Boolean value){
        SharedPreferences sharedata = context.getSharedPreferences("message_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor er=sharedata.edit();
        er.putBoolean(key, value);
        er.commit();
    }

    public static void setValue(Context context,String key,String value) {
        SharedPreferences sharedata = context.getSharedPreferences("message_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor er=sharedata.edit();
        er.putString(key, value);
        er.commit();
    }
    public static void clearAll(Context context) {
        SharedPreferences sharedata = context.getSharedPreferences("message_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor er=sharedata.edit();
        er.clear();
        er.commit();
    }
}
