package com.example.keepacount.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.keepacount.entity.User;


//用户表的操作
public class DBHelper1 extends SQLiteOpenHelper {
    private static final String DB_NAME = "Account.db";
    private String tableName = "user";
    //创建数据库语句
    private String CREATE_TABLE = "create table if not exists "
            + tableName + "(userId integer primary key autoincrement,name text(20),userName text(20),password text(20))";
    //创建数据库语句
    private String CREATE_TABLE2 = "create table if not exists "
            + "account" + "(accountId integer primary key autoincrement, des text(20),type text(20),isPay integer,money real,date text(20),userId integer)";



    public DBHelper1(Context context) {
        super(context, DB_NAME, null, 1);
    }

    //创建数据库方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
//            创建数据库
            db.execSQL("drop table  if exists " + tableName);
            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_TABLE2);



        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

//  更新数据库版本
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table  if exists " + tableName);
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }


    //    查找用户（用于登录）
    @SuppressLint("Range")
    public User queryUser(String userName,String password){
        User user=null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor=db.query(tableName,new String[]{"userId","userName","password","name"},"userName=? and password=?"
                ,new String[]{userName, password},null,null,null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            user = new User();
            user.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));
            user.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
        }
        return user;
    }



    //    添加用户（用于注册）
    public long insertUser(User user){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        //声明键值对values
        ContentValues values=new ContentValues();
        values.put("userName",user.getUserName());
        values.put("name",user.getName());
        values.put("password",user.getPassword());
        //将键值对values插入到数据库中
        return sqLiteDatabase.insert(tableName,null,values);
    }

//      检查是否有相同用户名（用于注册）
    public boolean checkUserName(String userName){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor=db.query(tableName,new String[]{"userName"},"userName=?"
                ,new String[]{userName},null,null,null);
        if(cursor.getCount() > 0){
            return false;
        }
        return true;
    }

//    修改密码
    public void updatePassword(String newPassword,int userId){
        SQLiteDatabase db = getReadableDatabase();
        String sql="update "+tableName+" set password=? where userId=?";
        db.execSQL(sql,new Object[]{newPassword,userId});
    }



}
