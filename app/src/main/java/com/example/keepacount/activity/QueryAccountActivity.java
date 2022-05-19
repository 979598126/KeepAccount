package com.example.keepacount.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.keepacount.R;
import com.example.keepacount.adapter.AccountAdapter;
import com.example.keepacount.entity.Account;
import com.example.keepacount.entity.User;
import com.example.keepacount.utils.DBHelper2;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class QueryAccountActivity extends AppCompatActivity {
    ConstraintLayout cl;
    LinearLayout ll,ll1,ll2;
    Button timeBtn,clearBtn,updateBtn;
    ImageView[] imageViews=new ImageView[5];
    ImageView incomeImg,payImg,turnToIndexImg,noAccountImg;
    int[] imgID=new int[]{R.id.imageView6,R.id.imageView7,R.id.imageView8,R.id.imageView9,R.id.imageView10};
    TextView timeText,foodText,waterText,playText,shopText,otherText,yearTest,monthText,incomeText,payText;
    EditText moneyText,descText;
    SmartRefreshLayout sRL;
    SwipeRecyclerView recyclerView;


    private User user;
    Account account=new Account();
    DBHelper2 dbHelper=new DBHelper2(this);
    List<Account> listData=new ArrayList<>();
    AccountAdapter pla;


//    获取当前的年月
    Calendar calendar=Calendar.getInstance();
    Date date=calendar.getTime();
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
    String time=sdf.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_account);

        //        获取登录的用户信息
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            user=(User) bundle.get("user");
        }


        //    初始化视图
        initView();

        String[] x= time.split("-");
        String year=x[0];
        String month=x[1];
        yearTest.setText(year+"年");
        monthText.setText(month+"月");
        System.out.println(year+"年"+month+"月");
        List<Account> accountList= dbHelper.selectAccounts(time,user.getUserId());
        listData=accountList;
        compute();
        isAccount();

        //        设置recyclerView的加载和更新
        setSmartRefresh();

//        侧滑菜单的设置
        setSwipeRecyclerView();

//        RecyclerView的设置
        setRecyclerView();

        //    设置点击监听
        setClickListener();


    }
//    初始化视图
    public void initView(){
        cl=findViewById(R.id.addPanel);
        ll=findViewById(R.id.linearLayout7);
        ll1=findViewById(R.id.linearLayout8);
        ll2=findViewById(R.id.linearLayout9);
        sRL=findViewById(R.id.refreshLayout);
        recyclerView=findViewById(R.id.recyclerView3);

        for(int i=0;i<imageViews.length;i++){
            imageViews[i]=findViewById(imgID[i]);
            imageViews[i].setOnClickListener(this::onImgClick);
        }
        noAccountImg=findViewById(R.id.noAccount);
        turnToIndexImg=findViewById(R.id.imageView13);
        incomeImg=findViewById(R.id.imageView11);
        payImg=findViewById(R.id.imageView12);

        timeText=findViewById(R.id.textView8);
        foodText=findViewById(R.id.textView12);
        waterText=findViewById(R.id.textView16);
        playText=findViewById(R.id.textView17);
        shopText=findViewById(R.id.textView18);
        otherText=findViewById(R.id.textView19);
        moneyText=findViewById(R.id.editTextNumber);
        descText=findViewById(R.id.editTextTextPersonName5);
        yearTest=findViewById(R.id.textView33);
        monthText=findViewById(R.id.textView34);
        incomeText=findViewById(R.id.textView36);
        payText=findViewById(R.id.textView38);


        timeBtn=findViewById(R.id.button3);
        clearBtn=findViewById(R.id.button6);
        updateBtn=findViewById(R.id.button7);
    }


//    设置点击监听
    public void setClickListener(){
//        返回主页
        turnToIndexImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent=new Intent(QueryAccountActivity.this, IndexActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("user",user);
                    intent.putExtras(bundle);
                    startActivity(intent);

            }
        });
        //        选择年月
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeSelect2();
            }
        });
        //        显示收入的账单
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Account> incomes=new ArrayList<>();
                for(int i=0;i<listData.size();i++){
                    if(!listData.get(i).isPay()){
                        incomes.add(listData.get(i));
                    }
                }
                    isAccount();
                    pla.setListData(incomes);
                    pla.notifyDataSetChanged();

            }
        });
        //      显示支出的账单
        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Account> pays=new ArrayList<>();
                for(int i=0;i<listData.size();i++){
                    if(listData.get(i).isPay()){
                        pays.add(listData.get(i));
                    }
                }
                isAccount();
                pla.setListData(pays);
                pla.notifyDataSetChanged();
            }
        });

        //        修改栏选择时间
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeSelect1();
            }
        });
        //        选择收入和支出
        incomeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomeImg.setImageResource(R.drawable.ic_income_selected);
                payImg.setImageResource(R.drawable.ic_pay);
                account.setPay(false);
            }
        });
        payImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomeImg.setImageResource(R.drawable.ic_income);
                payImg.setImageResource(R.drawable.ic_pay_selected);
                account.setPay(true);
            }
        });
        //        关闭修改栏
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAddAccount();
                cl.setVisibility(View.GONE);
            }
        });
        //        修改账单
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                将账单信息存入account对象中
                float money=Float.parseFloat(moneyText.getText().toString());
                if(account.isPay()){
                    money=-money;
                }
                account.setMoney(money);
                account.setDesc(descText.getText().toString());
                account.setUserId(user.getUserId());
                System.out.println(account);

                if(dbHelper.updateAccount(account)>0){
                    Toast.makeText(QueryAccountActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                    clearAddAccount();
                    cl.setVisibility(View.GONE);
                }
                else{
                    Toast.makeText(QueryAccountActivity.this,"修改失败！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    //        选择账单类型
    public void onImgClick(View view){
        switch (view.getId()){
            case R.id.imageView6:
                imageViews[0].setImageResource(R.drawable.ic_food_selected);
                imageViews[1].setImageResource(R.drawable.ic_water);
                imageViews[2].setImageResource(R.drawable.ic_play);
                imageViews[3].setImageResource(R.drawable.ic_shop);
                imageViews[4].setImageResource(R.drawable.ic_other);
                account.setType(foodText.getText().toString());
                break;

            case R.id.imageView7:
                imageViews[0].setImageResource(R.drawable.ic_food);
                imageViews[1].setImageResource(R.drawable.ic_water_selected);
                imageViews[2].setImageResource(R.drawable.ic_play);
                imageViews[3].setImageResource(R.drawable.ic_shop);
                imageViews[4].setImageResource(R.drawable.ic_other);
                account.setType(waterText.getText().toString());
                break;
            case R.id.imageView8:
                imageViews[0].setImageResource(R.drawable.ic_food);
                imageViews[1].setImageResource(R.drawable.ic_water);
                imageViews[2].setImageResource(R.drawable.ic_play_selected);
                imageViews[3].setImageResource(R.drawable.ic_shop);
                imageViews[4].setImageResource(R.drawable.ic_other);
                account.setType(playText.getText().toString());
                break;
            case R.id.imageView9:
                imageViews[0].setImageResource(R.drawable.ic_food);
                imageViews[1].setImageResource(R.drawable.ic_water);
                imageViews[2].setImageResource(R.drawable.ic_play);
                imageViews[3].setImageResource(R.drawable.ic_shop_selected);
                imageViews[4].setImageResource(R.drawable.ic_other);
                account.setType(shopText.getText().toString());
                break;
            case R.id.imageView10:
                imageViews[0].setImageResource(R.drawable.ic_food);
                imageViews[1].setImageResource(R.drawable.ic_water);
                imageViews[2].setImageResource(R.drawable.ic_play);
                imageViews[3].setImageResource(R.drawable.ic_shop);
                imageViews[4].setImageResource(R.drawable.ic_other_selected);
                account.setType(otherText.getText().toString());
                break;
        }
    }

    //  设置时间选择器
    public void setTimeSelect1(){
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2015,1,1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2030,1,1);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date,View v) {//选中事件回调
                timeText.setText(sdf.format(date));
                account.setDate(sdf.format(date));
            }
        })
                .setType(new boolean[]{true, true, true,false,false,false})
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLUE)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .isDialog(true)//是否显示为对话框样式
                .build();
        pvTime.show();
    }
    public void setTimeSelect2(){
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2015,1,1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2030,1,1);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");

        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date,View v) {//选中事件回调

                String[] x= sdf.format(date).split("-");
                String year=x[0];
                String month=x[1];
                yearTest.setText(year+"年");
                monthText.setText(month+"月");
                time=sdf.format(date);
                System.out.println(year+"年"+month+"月");
                List<Account> accounts= dbHelper.selectAccounts(time,user.getUserId());
                    listData=accounts;
                    pla.setListData(listData);
                    pla.notifyDataSetChanged();
                    compute();


            }
        })
                .setType(new boolean[]{true, true,false,false,false,false})
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLUE)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .isDialog(true)//是否显示为对话框样式
                .build();
        pvTime.show();
    }

    //    清空添加栏
    public void clearAddAccount(){
        // 清除account数据
        account=new Account();
//                恢复UI
        timeText.setText("未选择");
        incomeImg.setImageResource(R.drawable.ic_income);
        payImg.setImageResource(R.drawable.ic_pay);
        imageViews[0].setImageResource(R.drawable.ic_food);
        imageViews[1].setImageResource(R.drawable.ic_water);
        imageViews[2].setImageResource(R.drawable.ic_play);
        imageViews[3].setImageResource(R.drawable.ic_shop);
        imageViews[4].setImageResource(R.drawable.ic_other);
        moneyText.setText("");
        descText.setText("");
        updateBtn.setText("修改");
    }

    //    设置recyclerView的加载和更新
    public void setSmartRefresh(){
        //头部刷新样式
        sRL.setRefreshHeader(new ClassicsHeader(this));
        //尾部刷新样式
        sRL.setRefreshFooter(new ClassicsFooter(this));
        sRL.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                sRL.finishRefresh(1000);
                System.out.println("刷新");
                listData=dbHelper.selectAccounts(time,user.getUserId());
                if(isAccount()){
                    pla.setListData(listData);
                    pla.notifyDataSetChanged();
                    compute();
                }


            }
        });

        sRL.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                System.out.println("加载");
                sRL.finishLoadMore(2000);

            }
        });

    }
    //      侧滑菜单的设置
    public void setSwipeRecyclerView(){

        // 创建侧滑菜单
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
                int width = getResources().getDimensionPixelOffset(R.dimen.dp_100);
                int height = getResources().getDimensionPixelOffset(R.dimen.dp_100);
//                修改按钮
                SwipeMenuItem deleteItem = new SwipeMenuItem(QueryAccountActivity.this)
                        .setBackgroundColor(Color.rgb(56, 154, 243))
                        .setText("修改")
                        .setTextSize(18)
                        .setTextColor(Color.WHITE)
                        .setImage(R.drawable.ic_update_small)
                        .setHeight(height)
                        .setWidth(width);
//                 删除按钮
                SwipeMenuItem deleteItem2 = new SwipeMenuItem(QueryAccountActivity.this)
                        .setBackground(R.color.red)
                        .setImage(R.drawable.ic_delete_small)
                        .setText("删除")
                        .setTextSize(18)
                        .setTextColor(Color.WHITE)
                        .setHeight(height)
                        .setWidth(width);


                // 各种文字和图标属性设置。
                rightMenu.addMenuItem(deleteItem); // 在Item右侧添加一个菜单。
                rightMenu.addMenuItem(deleteItem2);
            }
        };

        // 菜单点击监听
        OnItemMenuClickListener mItemMenuClickListener = new OnItemMenuClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge, int position) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();

                // 左侧还是右侧菜单：
                int direction = menuBridge.getDirection();
                // 菜单在Item中的Position：
                int menuPosition = menuBridge.getPosition();
                System.out.println(position);

//                修改账单
                if(direction==-1&&menuPosition==0){
//                    获取账单信息
                    account=listData.get(position);
                    System.out.println(account);
//                    打开修改栏
                    openUpdatePanel(account);

                }
//                删除账单
                else{
//                    定义删除弹窗
                    AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(QueryAccountActivity.this);
                    alertdialogBuilder.setMessage("您确认要删除该账单记录");
//                    删除账单记录
                    alertdialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(dbHelper.deleteAccount(listData.get(position).getAccountId())>1){
                                Toast.makeText(QueryAccountActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(QueryAccountActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    alertdialogBuilder.setNeutralButton("取消", null);
                    final AlertDialog alertdialog1 = alertdialogBuilder.create();
                    alertdialog1.show();
                }

            }
        };

        // 设置监听器。
        recyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        // 菜单点击监听。
        recyclerView.setOnItemMenuClickListener(mItemMenuClickListener);
        //item的点击监听
        recyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                显示账单备注
                AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(QueryAccountActivity.this);
                alertdialogBuilder.setMessage("备注："+listData.get(position).getDesc());
                alertdialogBuilder.setNegativeButton("确定",null);
                final AlertDialog alertdialog1 = alertdialogBuilder.create();
                alertdialog1.show();
            }
        });

    }

    //    RecyclerView的设置
    public void setRecyclerView(){
        pla=new AccountAdapter(this,R.layout.account_item,listData);
        recyclerView.setAdapter(pla);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    //     设置空数据图片
    public boolean isAccount(){
        if (listData.size()==0){
            noAccountImg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return false;
        }
        else{
            noAccountImg.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            return true;
        }
    }

//    打开修改栏
    public void openUpdatePanel(Account account){
        cl.setVisibility(View.VISIBLE);
        timeText.setText(account.getDate());
        if(account.isPay()){
            payImg.setImageResource(R.drawable.ic_pay_selected);
        }else {incomeImg.setImageResource(R.drawable.ic_income_selected);}
        switch (account.getType()){
            case "饮食": imageViews[0].setImageResource(R.drawable.ic_food_selected);break;
            case "水电": imageViews[1].setImageResource(R.drawable.ic_water_selected);break;
            case "娱乐": imageViews[2].setImageResource(R.drawable.ic_play_selected);break;
            case "网购": imageViews[3].setImageResource(R.drawable.ic_shop_selected);break;
            default:imageViews[4].setImageResource(R.drawable.ic_other_selected);break;
        }
        float money=account.getMoney();
        if(money<0){
            money=-money;
        }
        String x=String.valueOf(money);
        moneyText.setText(x);
        descText.setText(account.getDesc());
    }
//    计算总收支
    public void compute(){
        float allIncome=0;
        float allPay=0;
        for (int i=0;i<listData.size();i++){
            if(listData.get(i).isPay()){
                allPay+=listData.get(i).getMoney();
            }
            else{
                allIncome+=listData.get(i).getMoney();
            }
        }
        incomeText.setText(String.valueOf(allIncome));
        payText.setText(String.valueOf(allPay));
    }
}
