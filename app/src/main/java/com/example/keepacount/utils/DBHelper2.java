package com.example.keepacount.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.keepacount.entity.Account;
import com.example.keepacount.entity.User;

import java.util.ArrayList;
import java.util.List;

//账单表的操作
public class DBHelper2 extends SQLiteOpenHelper {
    private static final String DB_NAME = "Account.db";
    private String tableName = "account";

    //创建数据库语句
    private String CREATE_TABLE = "create table if not exists "
            + tableName + "(accountId integer primary key autoincrement, des text(20)," +
            "type text(20),isPay integer,money real,date text(20),userId integer)";

    public DBHelper2(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    //  更新数据库版本
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table  if exists " + tableName);
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }


//  查询今日账单
    @SuppressLint("Range")
    public List<Account> selectTodayAccounts(String date, int userId){
        List<Account> accounts=new ArrayList<>();
        String id=(userId+"").trim();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor=db.query(tableName,new String[]{"accountId","des","type","isPay","money","date","userId"},
                "userId="+id+" and date=?", new String[]{date},null,null,null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for(int i=0;i<cursor.getCount();i++){
                Account account=new Account();
                account.setAccountId(cursor.getInt(cursor.getColumnIndex("accountId")));
                account.setDesc(cursor.getString(cursor.getColumnIndex("des")));
                account.setType(cursor.getString(cursor.getColumnIndex("type")));
                int isPay=cursor.getInt(cursor.getColumnIndex("isPay"));
                boolean x=(isPay==1)?true:false;
                account.setPay(x);
                account.setMoney(cursor.getFloat(cursor.getColumnIndex("money")));
                account.setDate(cursor.getString(cursor.getColumnIndex("date")));
                account.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));
                accounts.add(account);
                cursor.moveToNext();
            }
        }
        return accounts;
    }

//    添加账单
    public long insertAccount(Account account){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        //声明键值对values
        ContentValues values=new ContentValues();
        values.put("des",account.getDesc());
        values.put("type",account.getType());
        values.put("isPay",account.isPay());
        values.put("money",account.getMoney());
        values.put("date",account.getDate());
        values.put("userId",account.getUserId());
        //将键值对values插入到数据库中
        return sqLiteDatabase.insert(tableName,null,values);
    }

//    修改账单
    public int updateAccount(Account account){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        //声明键值对values
        ContentValues values=new ContentValues();
        values.put("des",account.getDesc());
        values.put("type",account.getType());
        values.put("isPay",account.isPay());
        values.put("money",account.getMoney());
        values.put("date",account.getDate());
        values.put("userId",account.getUserId());
        String accountId=String.valueOf(account.getAccountId()).trim();
        //将键值对values插入到数据库中
        return sqLiteDatabase.update(tableName,values,"accountId="+accountId,null);

    }

//    删除账单
    public int deleteAccount(int accountId){
        String id=String.valueOf(accountId).trim();
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        return sqLiteDatabase.delete(tableName,"accountId="+id,null);
    }

//    按年月查询账单
    @SuppressLint("Range")
    public List<Account> selectAccounts(String date, int userId){
        List<Account> accounts=new ArrayList<>();
        String id=(userId+"").trim();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor=db.query(tableName,new String[]{"accountId","des","type","isPay","money","date","userId"},
                "userId="+id+" and date LIKE ?", new String[]{date+"%"},null,null,null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for(int i=0;i<cursor.getCount();i++){
                Account account=new Account();
                account.setAccountId(cursor.getInt(cursor.getColumnIndex("accountId")));
                account.setDesc(cursor.getString(cursor.getColumnIndex("des")));
                account.setType(cursor.getString(cursor.getColumnIndex("type")));
                int isPay=cursor.getInt(cursor.getColumnIndex("isPay"));
                boolean x=(isPay==1)?true:false;
                account.setPay(x);
                account.setMoney(cursor.getFloat(cursor.getColumnIndex("money")));
                account.setDate(cursor.getString(cursor.getColumnIndex("date")));
                account.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));
                accounts.add(account);
                cursor.moveToNext();
            }
        }

        return accounts;
    }
}
