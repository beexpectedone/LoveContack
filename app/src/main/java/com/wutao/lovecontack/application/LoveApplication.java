package com.wutao.lovecontack.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.wutao.lovecontack.view.MainActivity;
import com.zxy.recovery.callback.RecoveryCallback;
import com.zxy.recovery.core.Recovery;

import java.io.File;

import me.qianyue.dao.DaoMaster;
import me.qianyue.dao.DaoSession;

/**
 * Created by qianyue.wang on 2017/5/23.
 */

public class LoveApplication extends Application {
    public static LoveApplication mApplication;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private String sdDir;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        initRecovery();
        initDataBase();
        initDir();
    }

    /**
     * 初始化项目的文件夹
     */
    private void initDir() {
        //获取根目录
        sdDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "LOVEC";
        File file = new File(sdDir);
        if(!file.exists() ){
            file.mkdirs();
        }
    }

    private void initDataBase() {
        //通过DaoMaster 的内部类 DevOpenHelper， 得到一个便利的SQLiteOpenHelper。
        //升级时会默认删除掉所有的表，所以需要进一步封装实现数据库的安全升级。
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"contacts-db",null);
        db = helper.getWritableDatabase();

        //该数据库连接属于DaoMaster,所以多个Session值的是相同对的数据库连接
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    /**
     * 埋点，捕捉运行时的bug
     */
    private void initRecovery() {
        Recovery.getInstance()
                .debug(true)
                .recoverInBackground(false)
                .recoverStack(true)
                .mainPage(MainActivity.class)
                .recoverEnabled(true)
                .callback(new MyCrashCallback())
                .silent(false,Recovery.SilentMode.RECOVER_ACTIVITY_STACK)
//                .skip(AddEditNewContactActivity.class)
                .init(this);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public String getSdDir() {
        return sdDir;
    }

    public void setSdDir(String sdDir) {
        this.sdDir = sdDir;
    }

    static final class MyCrashCallback implements RecoveryCallback {
        @Override
        public void stackTrace(String exceptionMessage) {
            Log.e("zxy", "exceptionMessage:" + exceptionMessage);
        }

        @Override
        public void cause(String cause) {
            Log.e("zxy", "cause:" + cause);
        }
        @Override
        public void exception(String exceptionType, String throwClassName, String throwMethodName, int throwLineNumber) {
            Log.e("zxy", "exceptionClassName:" + exceptionType);  //异常类型
            Log.e("zxy", "throwClassName:" + throwClassName); //异常的类名
            Log.e("zxy", "throwMethodName:" + throwMethodName); //异常的方法名
            Log.e("zxy", "throwLineNumber:" + throwLineNumber); //异常所在的行数
        }
        @Override
        public void throwable(Throwable throwable) {

        }
    }
}
