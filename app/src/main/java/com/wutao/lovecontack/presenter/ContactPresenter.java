package com.wutao.lovecontack.presenter;

import android.app.Activity;
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
    private Activity mAct;

    public ContactPresenter(@Nullable ContactContract.View contactView, ContactDataSource mContactRepository,
                            @Nullable ContactDao mContactDao,Activity activity){

        this.mContactView = contactView;
        this.mContactRepository = mContactRepository;
        this.mContactDao = mContactDao;
        this.mAct = activity;
        mContactView.setPresenter(this);
    }



    @Override
    public void start() {
        loadContacts(mContactDao,false,mAct);
    }

    public void loadContacts(@NonNull ContactDao contactDao,boolean forceUpdate,@NonNull Activity context){
        loadTasks(contactDao,forceUpdate || mFirstLoad, true,context);
        mFirstLoad = false;
    }

    /**
     *具体获取contacts对象的方法
     * @param forceUpdate
     * @param showLoadingUI
     */
    private void loadTasks(@NonNull ContactDao contactDao, boolean forceUpdate, final boolean showLoadingUI,@NonNull Activity context) {

        if(showLoadingUI){
            mContactView.setLoadingIndicator(true);
        }

        if (forceUpdate) {
            mContactRepository.refreshContacts();
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
        },context);
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

    @Override
    public void deleteContact(@NonNull ContactDao contactDao, final ContactBean contactBean,@NonNull Activity context) {
        mContactRepository.deleteContact(contactDao, contactBean, new ContactDataSource.DeleteState() {
            @Override
            public void deleteSuccess() {
                /** 调用刷新页面方法 */
                mContactView.ifDeleteSuccess(contactBean);
            }

            @Override
            public void deleteFailure() {
                mContactView.deleteFailure();
            }
        },context);
    }


}
