package com.wutao.lovecontack.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wutao.lovecontack.contract.AddEditContactContract;
import com.wutao.lovecontack.model.ContactBean;
import com.wutao.lovecontack.model.source.ContactDataSource;
import com.wutao.lovecontack.model.source.ContactsRepository;

import me.qianyue.dao.ContactDao;

/**
 * Created by mingyue on 2017/5/21.
 */

public class AddEditContactPresenter implements AddEditContactContract.Presenter, ContactsRepository.GetContactCallback {

    @NonNull
    private final ContactDataSource mContactsRepository;

    @NonNull
    private final AddEditContactContract.View mAddContactView;

    @Nullable
    private String mTaskId;

    public AddEditContactPresenter(@Nullable String taskId, @NonNull ContactDataSource mContactsRepository, @NonNull AddEditContactContract.View mAddContactView) {
        mTaskId = taskId;
        this.mContactsRepository = mContactsRepository;
        this.mAddContactView = mAddContactView;

        mAddContactView.setPresenter(this);
    }


    @Override
    public void createContact(ContactDao contactDao,ContactBean contactBean) {
        ContactBean newContactBean = new ContactBean(contactBean.getName(),contactBean.getNumber(),contactBean.getPhotoPath(),contactBean.getNumber2());
        if(newContactBean.isEmpty()){
            mAddContactView.showEmptyContactError();
        }else {
            mContactsRepository.saveContact(contactDao,newContactBean);
            mAddContactView.showContactsList();
        }
    }

    @Override
    public void saveContact(ContactDao contactDao, ContactBean contactBean) {
        if(isNewContact()){ //如果是新建的联系人就执行create方法
            createContact(contactDao,contactBean);
        }else {

        }
    }


    @Override
    public void start() {
        if(null != mTaskId){
            populateContact();
        }
    }

    private void populateContact() {
        if (null == mTaskId){
            throw new RuntimeException("populateTask() was called but task is new.");
        }
        mContactsRepository.getContact(mTaskId,this);
    }

    private boolean isNewContact() {
        return mTaskId == null;
    }

    @Override
    public void onContactLoaded(ContactBean contactBean) {

    }

    @Override
    public void onDataNotAvailable() {

    }
}
