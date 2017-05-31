package com.wutao.lovecontack.Utils;

import android.app.Activity;
import android.text.TextUtils;

import com.wutao.lovecontack.model.ContactBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.Query;
import me.qianyue.dao.Contact;
import me.qianyue.dao.ContactDao;

/**
 * Created by qianyue.wang on 2017/5/23.
 */

public class DataBaseUtils {

    /**
     * 查询数据库中是否已经存在该姓名的联系人
     * @param contactDao
     * @param contactBean
     * @return
     */
    public static boolean search(ContactDao contactDao, ContactBean contactBean){
        Query query = contactDao.queryBuilder()
                .where(ContactDao.Properties.Name.eq(contactBean.getName()))
                .build();
        List<Contact> contactBeenList = query.list();
        if(null != contactBeenList && contactBeenList.size() > 0){
            return true;
        }
        return false;
    }

    public static List<ContactBean> search(ContactDao contactDao){
        List<ContactBean> contactBeanList = new ArrayList<>();
        List<Contact> contactList = contactDao.queryBuilder()
                .list();
        if(null != contactList && contactList.size() > 0){
            for (int i = 0; i< contactList.size(); i++){
                ContactBean contactBean = new ContactBean();
                if(TextUtils.isEmpty(contactList.get(i).getPhotoPath())){ //照片路径为空说明照片被删除了
                    continue;
                }
                contactBean.setPhotoPath(contactList.get(i).getPhotoPath());
                contactBean.setName(contactList.get(i).getName());
                contactBean.setNumber(contactList.get(i).getNumber());
                contactBean.setNumber2(contactList.get(i).getNumbertwo());
                contactBeanList.add(contactBean);
            }
        }
        return contactBeanList;
    }



    public static void insert(ContactDao contactDao, String photoPath, String name, String number1, double number2, Activity context){

        /**这里做具体的保存联系人的操作*/
//        mContactLocalSource.saveContact(contactBean);
        Date date = new Date();
        Contact contact = new Contact();
        contact.setName(name);
        contact.setDate(date);
        contact.setPhotoPath(photoPath);
        contact.setNumber(number1);
        contact.setNumbertwo(number2);
        contactDao.insert(contact);
    }


    public static void delete(ContactDao contactDao, ContactBean contactBean){
        List<Contact> contactList = contactDao.queryBuilder()
                .where(ContactDao.Properties.Name.eq(contactBean.getName()))
                .list();
        if(null != contactList && contactList.size() > 0){
            contactDao.delete(contactList.get(0));
        }
    }

}
