package com.example.keepacount.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.keepacount.R;

public class Fragment3 extends Fragment {
    boolean end; //计算是否结束
    TextView textView1,textView2;
    Button [] buttons = new Button[17];
    int [] btnID = new int[]{R.id.btn0,R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,R.id.btn5,
            R.id.btn6,R.id.btn7,R.id.btn8,R.id.btn9,R.id.btnC,R.id.btnPlus,R.id.btnSub,
            R.id.btnMul,R.id.btnDiv,R.id.btnPoint,R.id.btnEqual};






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.index_fragment3,container,false);
        initView(view);

        return view;
    }
//  初始化视图
    public void initView(View view){
        textView1=view.findViewById(R.id.formula);
        textView2=view.findViewById(R.id.result);
        for (int i=0;i<btnID.length;i++) {
            buttons[i] =view.findViewById(btnID[i]);
            buttons[i].setOnClickListener(this::onClick);
        }
    }


//  点击事件
    public void onClick(View v){
        String str = textView1.getText().toString();
        switch (v.getId()){
//            数字、小数点按钮
            case R.id.btn0:
            case R.id.btn1:
            case R.id.btn2:
            case R.id.btn3:
            case R.id.btn4:
            case R.id.btn5:
            case R.id.btn6:
            case R.id.btn7:
            case R.id.btn8:
            case R.id.btn9:
            case R.id.btnPoint:
//                如果计算结束，清空计算式
                if (end){
                    end = false;
                    str = "";
                    textView1.setText("");
                }
//                获取按钮上的数字
                textView1.setText(str+((Button)v).getText());
                break;

//                计算符号+、-、*、/
            case R.id.btnPlus:
            case R.id.btnSub:
            case R.id.btnMul:
            case R.id.btnDiv:
//                如果计算结束，清空计算列表
                if (end){
                    end = false;
                    str = "";
                    textView1.setText("");
                }
//                获取按钮上的符号
                textView1.setText(str+" "+((Button)v).getText()+" ");
                break;

//                清空按钮
            case R.id.btnC:
//                将计算式、计算结果清空
                end = false;
                str = "";
                textView1.setText("");
                textView2.setText("");
                break;
//              等于按钮
            case R.id.btnEqual:
//                获取计算式结果
                getResult();
                break;
        }
    }

//                获取计算式结果
    public void getResult(){
        String exp = textView1.getText().toString();
        if (exp.equals("")) return;//算式为空
        if (!exp.contains(" ")) return;//无运算符
        end = true;
        double result = 0;

        //仅完成两个数字间的运算，无混合运算
        //运算符前的数字
        String s1=exp.substring(0,exp.indexOf(" "));
        //运算符
        String op=exp.substring(exp.indexOf(" ")+1,exp.indexOf(" ")+2);
        //运算符后的数字
        String s2=exp.substring(exp.indexOf(" ")+3);
        //有两个数字
        if (!s1.equals("")&&!s2.equals("")) {
            double n1 = Double.parseDouble(s1);
            double n2 = Double.parseDouble(s2);
            if(op.equals("+")){
                result=n1+n2;
            }else if(op.equals("-")){
                result=n1-n2;
            }else if(op.equals("*")){
                result=n1*n2;
            }else if(op.equals("/")) {
                if (n2 == 0) textView2.setText("不能除以0");//如果被除数是0
                else result = n1/n2;
            }
        }
        //只有运算符
        else if (s1.equals("")&&s2.equals("")) {
            result = 0;
        }

        //运算符后无数字  有s1无s2
        else if (!s1.equals("")&&s2.equals("")) {
            double n1 = Double.parseDouble(s1);
            result = n1;
        }
        //运算符前无数字
        else if (s1.equals("")&&!s2.equals("")) {
            double n2 = Double.parseDouble(s2);
            if(op.equals("+")){
                result=n2;
            }else if(op.equals("-")) {
                result=0-n2;
            }else if(op.equals("*")||op.equals("/")){
                result=0;
            }
        }
        textView2.setText("= "+result);
    }


}
