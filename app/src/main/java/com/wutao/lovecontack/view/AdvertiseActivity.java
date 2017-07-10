package com.wutao.lovecontack.view;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.wutao.lovecontack.R;
import com.wutao.lovecontack.model.AdvertiseBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qianyue.wang on 2017/6/7.
 */

public class AdvertiseActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.advertise_iv)
    ImageView advertiseIV;
    @BindView(R.id.scrip_tv)
    TextView scripTV;
    private long time;
    private TimeCount timeCount;
    private int code;
    private AdvertiseBean advertiseBean;

    PackageManager mP;
    ComponentName def;
    ComponentName mBazaar;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scrip_tv:
                Intent intent = new Intent();
                intent.setClass(AdvertiseActivity.this, MainActivity.class);
                startActivity(intent);
                AdvertiseActivity.this.finish();
                break;
            case R.id.advertise_iv:
//                Intent intent2 = new Intent();
//                intent2.setClass(this,MainActivity.class);
//                startActivity(intent2);
//                this.finish();
                break;
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);
        initView();
        initIcon();
    }

    private void initIcon() {
        mP = getApplicationContext().getPackageManager();
        def = new ComponentName(getBaseContext(),"com.wutao.lovecontack.view.AdvertiseActivity");
        mBazaar= new ComponentName(getBaseContext(),"com.wutao.lovecontack.icon");
    }

    //执行此方法改变图标
    private void setIconSc() {
        disableComponent(def); //禁用之前的图标
        enabledComponent(mBazaar);}

    private void setIconWm() {
        disableComponent(mBazaar);
        enabledComponent(def);}//显示快捷图标

    private void enabledComponent(ComponentName name) {
        mP.setComponentEnabledSetting(name, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
    private void disableComponent(ComponentName name) {
        mP.setComponentEnabledSetting(name, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    public void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        scripTV.setOnClickListener(this);
        advertiseIV.setOnClickListener(this);
        timeCount = new TimeCount(2000,1000);
        timeCount.start();
        getImageAndHandle(initBackGroundImage());
    }

    private AdvertiseBean initBackGroundImage() {
        advertiseBean = new AdvertiseBean();
        advertiseBean.setCode(1);
        advertiseBean.setTime(2 * 1000);
        return advertiseBean;
    }

    private void getImageAndHandle(AdvertiseBean advertiseBean) {
        time = advertiseBean.getTime();
        code = advertiseBean.getCode();
        String url = advertiseBean.getUrl();
        if (code == 9) {//如果code为9，说明是要进行更新操作
            //如果code为9，那么就将button按钮隐藏掉
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else { //code 为0时有新图片，需要去URL地址下载新图片
            //            String imagePath = ImageUtil.ImageCachePath + File.separator + ConfigName.advertiseBitmap;
            //            Bitmap advertiseBit = ImageUtil.getCompressBitmapFromSD(imagePath,this);
            //            scripTV.setVisibility(View.VISIBLE);
            //            advertiseIV.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //            if(null == advertiseBit){
            //                advertiseBit = ImageUtil.getBitmap(this,R.drawable.splash1);
            //                ImageUtil.writerToSDCard(imagePath,advertiseBit,100); //如果默认的图片为空，就把drawable当中图片写入默认的文件
            //            }
            //            advertiseIV.setImageBitmap(advertiseBit);
            //            if(time > 0){
            //                timeCount = new TimeCount(time,1000);
            //                timeCount.start();
            //            }
            //            if(code == 0){
            //                new BitmapCache().getBitmapAndSave(this,url); //调用方法将图片存入
            //            }

//            scripTV.setVisibility(View.VISIBLE);
//            advertiseIV.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            Bitmap advertiseBit = ImageUtil.getBitmap(this, R.drawable.welcome);
//            if (null != advertiseBit) {
//                advertiseIV.setImageBitmap(advertiseBit);
//            }
//            if (time > 0) {
//                timeCount = new TimeCount(time, 1000);
//                timeCount.start();
//            }
        }
    }

    public class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /*
        * 在倒计时期间显示的文字信息*/
        @Override
        public void onTick(long millisUntilFinished) {
            scripTV.setText(millisUntilFinished / 1000 + "跳过");
        }

        /*
        * 倒计时结束的时候要调用的方法*/
        @Override
        public void onFinish() {
            Intent intent = new Intent();
            intent.setClass(AdvertiseActivity.this, MainActivity.class);  //无论如何都要跳转到MainActivity中
            startActivity(intent);
            AdvertiseActivity.this.finish();
        }
    }
    @Override
    public void onBackPressed() {

    }
}
