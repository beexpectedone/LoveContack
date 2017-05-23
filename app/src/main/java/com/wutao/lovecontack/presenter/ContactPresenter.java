package com.wutao.lovecontack.presenter;

import android.support.annotation.Nullable;

import com.wutao.lovecontack.contract.ContactContract;
import com.wutao.lovecontack.model.ContactBean;
import com.wutao.lovecontack.model.source.ContactDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mingyue on 2017/5/21.
 */

public class ContactPresenter implements ContactContract.Presenter {

    private final ContactContract.View mContactView;
    private final ContactDataSource mContactRepository;

    private boolean mFirstLoad = true;



    public ContactPresenter(@Nullable String taskId, @Nullable ContactContract.View taskView, ContactDataSource mContactRepository, ContactContract.View mContactView, ContactDataSource mContactRepository1){

        this.mContactView = mContactView;
        this.mContactRepository = mContactRepository1;

        mContactView.setPresenter(this);
    }



    @Override
    public void start() {
        loadContacts(false);
    }

    public void loadContacts(boolean forceUpdate){
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    /**
     *具体获取contacts对象的方法
     * @param forceUpdate
     * @param showLoadingUI
     */
    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if(showLoadingUI){
            mContactView.setLoadingIndicator(true);
        }

        /**调用M层的方法获取本地数据库当中的方法*/
        mContactRepository.getContactsList(new ContactDataSource.LoadContactsCallback() {
            @Override
            public void onContactsLoaded(List<ContactBean> contacts) {

                List<ContactBean> contactsToShow = new ArrayList<>();
                if(null != contacts){
                    contactsToShow.addAll(contacts);
                }

                if(!mContactView.isActive()){
                    return;
                }
                if(showLoadingUI){
                    mContactView.setLoadingIndicator(false);
                }

                processContacts(contactsToShow);

            }

            @Override
            public void onDataNotAvailable() {
                if(!mContactView.isActive()){
                    return;
                }
                mContactView.showLoadingTasksError();
            }
        });
    }

    private void processContacts(List<ContactBean> contactsToShow) {
        if(contactsToShow.isEmpty()){
            processEmptyTasks();
        }else{
            mContactView.showContacts(contactsToShow);
        }
    }

    /**当无法查询到contacts对象的时候，显示的画面*/
    private void processEmptyTasks() {
        mContactView.showNoContacts();
    }

    @Override
    public void addNewContact() {

    }
}
