package com.wutao.lovecontack.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wutao.lovecontack.R;
import com.wutao.lovecontack.auto.view.DividerGridItemDecoration;
import com.wutao.lovecontack.auto.view.adapter.ContactItemListener;
import com.wutao.lovecontack.auto.view.adapter.ContactsAdapter;
import com.wutao.lovecontack.auto.view.adapter.DiffCallBack;
import com.wutao.lovecontack.contract.ContactContract;
import com.wutao.lovecontack.model.ContactBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mingyue on 2017/5/21.
 */

public class ContackListFragment extends Fragment implements ContactContract.View {

    @BindView(R.id.contactsRV)
    RecyclerView contactsRV;
    @BindView(R.id.noContactsTV)
    TextView noContactsTV;

    private Activity mAct;
    private ProgressDialog mDialog;
    private final int MY_PERMISSIONS_REQUEST_CALL = 0xb1;
    private final int REQUESTCODE_CALL = 0xb2;

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
        public void oncontactItemLongClick(ContactBean contactBean) {
            /**这里实现删除item的操作*/

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mAct = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contact_list,null);
        ButterKnife.bind(this,root);
        mListAdapter = new ContactsAdapter(mAct,mItemListener,mData,500,500);
        contactsRV.setLayoutManager(new GridLayoutManager(mAct,2));
        contactsRV.addItemDecoration(new DividerGridItemDecoration(mAct));
//        contactsRV.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));//设置RecyclerView布局管理器为2列垂直排布
//        contactsRV.setItemAnimator(new DefaultItemAnimator());
        contactsRV.setAdapter(mListAdapter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override/***/
    public void setLoadingIndicator(boolean active) {
        if (getView() == null) {
            return;
        }
        mAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDialog = new ProgressDialog(mAct);
                mDialog.show();
            }
        });
    }

    @Override/***/
    public void showLoadingTasksError() {

    }

    @Override/***/
    public boolean isActive() {
        return true;
    }

    @Override/** 在这里面调用adapter的notify数据方法*/
    public void showContacts(final List<ContactBean> contactBeanList) {
        mAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null != mDialog && mDialog.isShowing()){
                    mDialog.dismiss();
                }
                noContactsTV.setVisibility(View.GONE);
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
        mAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null != mDialog && mDialog.isShowing()){
                    mDialog.dismiss();
                }
                noContactsTV.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override/***/
    public void showAddNewContact() {
        Intent intent = new Intent(getContext(), AddEditNewContactActivity.class);
        startActivityForResult(intent,AddEditNewContactActivity.REQUEST_ADD_TASK);
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
        }
    }

    public RecyclerView getContactsRV() {
        return contactsRV;
    }

    public List<ContactBean> getmData() {
        return mData;
    }

    public ContactItemListener getmItemListener() {
        return mItemListener;
    }
}
