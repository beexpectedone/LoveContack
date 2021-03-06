package com.wutao.lovecontack.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qianyue.wang on 2017/5/23.
 */

public class CommonUtils {
    /**
     * 判断姓名的合法性
     * @param name
     * @return
     */
    public static boolean isName(String name){
        boolean flag = false;
        String regEx = "^[\\u4e00-\\u9fa5]+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(name);

        if (m.matches()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 获取到特定的字符串
     * @param string
     * @return
     */
    public static String getFormedString(String string) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);
        string = m.replaceAll("").toString();
        return string;
    }
}
