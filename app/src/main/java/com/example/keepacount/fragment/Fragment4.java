package com.example.keepacount.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keepacount.R;
import com.example.keepacount.adapter.UserAdapter;
import com.example.keepacount.entity.User;
import com.example.keepacount.entity.UserMsg;
import com.example.keepacount.utils.DBHelper1;

import java.util.ArrayList;
import java.util.List;

public class Fragment4 extends Fragment {
    ConstraintLayout cl1,cl2;
    RecyclerView recyclerView;
    ImageView openPanel;
    EditText oldPasswordText,newPasswordText,confirmPasswordText;
    Button updateBtn;

    private User user;//用户信息
    List<UserMsg> listData=new ArrayList();

    public Fragment4(User user) {
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.index_fragment4,container,false);

//        初始化视图
        initView(view);

//        设置listData
        setListData();

        UserAdapter pla=new UserAdapter(getContext(),R.layout.user_item,listData);
        recyclerView.setAdapter(pla);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);



//    设置点击监听
        setClickListener();


        return view;
    }

//    初始化视图
    public void initView(View view){
        openPanel=view.findViewById(R.id.imageView14);
        oldPasswordText=view.findViewById(R.id.editTextTextPassword4);
        newPasswordText=view.findViewById(R.id.editTextTextPassword5);
        confirmPasswordText=view.findViewById(R.id.editTextTextPassword6);
        cl1=view.findViewById(R.id.constraintLayout3);
        cl2=view.findViewById(R.id.constraintLayout4);
        updateBtn=view.findViewById(R.id.button4);
        recyclerView=view.findViewById(R.id.recyclerView2);
    }

//    设置listData
    public void setListData(){
        UserMsg u1=new UserMsg("用户ID",Integer.toString(user.getUserId()));
        UserMsg u2=new UserMsg("用户名",user.getUserName());
        UserMsg u3=new UserMsg("姓名",user.getName());
        UserMsg u4=new UserMsg("性别","男");
        UserMsg u5=new UserMsg("邮箱","123456@qq.com");
        listData.add(u1);
        listData.add(u2);
        listData.add(u3);
        listData.add(u4);
        listData.add(u5);
    }



//    设置点击监听
    public void setClickListener(){
        //        显示和隐藏修改密码框
        cl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                修改面板显示时
                if(cl2.getVisibility()==View.VISIBLE){
                    cl2.setVisibility(View.GONE);
                    openPanel.setImageResource(R.drawable.ic_up);
                }
//                修改面板隐藏时
                else{
                    cl2.setVisibility(View.VISIBLE);
                    openPanel.setImageResource(R.drawable.ic_bottom);
                }
            }
        });

        //      提交修改的密码
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword=oldPasswordText.getText().toString();
                String newPassword=newPasswordText.getText().toString();
                String confirmPassword=confirmPasswordText.getText().toString();
                System.out.println(oldPassword);
                System.out.println(newPassword);
                System.out.println(confirmPassword);

                DBHelper1 dbHelper=new DBHelper1(getContext());
                System.out.println(user.getPassword());

//                原密码错误
                if(!user.getPassword().equals(oldPassword) ){
                    Toast.makeText(getContext(),"原密码错误，请输入正确的密码！",Toast.LENGTH_SHORT).show();
                }
                else{
//                    密码不一致
                    if(newPassword!="" && !newPassword.equals(confirmPassword)){
                        Toast.makeText(getContext(),"密码前后不一致！",Toast.LENGTH_SHORT).show();
                    }
//                    修改密码
                    else{
                        dbHelper.updatePassword(newPassword,user.getUserId());
                        Toast.makeText(getContext(),"修改成功！",Toast.LENGTH_SHORT).show();
//                        清空修改框
                        oldPasswordText.setText("");
                        newPasswordText.setText("");
                        confirmPasswordText.setText("");
//                        修改密码框隐藏
                        cl2.setVisibility(View.GONE);
                        openPanel.setImageResource(R.drawable.ic_up);
                    }
                }
            }
        });
    }



}
