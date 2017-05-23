package com.wutao.lovecontack.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wutao.lovecontack.R;
import com.wutao.lovecontack.Utils.BitmapUtils;
import com.wutao.lovecontack.contract.AddEditContactContract;
import com.wutao.lovecontack.model.ContactBean;

import static android.app.Activity.RESULT_OK;

/**
 * Created by mingyue on 2017/5/21.
 */

public class AddEditNewContactFragment extends Fragment implements AddEditContactContract.View,View.OnClickListener {

//    @BindView(R.id.addContactNumET)
    EditText addContactNumET;
//    @BindView(R.id.selectTV)
    TextView selectTV;
//    @BindView(R.id.contactIM)
    ImageView contactIM;
//    @BindView(R.id.addContactNameET)
    EditText addContactNameET;
//    @BindView(R.id.getPhotoTV)
    TextView getPhotoTV;

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";
    public final static int REQUEST_IMAGE = 2;

    private String mPhotoPath = "";

    private AddEditContactContract.Presenter mAddContactPresenter;

    private String mEditedContactId;
    private String mNumber;
    private String mName;

    public static AddEditNewContactFragment newInstance(){
        return new AddEditNewContactFragment();
    }

    public AddEditNewContactFragment(){

    }


    @Override
    public void onResume() {
        super.onResume();
        mAddContactPresenter.start();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addcontact,container,false);
//        ButterKnife.bind(getActivity(),root);

        selectTV = (TextView) root.findViewById(R.id.selectTV);
        selectTV.setOnClickListener(this);
        getPhotoTV = (TextView) root.findViewById(R.id.getPhotoTV);
        getPhotoTV.setOnClickListener(this);
        contactIM = (ImageView) root.findViewById(R.id.contactIM);



        return root;
    }

    @Override
    public void setName(ContactBean contactBean) {
        if(!TextUtils.isEmpty(mName)){
            contactBean.setName(mName);
        }

    }

    @Override
    public void setNumber(ContactBean contactBean) {
        if(!TextUtils.isEmpty(mNumber)){
            contactBean.setName(mNumber);
        }

    }

    public void setmPhotoPath(ContactBean contactBean) {
        if(!TextUtils.isEmpty(mPhotoPath)){
            contactBean.setPhotoPath(mPhotoPath);
        }
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void showEmptyContactError() {

    }

    @Override
    public void showContactsList() {

    }


    @Override
    public void setPresenter(AddEditContactContract.Presenter presenter) {
        if(null != presenter){
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
        switch (v.getId()){
            case R.id.getPhotoTV:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
            case R.id.fab_edit_task_done:
                if (isNewTask()) {
                    mNumber = addContactNumET.getText().toString().trim();
                    if(TextUtils.isEmpty(mNumber)){
                        return;
                    }
                    int numberInt = Integer.parseInt(mNumber);
                    mName = addContactNameET.getText().toString().trim();
                    if(TextUtils.isEmpty(mName)){
                        return;
                    }
                    /**这里要有获取到Image图片路径的操作*/

                    if(TextUtils.isEmpty(mPhotoPath)){
                        return;
                    }


                    mAddContactPresenter.createContact(mName,numberInt, mPhotoPath);

                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE) {
            Log.i("qqliLog", "GalleryUri:    " + data.getData());
            Uri uriImg = data.getData();
            if (null != uriImg){
//                BitmapUtils
                mPhotoPath = BitmapUtils.getRealPathFromURI(uriImg,getContext());
                Bitmap bitmap = BitmapUtils.getSmallBitmap(BitmapUtils.getRealPathFromURI(uriImg,getContext()),480,800);
                if (null != bitmap){
                    contactIM.setImageBitmap(bitmap);
                }
            }

        }
    }
}
