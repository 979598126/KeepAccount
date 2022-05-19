package com.example.keepacount.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.keepacount.R;
import com.example.keepacount.activity.QueryAccountActivity;
import com.example.keepacount.adapter.AccountAdapter;
import com.example.keepacount.entity.Account;
import com.example.keepacount.entity.User;
import com.example.keepacount.utils.DBHelper2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Fragment1 extends Fragment {
    ConstraintLayout cl;
    FloatingActionButton appearBtn;
    Button timeBtn,clearBtn,addBtn;
    ImageView foodImg,waterImg,playImg,shopImg,otherImg,incomeImg,payImg,turnToQueryImg,noAccountImg;
    ImageView[] imageViews=new ImageView[5];
    int[] imgID=new int[]{R.id.imageView6,R.id.imageView7,R.id.imageView8,R.id.imageView9,R.id.imageView10};
    TextView timeText,foodText,waterText,playText,shopText,otherText,titleText;
    EditText moneyText,descText;
    SmartRefreshLayout sRL;
    SwipeRecyclerView recyclerView;

    private Account account=new Account();//新增的账单
    private List<Account> listData=new ArrayList<>();
    private User user;
    AccountAdapter pla;
    DBHelper2 dbHelper;

    Calendar calendar=Calendar.getInstance();
    Date date=calendar.getTime();
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    String today=sdf.format(date);

    public Fragment1(User user) {
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.index_fragment1,container,false);

//        初始化视图
        initView(view);

        dbHelper=new DBHelper2(getActivity());

        System.out.println(today);
        List<Account> accountList= dbHelper.selectTodayAccounts(today,user.getUserId());
        listData=accountList;
        isAccount();



//        设置recyclerView的加载和更新
        setSmartRefresh(today);
//        设置数据
//        setListData();
//        侧滑菜单的设置
        setSwipeRecyclerView();

//        RecyclerView的设置
        setRecyclerView();


//        设置点击监听
        setClickListener();
        return view;
    }

//      初始化视图
    public void initView(View view){
        cl=view.findViewById(R.id.addPanel);
        appearBtn=view.findViewById(R.id.floatingActionButton3);
        timeBtn=view.findViewById(R.id.timeBtn);
        clearBtn=view.findViewById(R.id.button6);
        addBtn=view.findViewById(R.id.button7);

        for(int i=0;i<imageViews.length;i++){
            imageViews[i]=view.findViewById(imgID[i]);
            imageViews[i].setOnClickListener(this::onImgClick);
        }
        noAccountImg=view.findViewById(R.id.noAccount);
        turnToQueryImg=view.findViewById(R.id.imageView5);
        incomeImg=view.findViewById(R.id.incomeImg);
        payImg=view.findViewById(R.id.payImg);
        titleText=view.findViewById(R.id.textView6);
        timeText=view.findViewById(R.id.timeText);
        foodText=view.findViewById(R.id.textView12);
        waterText=view.findViewById(R.id.waterText);
        playText=view.findViewById(R.id.playText);
        shopText=view.findViewById(R.id.shopText);
        otherText=view.findViewById(R.id.otherText);
        moneyText=view.findViewById(R.id.editTextNumber);
        descText=view.findViewById(R.id.editTextTextPersonName5);

        sRL=view.findViewById(R.id.refreshLayout);
        recyclerView=view.findViewById(R.id.recyclerView3);
    }

//    设置点击监听
    public void setClickListener(){
//        跳转到查询账单页面
        turnToQueryImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), QueryAccountActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("user",user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
//        显示和隐藏添加账单栏
        appearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cl.getVisibility()== View.GONE){
                    cl.setVisibility(View.VISIBLE);
                }
                else{
                    cl.setVisibility(View.GONE);
                }
                System.out.println();
            }
        });

//        选择时间
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeSelect();
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


//        清空添加栏
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAddAccount();
                cl.setVisibility(View.GONE);
            }
        });

//        添加和修改账单
        addBtn.setOnClickListener(new View.OnClickListener() {
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

//                添加操作
                if(addBtn.getText().toString().equals("添加")){

                    if(dbHelper.insertAccount(account)>0){
                        Toast.makeText(getContext(),"添加成功！",Toast.LENGTH_SHORT).show();
                        clearAddAccount();
                        cl.setVisibility(View.GONE);
                    }
                    else{
                        Toast.makeText(getContext(),"添加失败！",Toast.LENGTH_SHORT).show();
                    }
                }
//              修改操作
                 else{
                     if(dbHelper.updateAccount(account)>0){
                         Toast.makeText(getContext(),"修改成功！",Toast.LENGTH_SHORT).show();
                         clearAddAccount();
                         cl.setVisibility(View.GONE);
                     }
                     else{
                         Toast.makeText(getContext(),"修改失败！",Toast.LENGTH_SHORT).show();
                     }
                }

                    listData=dbHelper.selectTodayAccounts(today,user.getUserId());

                    pla.setListData(listData);
                    pla.notifyDataSetChanged();
                    isAccount();

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
    public void setTimeSelect(){
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2015,1,1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2030,1,1);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
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

//    设置recyclerView的加载和更新
    public void setSmartRefresh(String today){
        //头部刷新样式
        sRL.setRefreshHeader(new ClassicsHeader(getContext()));
        //尾部刷新样式
        sRL.setRefreshFooter(new ClassicsFooter(getContext()));
        sRL.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                sRL.finishRefresh(1000);
                System.out.println("刷新");
                listData=dbHelper.selectTodayAccounts(today,user.getUserId());
                if(isAccount()){
                    pla.setListData(listData);
                    pla.notifyDataSetChanged();
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

    public void setListData(){
        for(int i=0;i<10;i++){
            Account account1=new Account();
            account1.setPay(true);
            account1.setType("饮食");
            account1.setDesc("无");
            account1.setUserId(1);
            account1.setMoney(100.20F);
            account1.setDate("2022-5-15");
            listData.add(account1);
        }
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
                SwipeMenuItem updateItem = new SwipeMenuItem(getContext())
                        .setBackgroundColor(Color.rgb(56, 154, 243))
                        .setText("修改")
                        .setTextSize(18)
                        .setTextColor(Color.WHITE)
                        .setImage(R.drawable.ic_update_small)
                        .setHeight(height)
                        .setWidth(width);
//                 删除按钮
                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext())
                        .setBackground(R.color.red)
                        .setImage(R.drawable.ic_delete_small)
                        .setText("删除")
                        .setTextSize(18)
                        .setTextColor(Color.WHITE)
                        .setHeight(height)
                        .setWidth(width);


            // 各种文字和图标属性设置。
                rightMenu.addMenuItem(updateItem); // 在Item右侧添加一个菜单。
                rightMenu.addMenuItem(deleteItem);
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
//                    更新UI，打开修改栏
                    updateUI(account);

                }
//                删除账单
                else{
//                    定义删除弹窗
                    AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(getContext());
                    alertdialogBuilder.setMessage("您确认要删除该账单记录");
//                    删除账单记录
                    alertdialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(dbHelper.deleteAccount(listData.get(position).getAccountId())>1){
                                Toast.makeText(getContext(),"删除成功！",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getContext(),"删除成功！",Toast.LENGTH_SHORT).show();
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
                AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(getContext());
                alertdialogBuilder.setMessage("备注："+listData.get(position).getDesc());
                alertdialogBuilder.setNegativeButton("确定",null);
                final AlertDialog alertdialog1 = alertdialogBuilder.create();
                alertdialog1.show();
            }
        });

    }

//    RecyclerView的设置
    public void setRecyclerView(){
        pla=new AccountAdapter(getContext(),R.layout.account_item,listData);
        recyclerView.setAdapter(pla);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }


//    清空添加栏
    public void clearAddAccount(){
        // 清除account数据
        account=new Account();
//                恢复UI
        titleText.setText("新建账单");
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
        addBtn.setText("添加");
    }

//    修改添加栏UI
    public void updateUI(Account account){
        cl.setVisibility(View.VISIBLE);
        titleText.setText("修改账单");
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
        addBtn.setText("修改");
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
}
