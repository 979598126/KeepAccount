package com.example.keepacount.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.keepacount.R;
import com.example.keepacount.entity.Account;
import com.example.keepacount.utils.DBHelper2;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Fragment2 extends Fragment {
    LinearLayout ll;
    PieChart pieChart;
    TextView yearTest,monthText,incomeText,payText,descText;
    LineChart lineChart;
    Button chartBtn1,chartBtn2;


    LineDataSet lineDataSet;
    private List<Account> listData;

    String[] type={"饮食","水电","娱乐","网购","其他"};
    float[] allMoneys;

    DBHelper2 dbHelper;
    private int userId;
    //    获取当前的年月
    Calendar calendar=Calendar.getInstance();
    Date date=calendar.getTime();
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
    String time=sdf.format(date);


    public Fragment2(int userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.index_fragment2,container,false);

//        初始化视图
        initView(view);
//        获取账单数据
        dbHelper=new DBHelper2(getActivity());
        getListData();
//        设置点击监听
        setClickListener();
        setLineCart();
        setPieChart();


        return view;
    }
//        初始化视图
    public void initView(View view){
        ll=view.findViewById(R.id.linearLayout6);
        yearTest=view.findViewById(R.id.textView27);
        monthText=view.findViewById(R.id.textView29);
        payText=view.findViewById(R.id.textView40);
        incomeText=view.findViewById(R.id.textView32);
        descText=view.findViewById(R.id.textView42);
        lineChart=view.findViewById(R.id.chart);
        chartBtn1=view.findViewById(R.id.button5);
        chartBtn2=view.findViewById(R.id.button8);
        pieChart=view.findViewById(R.id.chart2);
    }
//      设置点击监听
    public void setClickListener(){
//        选择年月
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeSelect();
            }
        });
//      生成折线图
        chartBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pieChart.setVisibility(View.GONE);
                lineChart.setVisibility(View.VISIBLE);
            }
        });
//        生成饼状图
        chartBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pieChart.setVisibility(View.VISIBLE);
                lineChart.setVisibility(View.GONE);
            }
        });
    }
//      获取账单数据
    public void getListData(){
        String[] x= time.split("-");
        String year=x[0];
        String month=x[1];
        yearTest.setText(year+"年");
        monthText.setText(month+"月");
        System.out.println(year+"年"+month+"月");
        List<Account> accountList= dbHelper.selectAccounts(time,userId);
        listData=accountList;
        compute();
        compute2();
    }

//  设置时间选择器
    public void setTimeSelect(){
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2015,1,1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2030,1,1);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
//        回调函数
        TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date,View v) {//选中事件回调
                String[] x= sdf.format(date).split("-");
                String year=x[0];
                String month=x[1];
                yearTest.setText(year+"年");
                monthText.setText(month+"月");
                time=sdf.format(date);
                System.out.println(year+"年"+month+"月");
                List<Account> accounts= dbHelper.selectAccounts(time,userId);
                listData=accounts;
                compute();
                compute2();
                setLineCart();
                setPieChart();
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
//        计算每项类型的总收支
    public void compute2(){
        allMoneys=new float[5];
        for(int i=0;i<listData.size();i++){
            if(listData.get(i).getType().equals("饮食")){
                allMoneys[0]+=listData.get(i).getMoney();
            }
            else if(listData.get(i).getType().equals("水电")){
                allMoneys[1]+=listData.get(i).getMoney();
            }
            else if(listData.get(i).getType().equals("娱乐")){
                allMoneys[2]+=listData.get(i).getMoney();
            }
            else if(listData.get(i).getType().equals("网购")){
                allMoneys[3]+=listData.get(i).getMoney();
            }
            else if(listData.get(i).getType().equals("其他")){
                allMoneys[4]+=listData.get(i).getMoney();
            }
        }
        float min=allMoneys[0];
        int index=0;
        for(int j=0;j<allMoneys.length-1;j++){
            if(min>allMoneys[j+1]){
                min=allMoneys[j+1];
                index=j+1;
            }
        }
        if(allMoneys[0]==0&&allMoneys[1]==0&&allMoneys[2]==0&&allMoneys[3]==0&&allMoneys[4]==0){
            descText.setText("本月暂无花销");
        }
        else{
            descText.setText("本月花费最多的类型为："+type[index]);
        }

    }

    //  设置折线图
    public void setLineCart(){
        XAxis xAxis=lineChart.getXAxis();//获取此图表的 x 轴轴线
        YAxis yAxisleft =lineChart.getAxisLeft();//获取此图表的 Y 轴左侧轴线
        YAxis yAxisright =lineChart.getAxisRight();//获取此图表的 Y轴右侧轴线
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置 X 轴线的位置为底部
        xAxis.setGranularity(1f);
//        yAxisleft.setAxisMinimum(0f);//保证 Y 轴从 0 开始，不然会上移一点。
        yAxisright.setAxisMinimum(0f);
//        设置X轴的底部坐标显示
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return type[(int) value];
            }
        });

        List<Entry> inentries=new ArrayList<>();//Y 轴的数据
        inentries.add(new Entry(0,allMoneys[0]));
        inentries.add(new Entry(1,allMoneys[1]));
        inentries.add(new Entry(2,allMoneys[2]));
        inentries.add(new Entry(3,allMoneys[3]));
        inentries.add(new Entry(4,allMoneys[4]));
        lineDataSet=new LineDataSet(inentries,"金额");//代表一条线,“金额”是曲线名称
        lineDataSet.setValueTextSize(25);//曲线上文字的大小
        lineDataSet.setValueTextColor(Color.RED);//曲线上文字的颜色
        lineDataSet.setDrawFilled(true);//设置折线图填充
//        设置以最高的支出类型的数据为起点
        lineDataSet.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return yAxisleft.getAxisMinimum();
            }
        });
        LineData data=new LineData(lineDataSet);//创建 LineData 对象 属于LineChart 折线图的数据集合
        lineChart.setData(data);
    }

    // 设置饼状图
    public void setPieChart(){
        List<PieEntry> pieEntries = new ArrayList<>();
        //ArrayList<String> rate = new ArrayList<>();
//        饼状图显示的数据
        for(int i = 0; i < allMoneys.length; i++) {
            //DecimalFormat df = new DecimalFormat("0.00%");
            //String decimal = df.format(allMoneys[i] / (allMoneys[0] + allMoneys[1] + allMoneys[2] + allMoneys[3] + allMoneys[4]));
            PieEntry pieEntry = new PieEntry(allMoneys[i] / (allMoneys[0] + allMoneys[1] + allMoneys[2] + allMoneys[3] + allMoneys[4]), type[i]);
            pieEntries.add(pieEntry);
            //rate.add(decimal);
        }
//      饼状图各块的颜色
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#6BE61A"));
        colors.add(Color.parseColor("#4474BB"));
        colors.add(Color.parseColor("#AA7755"));
        colors.add(Color.parseColor("#BB5C44"));
        colors.add(Color.parseColor("#E61A1A"));

//        饼状图的具体属性设置
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setSliceSpace(1f); //设置个饼状图之间的距离
        pieDataSet.setColors(colors);

        //是否在图上显示数值
        pieDataSet.setDrawValues(true);
//        文字的大小
        pieDataSet.setValueTextSize(14);
//        文字的颜色
        pieDataSet.setValueTextColor(Color.RED);

        //      当值位置为外边线时，表示线的前半段长度。
        pieDataSet.setValueLinePart1Length(0.4f);
//      当值位置为外边线时，表示线的后半段长度。
        pieDataSet.setValueLinePart2Length(0.4f);
//      当ValuePosits为OutsiDice时，指示偏移为切片大小的百分比
        pieDataSet.setValueLinePart1OffsetPercentage(80f);
        // 当值位置为外边线时，表示线的颜色。
        pieDataSet.setValueLineColor(Color.parseColor("#a1a1a1"));
//        设置Y值的位置是在圆内还是圆外
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        设置Y轴描述线和填充区域的颜色一致
        pieDataSet.setUsingSliceColorAsValueLineColor(false);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setDrawCenterText(true);//显示中间文字
        pieChart.setCenterText("类型");
        pieChart.setCenterTextSize(20);//中间文字大小
        pieChart.setCenterTextColor(Color.parseColor("#3CC4C4"));//中间文字颜色
        pieChart.setEntryLabelTextSize(0);//标签不显示
        Description description = new Description();//设置描述
        description.setText("");
        pieChart.setDescription(description);
        Legend legend = pieChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextSize(12);

//        显示饼状图
        pieChart.animateXY(1000, 1000);
        pieChart.invalidate();

    }



}