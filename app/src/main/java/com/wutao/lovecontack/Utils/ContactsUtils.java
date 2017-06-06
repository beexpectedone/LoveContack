package com.wutao.lovecontack.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by qianyue.wang on 2017/5/25.
 */

public class ContactsUtils {

    public static void getContactFromContacts(EditText addContactNameET, EditText addContactNumET, Intent data, final Activity context){
        ContentResolver reContentResolverol = context.getContentResolver();
        Uri contactData = data.getData();
        if (contactData == null) {
            return;
        }
//        Cursor cursor = context.managedQuery(contactData, null, null, null, null);
        Cursor cursor =context.getContentResolver().query(contactData,null,null,null,null);
        if (cursor == null) {
            return;
        }
        if (!cursor.moveToFirst()) {
            // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面
            new AlertDialog.Builder(context)
                    .setMessage("app需要开启读取联系人权限")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + context.getPackageName()));
                            context.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create()
                    .show();
        }else{
            String userName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null,
                    null);
            String userNumber = "";
            if(null != phone){
                while (phone.moveToNext()) {
                    userNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (TextUtils.isEmpty(userNumber)) {
                        break;
                    }
                }
                if (!TextUtils.isEmpty(userNumber)) {
                    userNumber = CommonUtils.getFormedString(userNumber);
                }
                //如果userName为空，userNumber不为空，说明只有号码没设姓名
                if (TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userNumber)) {
                    addContactNameET.setText(userNumber);
                    addContactNumET.setText(userNumber);
                }
                //如果userName不为空，userNumber为空,只有号码没有联系人
                if (!TextUtils.isEmpty(userName) && TextUtils.isEmpty(userNumber)) {
                    ToastUtils.showShortToast(context,"无效号码");
                }
                if (!TextUtils.isEmpty(userNumber) && !TextUtils.isEmpty(userName)) {
                    addContactNameET.setText(userName);
                    addContactNumET.setText(userNumber);
                }
                phone.close();
            }else {
                ToastUtils.showShortToast(context,"无联系人信息");
            }
        }
    }
}
