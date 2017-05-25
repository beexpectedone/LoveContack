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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wutao.lovecontack.R;
import com.wutao.lovecontack.auto.view.DividerGridItemDecoration;
import com.wutao.lovecontack.contract.ContactContract;
import com.wutao.lovecontack.model.ContactBean;

import java.io.File;
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
//            Intent intent=new Intent("android.intent.action.CALL", Uri.parse("tel:"+num));
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL);
            } else {
                startActivity(new Intent(
                        Intent.ACTION_CALL, Uri.parse("tel:"+ mNum)));
            }
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
        mListAdapter = new ContactsAdapter(mItemListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contact_list,null);
        ButterKnife.bind(this,root);
        mListAdapter = new ContactsAdapter(mItemListener);
        contactsRV.setLayoutManager(new GridLayoutManager(mAct,2));
        contactsRV.addItemDecoration(new DividerGridItemDecoration(mAct));
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
                mData.clear();
                mData.addAll(contactBeanList);
                mListAdapter.notifyDataSetChanged();
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

    private  class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyHolder>{

        private ContactItemListener mListener;

        public ContactsAdapter(ContactItemListener listener){
            this.mListener = listener;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_contact,parent,false));
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            final ContactBean contactBean = mData.get(position);
            if(null != contactBean){
                String name = mData.get(position).getName();
                String photoPath = mData.get(position).getPhotoPath();
                if (!TextUtils.isEmpty(name)){
                    holder.itemTV.setText(name);
                }
                if(!TextUtils.isEmpty(photoPath)){
                    Picasso.with(mAct)
                            .load(new File(photoPath))
                            .resize(400, 400)
                            .centerCrop()
                            .into(holder.itemIV);
                }
                holder.item_LL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onContactItemClick(contactBean);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {

            LinearLayout item_LL;
            ImageView itemIV;
            TextView itemTV;

            public MyHolder(View itemView) {
                super(itemView);
                itemIV = (ImageView) itemView.findViewById(R.id.itemIV);
                itemTV = (TextView) itemView.findViewById(R.id.itemTV);
                item_LL = (LinearLayout) itemView.findViewById(R.id.itemLL);
            }
        }
    }

    public interface ContactItemListener{
        void onContactItemClick(ContactBean contactBean);
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
}
