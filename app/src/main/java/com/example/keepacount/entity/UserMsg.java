package com.example.keepacount.entity;

//    用户信息类，方便将用户数据展现到RecyclerView
public class UserMsg {
    public UserMsg(String head, String body) {
        this.head = head;
        this.body = body;
    }
    public String head;
    public String body;
}
