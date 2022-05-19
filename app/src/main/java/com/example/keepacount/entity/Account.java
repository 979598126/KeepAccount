package com.example.keepacount.entity;

import java.io.Serializable;

public class Account implements Serializable {
    private int accountId;
    private String desc;
    private String type;
    private boolean isPay;
    private float money;
    private String date;
    private int userId;


    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", desc='" + desc + '\'' +
                ", type='" + type + '\'' +
                ", isPay=" + isPay +
                ", money=" + money +
                ", date='" + date + '\'' +
                ", userId=" + userId +
                '}';
    }
}
