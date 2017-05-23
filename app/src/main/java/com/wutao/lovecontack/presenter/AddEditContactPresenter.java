package com.wutao.lovecontack.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wutao.lovecontack.contract.AddEditContactContract;
import com.wutao.lovecontack.model.ContactBean;
import com.wutao.lovecontack.model.source.ContactDataSource;
import com.wutao.lovecontack.model.source.ContactsRepository;

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
    public void createContact(String name, int number, String photoPath) {
        ContactBean newContactBean = new ContactBean(name,number,photoPath);
        if(newContactBean.isEmpty()){
            mAddContactView.showEmptyContactError();
        }else {
            mContactsRepository.saveContact(newContactBean);
            mAddContactView.showContactsList();
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

    @Override
    public void onContactLoaded(ContactBean contactBean) {

    }

    @Override
    public void onDataNotAvailable() {

    }
}
