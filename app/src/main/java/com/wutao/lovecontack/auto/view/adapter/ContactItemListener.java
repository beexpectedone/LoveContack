package com.wutao.lovecontack.auto.view.adapter;

import com.wutao.lovecontack.model.ContactBean;

import me.qianyue.dao.ContactDao;

public interface ContactItemListener{
    void onContactItemClick(ContactBean contactBean);
    void oncontactItemLongClick(ContactDao contactDao, ContactBean contactBean);
}
