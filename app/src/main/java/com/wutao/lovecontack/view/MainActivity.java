package com.wutao.lovecontack.view;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.wutao.lovecontack.R;
import com.wutao.lovecontack.Utils.ActivityUtils;
import com.wutao.lovecontack.Utils.SharedPreferencesUtils;
import com.wutao.lovecontack.config.ConfigValue;
import com.wutao.lovecontack.model.source.Injection;
import com.wutao.lovecontack.presenter.ContactPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    public static final String EXTRA_ACTIVITY_ID = "ACTIVITY_ID";

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.rootViewCL)
    CoordinatorLayout rootViewCL;
    @BindView(R.id.contentFrame)
    FrameLayout contentFrame;


    @Override
    protected void initView() {
        ButterKnife.bind(this);

        toolbar.setTitle("联系人列表");
        setSupportActionBar(toolbar);
        fab.setOnClickListener(this);

        String taskId = getIntent().getStringExtra(EXTRA_ACTIVITY_ID);
        ContackListFragment contackListFragment = (ContackListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if(null == contackListFragment){
            contackListFragment = ContackListFragment.newInstance(EXTRA_ACTIVITY_ID);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),contackListFragment,R.id.contentFrame);
        }
        new ContactPresenter(contackListFragment, Injection.provideContactsRepository(this),contackListFragment.contactDao,this);
//        new ContactPresenter(contackListFragment, new ContactsRepository(),contackListFragment.contactDao,this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /***
     * 更换view的显示，大图、中图、小图
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        ContackListFragment contackListFragment = (ContackListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(null != contackListFragment) {
            switch (id){
                case R.id.action_big:
                    contackListFragment.changeView(ConfigValue.BIG_IMAGE);

                break;
                case R.id.action_normal:
                    contackListFragment.changeView(ConfigValue.NORMAL_IMAGE);
                    SharedPreferencesUtils.setValue(this,ConfigValue.SHOW_MODEL,ConfigValue.NORMAL_IMAGE);
                    break;
                case R.id.action_small:
                    contackListFragment.changeView(ConfigValue.SMALL_IMAGE);
                    SharedPreferencesUtils.setValue(this,ConfigValue.SHOW_MODEL,ConfigValue.SMALL_IMAGE);
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                Intent intent = new Intent();
                intent.setClass(this,AddEditNewContactActivity.class);
                startActivity(intent);
                break;
        }
    }
}
