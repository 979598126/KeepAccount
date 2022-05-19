package com.example.keepacount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keepacount.activity.IndexActivity;
import com.example.keepacount.activity.RegisterActivity;
import com.example.keepacount.entity.User;
import com.example.keepacount.utils.DBHelper1;
import com.example.keepacount.utils.DBHelper2;

public class MainActivity extends AppCompatActivity {
    TextView toRegister;
    EditText userNameText,passwordText;
    Button loginBtn;
    ImageView showPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
//        初始化视图
        initView();
//      设置点击监听
        setClickListener();

    }
//        初始化视图
    public void initView(){
        userNameText=findViewById(R.id.editTextTextPersonName);
        passwordText=findViewById(R.id.editTextTextPassword);
        toRegister=findViewById(R.id.textView);
        loginBtn=findViewById(R.id.button);
        showPassword=findViewById(R.id.imageView3);
    }
//      设置点击监听
    public void setClickListener(){
//        登录
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName=userNameText.getText().toString();
                String password=passwordText.getText().toString();
                System.out.println(userName);
                System.out.println(password);
                DBHelper1 dbHelper=new DBHelper1(MainActivity.this);

                User user=dbHelper.queryUser(userName,password);

                if(user!=null){
                    Toast.makeText(getApplicationContext(), user.getUserName() + "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this, IndexActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("user",user);
                    System.out.println(user.getUserId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"登录失败，密码错误或账号不存在！",Toast.LENGTH_SHORT).show();
                }
            }
        });
//        跳转注册页面
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}