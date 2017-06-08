package com.example;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {
    public static void main(String[] args) throws Exception{
        //首先，创建一个用于添加实体（Entity）的模式（Schema）对象。
        //两个参数分别代表：数据库版本号与自动生成代码的包路径

        Schema schema = new Schema(3, "me.qianyue.dao");

        //shiyong Schema对象，添加实体（Entities）
        addContact(schema);

        //使用DaoGenerator类的generateAll()方法自动生成代码，此处你需要根据自己的情况更改输出目录（既之前创建的 java-gen)。
        new DaoGenerator().generateAll(schema, "D:/Projectdemo/LoveContack/LoveContack/app/src/main/java-gen");

    }

    private static void addContact(Schema schema) {

        //一个实体（类）就关联到数据库中的一张表，此处表名为"Contact"
        Entity contact = schema.addEntity("Contact");

        contact.addIdProperty().primaryKey().autoincrement();//

        contact.addStringProperty("name").notNull(); //联系人姓名
        contact.addStringProperty("number").notNull(); //联系人电话
        contact.addDoubleProperty("numbertwo");// 联系人第二个电话
        contact.addStringProperty("photoPath").notNull();
        contact.addDateProperty("date");
        contact.addBooleanProperty("good");
    }
}
