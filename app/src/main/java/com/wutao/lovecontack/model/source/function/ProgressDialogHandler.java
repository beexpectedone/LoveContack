package com.wutao.lovecontack.model.source.function;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by mingyue on 2017/5/30.
 */

public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1; //
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private ProgressDialog mDialog;
    private Context mContext;

    public ProgressDialogHandler(Context context){
        super();
        this.mContext = context;
    }

    /**
     *
     */
    private void initDialog(){

        if(mDialog == null){
            mDialog = new ProgressDialog(mContext);
        }

        if(!mDialog.isShowing()){
            mDialog.show();
        }
    }

    /**
     *
     */
    private void dismissDialog(){
        if(mDialog != null){
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case SHOW_PROGRESS_DIALOG:
                initDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissDialog();
                break;
        }
    }
}
