package com.wutao.lovecontack.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.wutao.lovecontack.R;
import com.wutao.lovecontack.Utils.ActivityUtils;
import com.wutao.lovecontack.model.source.ContactsRepository;
import com.wutao.lovecontack.presenter.AddEditContactPresenter;

/**
 * Created by mingyue on 2017/5/21.
 */

public class AddEditNewContactActivity extends BaseActivity {
//
//    @BindView(R.id.toolbar)
    Toolbar toolbar;


    public static final int REQUEST_ADD_TASK = 1;


    @Override
    protected void initView() {
//        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);

        String taskId = null;
        AddEditNewContactFragment addEditNewContactFragment = (AddEditNewContactFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);;
        if(null == addEditNewContactFragment){
            addEditNewContactFragment = AddEditNewContactFragment.newInstance();
            if (getIntent().hasExtra(AddEditNewContactFragment.ARGUMENT_EDIT_TASK_ID)) {
                taskId = getIntent().getStringExtra(
                        AddEditNewContactFragment.ARGUMENT_EDIT_TASK_ID);
                toolbar.setTitle("编辑已有联系人");
                Bundle bundle = new Bundle();
                bundle.putString(addEditNewContactFragment.ARGUMENT_EDIT_TASK_ID, taskId);
                addEditNewContactFragment.setArguments(bundle);
            }else {
                toolbar.setTitle("新建联系人");
            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditNewContactFragment, R.id.contentFrame);

        }

        new AddEditContactPresenter(taskId, new ContactsRepository(), addEditNewContactFragment);


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_edit_new_contact;
    }
}
