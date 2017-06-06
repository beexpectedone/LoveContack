package com.wutao.lovecontack.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wutao.lovecontack.R;
import com.wutao.lovecontack.Utils.BitmapUtils;
import com.wutao.lovecontack.Utils.ContactsUtils;
import com.wutao.lovecontack.Utils.ToastUtils;
import com.wutao.lovecontack.contract.AddEditContactContract;
import com.wutao.lovecontack.model.ContactBean;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by mingyue on 2017/5/21.
 */

public class AddEditNewContactFragment extends BaseFragment implements AddEditContactContract.View, View.OnClickListener {

    @BindView(R.id.rootViewLV)
    LinearLayout rootViewLV;
    @BindView(R.id.addContactNumET)
    EditText addContactNumET;
    //    @BindView(R.id.addContactNum2ET)
//    EditText addContactNum2ET;
    @BindView(R.id.selectTV)
    TextView selectTV;
    @BindView(R.id.contactIM)
    ImageView contactIM;
    @BindView(R.id.addContactNameET)
    EditText addContactNameET;
    @BindView(R.id.getPhotoTV)
    TextView getPhotoTV;

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";
    public final static int REQUEST_IMAGE = 2;
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0xb1;
    private final int REQUESTCODE_READ_CONTACTS = 0xb2;

    private String mPhotoPath = "";
    private AddEditContactContract.Presenter mAddContactPresenter;
    private String mEditedContactId;
    private String mNumber;
    private String mName;
    private String mNumber2;
    private String contactPath = "";
    private Bitmap bitmap;

    public static AddEditNewContactFragment newInstance() {
        return new AddEditNewContactFragment();
    }

    public AddEditNewContactFragment() {

    }


    @Override
    public void onResume() {
        super.onResume();
        mAddContactPresenter.start();
    }


    @Override
    protected void initView() {
        selectTV.setOnClickListener(this);
        getPhotoTV.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_addcontact;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTaskIdIfAny();

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task_done);

        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(this);
    }


    @Override
    public void setName(ContactBean contactBean) {
        if (!TextUtils.isEmpty(mName)) {
            contactBean.setName(mName);
        }

    }

    @Override
    public void setNumber(ContactBean contactBean) {
        if (!TextUtils.isEmpty(mNumber)) {
            contactBean.setName(mNumber);
        }

    }

    public void setmPhotoPath(ContactBean contactBean) {
        if (!TextUtils.isEmpty(contactPath)) {
            contactBean.setPhotoPath(contactPath);
        }
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void showEmptyContactError() {
        ToastUtils.showShortSnackbar(rootViewLV, "保存失败请重试！");
    }

    @Override
    public void showContactsList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }


    @Override
    public void setPresenter(AddEditContactContract.Presenter presenter) {
        if (null != presenter) {
            mAddContactPresenter = presenter;
        }
    }

    private void setTaskIdIfAny() {
        if (getArguments() != null && getArguments().containsKey(ARGUMENT_EDIT_TASK_ID)) {
            mEditedContactId = getArguments().getString(ARGUMENT_EDIT_TASK_ID);
        }
    }

    private boolean isNewTask() {
        return mEditedContactId == null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getPhotoTV:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
            case R.id.fab_edit_task_done:
                if (isNewTask()) {
                    mNumber = addContactNumET.getText().toString().trim();
                    if (TextUtils.isEmpty(mNumber)) {
                        ToastUtils.showLongSnackbar(rootViewLV, "号码不能为空");
                        return;
                    }

//                    mNumber2 = addContactNum2ET.getText().toString().trim();
//                    if(TextUtils.isEmpty(mNumber2)){
//                        ToastUtils.showLongSnackbar(rootViewLV,"号码2不能为空");
//                        return;
//                    }
//                    double number2Dou = Double.parseDouble(mNumber2);

                    mName = addContactNameET.getText().toString().trim();
                    if (TextUtils.isEmpty(mName)) {
                        ToastUtils.showLongSnackbar(rootViewLV, "姓名不能为空");
                        return;
                    }
//                    if (!CommonUtils.isName(mName)) {
//                        ToastUtils.showLongSnackbar(rootViewLV, "姓名输入有误");
//                        return;
//                    }
                    /**这里要有获取到Image图片路径的操作*/
                    if (null == bitmap) {
                        ToastUtils.showShortSnackbar(rootViewLV, "图片有误");
                        return;
                    }
                    if (TextUtils.isEmpty(mPhotoPath)) {
                        ToastUtils.showShortSnackbar(rootViewLV, "获取图片失败");
                        return;
                    }

                    if (null != mAct) {
                        mAddContactPresenter.saveContact(contactDao, mPhotoPath, mName, mNumber, 0, mAct);
                    }
                }
                break;
            case R.id.selectTV:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    startActivityForResult(new Intent(
                            Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUESTCODE_READ_CONTACTS);
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE) {
            Log.i("qqliLog", "GalleryUri:    " + data.getData());
            Uri uriImg = data.getData();
            if (null != uriImg) {
                mPhotoPath = BitmapUtils.getRealPathFromURI(uriImg, getContext());
                bitmap = BitmapUtils.getSmallBitmap(mPhotoPath, 480, 800);
                if (null != bitmap) {
                    contactIM.setImageBitmap(bitmap);
                }
            }
        } else if (requestCode == REQUESTCODE_READ_CONTACTS) {
            if (null != data) { //测试魅族系统在这里会存在问题
                ContactsUtils.getContactFromContacts(addContactNameET, addContactNumET, data, getActivity());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                if (null != grantResults && grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(new Intent(
                            Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUESTCODE_READ_CONTACTS);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != bitmap) {
            bitmap.recycle();
        }
    }
}
