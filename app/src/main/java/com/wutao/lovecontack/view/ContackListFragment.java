package com.wutao.lovecontack.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.wutao.lovecontack.R;
import com.wutao.lovecontack.Utils.DialogUtils;
import com.wutao.lovecontack.Utils.ToastUtils;
import com.wutao.lovecontack.auto.view.adapter.ContactItemListener;
import com.wutao.lovecontack.auto.view.adapter.ContactsAdapter;
import com.wutao.lovecontack.auto.view.adapter.DiffCallBack;
import com.wutao.lovecontack.contract.ContactContract;
import com.wutao.lovecontack.model.ContactBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.qianyue.dao.ContactDao;

/**
 * Created by mingyue on 2017/5/21.
 */

public class ContackListFragment extends BaseFragment implements ContactContract.View {

    @BindView(R.id.contactsRV)
    RecyclerView contactsRV;
    @BindView(R.id.noContactsTV)
    TextView noContactsTV;

    private ProgressDialog mDialog;
    private final int MY_PERMISSIONS_REQUEST_CALL = 0xb1;
    private final int REQUESTCODE_CALL = 0xb2;
    private final int REQUESTCODE_READ_EXTERNAL = 0xb3;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL = 0xb4;
    public static final String BIG_IMAGE = "big_image";
    public static final String NORMAL_IMAGE = "normal_image";
    public static final String SMALL_IMAGE = "small_image";

    public static final String ARGUMENT_TASK_ID = "ACTIVITY_ID";
    private List<ContactBean> mData = new ArrayList<>();
    private ContactContract.Presenter mPresenter;

    private ContactItemListener mItemListener = new ContactItemListener() {
        @Override
        public void onContactItemClick(ContactBean contactBean) {
            mNum = contactBean.getNumber();
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL);
            } else {
                startActivity(new Intent(
                        Intent.ACTION_CALL, Uri.parse("tel:"+ mNum)));
            }
        }

        @Override
        public void oncontactItemLongClick(final ContactDao contactDao, final ContactBean contactBean) {
            DialogUtils.showDialog(mAct, "删除联系人", "确定要删除该联系人吗？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /**这里实现删除item的操作*/
                    ContackListFragment.this.deleteContact(contactDao,contactBean);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        }
    };
    private ContactsAdapter mListAdapter;
    private String mNum;

    public static ContackListFragment newInstance(String taskId){
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TASK_ID,taskId);
        ContackListFragment contackListFragment = new ContackListFragment();
        contackListFragment.setArguments(arguments);
        return contackListFragment;
    }


    @Override
    protected void initView() {
        mListAdapter = new ContactsAdapter(mAct,mItemListener,mData,contactDao,500,500);
//        contactsRV.setLayoutManager(new GridLayoutManager(mAct,2));
        contactsRV.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));//设置RecyclerView布局管理器为2列垂直排布
        contactsRV.setItemAnimator(new DefaultItemAnimator());
        contactsRV.setAdapter(mListAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contact_list;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL);
        }else {
            mPresenter.start();
        }
    }

    @Override/***/
    public void setLoadingIndicator(boolean active) {
        if (getView() == null) {
            return;
        }
//        mAct.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mDialog = new ProgressDialog(mAct);
//                mDialog.show();
//            }
//        });
    }

    @Override/***/
    public void showLoadingTasksError() {
        ToastUtils.showShortToastOnUIThread(mAct,"数据加载错误，请稍后再试！");
    }

    @Override/***/
    public boolean isActive() {
        return true;
    }

    @Override/** 在这里面调用adapter的notify数据方法*/
    public void showContacts(final List<ContactBean> contactBeanList) {
        contactsRV.post(new Runnable() {
            @Override
            public void run() {
                if(null != mDialog && mDialog.isShowing()){
                    mDialog.dismiss();
                }
                noContactsTV.setVisibility(View.GONE);
                /**使用DiffUtil实现对 recycleview 的刷新*/
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(mData, contactBeanList), true);
                diffResult.dispatchUpdatesTo(mListAdapter);
                mData = contactBeanList;
                mListAdapter.setData(mData);

//                mData.clear();
//                mData.addAll(contactBeanList);
//                mListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override/***/
    public void showNoContacts() {
        contactsRV.post(new Runnable() {
            @Override
            public void run() {
                if(null != mDialog && mDialog.isShowing()){
                    mDialog.dismiss();
                }
                noContactsTV.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void showAddNewContact() {
        Intent intent = new Intent(getContext(), AddEditNewContactActivity.class);
        startActivityForResult(intent,AddEditNewContactActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void deleteContact(ContactDao contactDao, ContactBean contactBean) {
        mPresenter.deleteContact(contactDao,contactBean,mAct);
    }

    @Override
    public void ifDeleteSuccess(final ContactBean contactBean){
        contactsRV.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShortToast(mAct,"删除成功！");
                if(mData.contains(contactBean)){
                    List<ContactBean> mNewData = new ArrayList<>();
                    mNewData.addAll(mData);
                    mNewData.remove(contactBean);
                    /**使用DiffUtil实现对 recycleview 的刷新*/
                    DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(mData, mNewData), true);
                    diffResult.dispatchUpdatesTo(mListAdapter);
                    mData = mNewData;
                    mListAdapter.setData(mData);
                }
            }
        });

    }

    @Override
    public void deleteFailure() {
        /**这里给出删除失败的提示*/
    }

    @Override/**设置presenter对象*/
    public void setPresenter(ContactContract.Presenter presenter) {
        this.mPresenter = presenter;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUESTCODE_CALL:
                if (null != grantResults && grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ mNum)));
                }
                break;
            case REQUESTCODE_READ_EXTERNAL:
                if (null != grantResults && grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPresenter.start();
                }
                break;
        }
    }

    /**
     * 切换不同视图显示
      * @param viewTag
     */
    public void changeView(String viewTag){
        switch (viewTag){
            case BIG_IMAGE:
                mListAdapter = new ContactsAdapter(mAct,mItemListener,mData,contactDao,1000,1000);
                contactsRV.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
                break;
            case NORMAL_IMAGE:
                mListAdapter = new ContactsAdapter(mAct,mItemListener,mData,contactDao,500,500);
                contactsRV.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                break;
            case SMALL_IMAGE:
                mListAdapter = new ContactsAdapter(mAct,mItemListener,mData,contactDao,200,200);
                contactsRV.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                break;
        }
        contactsRV.setItemAnimator(new DefaultItemAnimator());
        if(null != mListAdapter){
            contactsRV.setAdapter(mListAdapter);
        }
    }
}
