package com.wutao.lovecontack.contract;

import com.wutao.lovecontack.base.BasePresenter;
import com.wutao.lovecontack.base.BaseView;
import com.wutao.lovecontack.model.ContactBean;

/**
 * Created by mingyue on 2017/5/21.
 */

public interface AddEditContactContract {

    interface  View extends BaseView<Presenter>{

        void setName(ContactBean contactBean);

        void setNumber(ContactBean contactBean);

        void setmPhotoPath(ContactBean contactBean);

        boolean isActive();

        void showEmptyContactError();

        void showContactsList();

    }

    interface Presenter extends BasePresenter{
        void createContact(String name,int number,String photoPath);
    }
}
