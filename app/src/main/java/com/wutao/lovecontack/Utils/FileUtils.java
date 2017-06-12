package com.wutao.lovecontack.Utils;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.wutao.lovecontack.application.LoveApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by qianyue.wang on 2017/5/23.
 */

public class FileUtils {

    /**
     * 将系统相机的文件复制到自己新建的文件夹中
     */
    public static boolean writeImageFromSytem(String mphotePath,String mName){
        Bitmap bitmap = BitmapUtils.getSmallBitmap(mphotePath,480,800);
        ByteArrayOutputStream bos = BitmapUtils.compress(bitmap);


        String photoPath = LoveApplication.mApplication.getSdDir();
        String contactPath = "";
        if(!TextUtils.isEmpty(photoPath) && new File(photoPath).isDirectory()){
            contactPath = photoPath + File.separator + mName + ".jpg"; //设置图片文件存储路径
        }
        try {
            FileInputStream  fileInputS = new FileInputStream(mphotePath);

            File fileO = new File(contactPath);
            if(fileO.exists()){
                fileO.delete();
            }
            fileO.createNewFile();

            FileOutputStream fileoutP = new FileOutputStream(fileO);
//            byte[] buf = new byte[1024];
//            int size = 0;
//            while((size = fileInputS.read(buf)) != -1){
//                fileoutP.write(buf,0,size);
//            }
            fileoutP.write(bos.toByteArray());
            fileoutP.flush();
            fileoutP.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
