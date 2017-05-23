package com.wutao.lovecontack.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.wutao.lovecontack.base.BasePresenter;
import com.wutao.lovecontack.contract.ContactContract;
import com.wutao.lovecontack.model.ContactBean;

import java.util.List;

/**
 * Created by mingyue on 2017/5/21.
 */

public class ContackListFragment extends Fragment implements ContactContract.View {

    public static final String ARGUMENT_TASK_ID = "ACTIVITY_ID";

    public static ContackListFragment newInstance(String taskId){
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TASK_ID,taskId);
        ContackListFragment contackListFragment = new ContackListFragment();
        contackListFragment.setArguments(arguments);
        return contackListFragment;
    }


    @Override/***/
    public void setLoadingIndicator(boolean active) {

    }

    @Override/***/
    public void showLoadingTasksError() {

    }

    @Override/***/
    public boolean isActive() {
        return false;
    }

    @Override/***/
    public void showContacts(List<ContactBean> contactBeanList) {

    }

    @Override/***/
    public void showNoContacts() {

    }

    @Override/***/
    public void showAddNewContact() {
        Intent intent = new Intent(getContext(), AddEditNewContactActivity.class);
        startActivityForResult(intent,AddEditNewContactActivity.REQUEST_ADD_TASK);
    }

    @Override/***/
    public void setPresenter(BasePresenter presenter) {

    }
}
