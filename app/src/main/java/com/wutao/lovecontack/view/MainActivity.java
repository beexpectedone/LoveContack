package com.wutao.lovecontack.view;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.wutao.lovecontack.R;
import com.wutao.lovecontack.Utils.ActivityUtils;
import com.wutao.lovecontack.application.LoveApplication;
import com.wutao.lovecontack.model.source.ContactsRepository;
import com.wutao.lovecontack.presenter.ContactPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.qianyue.dao.ContactDao;
import me.qianyue.dao.DaoSession;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    public static final String EXTRA_ACTIVITY_ID = "ACTIVITY_ID";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.rootViewCL)
    CoordinatorLayout rootViewCL;
    @BindView(R.id.contentFrame)
    FrameLayout contentFrame;
    private ContactDao contactDao;


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
        initDao();
        new ContactPresenter(contackListFragment,new ContactsRepository(),contactDao);
    }

    /**
     获取到数据库的到文件
     */
    private void initDao() {
        DaoSession daoSession = LoveApplication.mApplication.getDaoSession();
        contactDao = daoSession.getContactDao();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                Snackbar.make(rootViewCL, "这里实现添加联系人的功能", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent();
                intent.setClass(this,AddEditNewContactActivity.class);
                startActivity(intent);
                break;
        }
    }
}
