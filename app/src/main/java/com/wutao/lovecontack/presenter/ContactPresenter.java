package com.wutao.lovecontack.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wutao.lovecontack.contract.ContactContract;
import com.wutao.lovecontack.model.ContactBean;
import com.wutao.lovecontack.model.source.ContactDataSource;

import java.util.List;

import me.qianyue.dao.ContactDao;

/**
 * Created by mingyue on 2017/5/21.
 */

public class ContactPresenter implements ContactContract.Presenter {

    private final ContactContract.View mContactView;
    private final ContactDataSource mContactRepository;

    private boolean mFirstLoad = true;
    private ContactDao mContactDao;

    public ContactPresenter(@Nullable ContactContract.View contactView, ContactDataSource mContactRepository,
                            @Nullable ContactDao mContactDao){

        this.mContactView = contactView;
        this.mContactRepository = mContactRepository;
        this.mContactDao = mContactDao;
        mContactView.setPresenter(this);
    }



    @Override
    public void start() {
        loadContacts(mContactDao,false);
    }

    public void loadContacts(@NonNull ContactDao contactDao,boolean forceUpdate){
        loadTasks(contactDao,forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    /**
     *具体获取contacts对象的方法
     * @param forceUpdate
     * @param showLoadingUI
     */
    private void loadTasks(@NonNull ContactDao contactDao, boolean forceUpdate, final boolean showLoadingUI) {
        if(showLoadingUI){
            mContactView.setLoadingIndicator(true);
        }

        /**调用M层的方法获取本地数据库当中的方法*/
        mContactRepository.getContactsList(contactDao,new ContactDataSource.LoadContactsCallback() {
            @Override
            public void onContactsLoaded(List<ContactBean> contacts) {
//                List<ContactBean> contactsToShow = new ArrayList<>();
//                if(null != contacts){
//                    contactsToShow.addAll(contacts);
//                }

                if(!mContactView.isActive()){
                    return;
                }

                processContacts(contacts);
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
