package com.wutao.lovecontack.view;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.wutao.lovecontack.R;
import com.wutao.lovecontack.Utils.ActivityUtils;
import com.wutao.lovecontack.application.LoveApplication;
import com.wutao.lovecontack.auto.view.DividerGridItemDecoration;
import com.wutao.lovecontack.auto.view.adapter.ContactItemListener;
import com.wutao.lovecontack.auto.view.adapter.ContactsAdapter;
import com.wutao.lovecontack.model.ContactBean;
import com.wutao.lovecontack.model.source.ContactsRepository;
import com.wutao.lovecontack.presenter.ContactPresenter;

import java.util.List;

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
    public boolean onOptionsItemSelected(MenuItem item) {/**fragment部分的业务逻辑不应该写到activity当中*/
        int id = item.getItemId();
        ContackListFragment contackListFragment = (ContackListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(null != contackListFragment) {
            List<ContactBean> mData = contackListFragment.getmData();
            RecyclerView mRecycleView = contackListFragment.getContactsRV();
            ContactItemListener mItemListener = contackListFragment.getmItemListener();
            ContactsAdapter mListAdapter = new ContactsAdapter(this,mItemListener,mData,1000,1000);
            switch (id){
                case R.id.action_big:
                    mRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
                    mRecycleView.addItemDecoration(new DividerGridItemDecoration(this));
                    mRecycleView.setAdapter(mListAdapter);
                break;
                case R.id.action_normal:
                    mListAdapter = new ContactsAdapter(this,mItemListener,mData,500,500);
                    mRecycleView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    mRecycleView.setItemAnimator(new DefaultItemAnimator());
                    mRecycleView.setAdapter(mListAdapter);
                    break;
                case R.id.action_small:
                    mListAdapter = new ContactsAdapter(this,mItemListener,mData,200,200);
                    mRecycleView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                    mRecycleView.setItemAnimator(new DefaultItemAnimator());
                    mRecycleView.setAdapter(mListAdapter);
                    break;
            }
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
