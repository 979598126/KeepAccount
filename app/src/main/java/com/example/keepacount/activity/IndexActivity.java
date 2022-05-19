package com.example.keepacount.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.example.keepacount.R;
import com.example.keepacount.adapter.ViewPagerAdapter;
import com.example.keepacount.entity.User;
import com.example.keepacount.fragment.Fragment1;
import com.example.keepacount.fragment.Fragment2;
import com.example.keepacount.fragment.Fragment3;
import com.example.keepacount.fragment.Fragment4;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class IndexActivity extends AppCompatActivity {
    ViewPager2 vp;
    BottomNavigationView navView;
    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;
    Fragment4 fragment4;
    ArrayList<Fragment> fragmentArrayList=new ArrayList<>();
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

//        获取登录的用户信息
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            user=(User) bundle.get("user");
        }

        //初始化视图
        initView();
        //设置ViewPager2
        setViewPager2();
        //设置页面之间切换的监听
        setListener();

    }

//    初始化视图
    public void initView(){
         fragment1=new Fragment1(user);
         fragment2=new Fragment2(user.getUserId());
         fragment3=new Fragment3();
         fragment4=new Fragment4(user);

        // 获取页面上的底部导航栏控件
        navView = findViewById(R.id.bottomNavView);
        vp=findViewById(R.id.viewPager2);
    }

//    设置ViewPager2
    public void setViewPager2(){
        fragmentArrayList.add(fragment1);
        fragmentArrayList.add(fragment2);
        fragmentArrayList.add(fragment3);
        fragmentArrayList.add(fragment4);

//        设置边界
        vp.setOffscreenPageLimit(fragmentArrayList.size()-1);
//        将Fragment数组添加到适配器中
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this,fragmentArrayList);
        vp.setAdapter(viewPagerAdapter);
    }

//    设置页面之间切换的监听
    public void setListener(){

        //        设置页面滑动事件
        vp.registerOnPageChangeCallback(new  ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                navView.getMenu().getItem(position).setChecked(true);
            }
        });

//      设置点击底部图标切换页面
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                vp.setCurrentItem(item.getOrder(),false);
                return true;
            }
        });
    }
}
