package com.wutao.lovecontack.auto.view.adapter;

import com.wutao.lovecontack.model.ContactBean;

public interface ContactItemListener{
    void onContactItemClick(ContactBean contactBean);
    void oncontactItemLongClick(ContactBean contactBean);
}
