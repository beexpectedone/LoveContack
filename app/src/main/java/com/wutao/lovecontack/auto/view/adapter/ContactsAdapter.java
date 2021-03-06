package com.wutao.lovecontack.auto.view.adapter;

import android.app.Activity;
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
import com.wutao.lovecontack.model.ContactBean;

import java.io.File;
import java.util.List;

import me.qianyue.dao.ContactDao;


/**
 * Created by qianyue.wang on 2017/5/27.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyHolder> {

    private Activity mAct;
    private ContactItemListener mListener;
    private List<ContactBean> mData;
    private ContactDao mContactDao;
    private int sizeWidth;
    private int sizeHeight;

    public ContactsAdapter(Activity mAct, ContactItemListener listener, List<ContactBean> mData, ContactDao contactDao,int sizeWidth, int sizeHeight){
        this.mAct = mAct;
        this.mListener = listener;
        setData(mData);
        this.mContactDao = contactDao;
        this.sizeWidth =sizeWidth;
        this.sizeHeight = sizeHeight;
    }

    public void setData(List<ContactBean> mList){
        mData = mList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(mAct).inflate(R.layout.item_contact,parent,false));
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
                        .resize(sizeWidth, sizeHeight)
                        .centerCrop()
                        .into(holder.itemIV);
            }
            holder.item_LL.setOnClickListener(new View.OnClickListener() {/**拨打电话功能*/
                @Override
                public void onClick(View v) {
                    mListener.onContactItemClick(contactBean);
                }
            });

            holder.item_LL.setOnLongClickListener(new View.OnLongClickListener() {/** 长按删除 */
                @Override
                public boolean onLongClick(View v) {
                    mListener.oncontactItemLongClick(mContactDao,contactBean);
                    return false;
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

