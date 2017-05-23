package com.wutao.lovecontack.Utils;

import com.wutao.lovecontack.model.ContactBean;

import java.util.List;

import de.greenrobot.dao.query.Query;
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
        List<ContactBean> contactBeenList = query.list();
        if(null != contactBeenList && contactBeenList.size() > 0){
            return true;
        }
        return false;
    }



}
