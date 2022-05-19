package com.example.keepacount.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.keepacount.MainActivity;
import com.example.keepacount.R;
import com.example.keepacount.entity.User;
import com.example.keepacount.utils.DBHelper1;

public class RegisterActivity extends AppCompatActivity {

    ImageView returnToLogin;
    Button registerBtn;
    EditText userNameText,nameText,passwordText,realPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
//        初始化视图
        initView();

        setClickListener();
    }

    public void initView(){
        returnToLogin=findViewById(R.id.imageView);
        registerBtn=findViewById(R.id.button2);
        userNameText=findViewById(R.id.editTextTextPersonName2);
        nameText=findViewById(R.id.editTextTextPersonName3);
        passwordText=findViewById(R.id.editTextTextPassword2);
        realPasswordText=findViewById(R.id.editTextTextPassword3);
    }

    public void setClickListener(){
        returnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName=userNameText.getText().toString();
                String name=nameText.getText().toString();
                String password=passwordText.getText().toString();
                String realPassword=realPasswordText.getText().toString();
                DBHelper1 dbHelper=new DBHelper1(RegisterActivity.this);

//                判断密码前后是否一致
                if(!password.equals(realPassword)){
                    Toast.makeText(getApplicationContext(),"注册失败，密码前后不一致！",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(dbHelper.checkUserName(userName)){
                        User user=new User();
                        user.setUserName(userName);
                        user.setName(name);
                        user.setPassword(password);
                        if(dbHelper.insertUser(user)>0){
                            Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("user",user);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"注册失败，该账户已存在！",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}
