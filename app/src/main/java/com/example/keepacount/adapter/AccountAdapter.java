package com.example.keepacount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keepacount.R;
import com.example.keepacount.entity.Account;
import com.example.keepacount.entity.UserMsg;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {
    private Context context;
    private int resource;
    private List<Account> listData;

    public AccountAdapter(Context context, int resource, List listData) {
        this.context = context;
        this.resource = resource;
        this.listData = listData;
    }

    public void setListData(List<Account> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        AccountAdapter.AccountViewHolder viewHolder=new AccountAdapter.AccountViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountAdapter.AccountViewHolder holder, int position) {
        Account account=listData.get(position);
        switch (account.getType()){
            case "饮食":
                holder.imageView.setImageResource(R.drawable.ic_food_selected);
                break;
            case "水电":
                holder.imageView.setImageResource(R.drawable.ic_water_selected);break;
            case "娱乐":holder.imageView.setImageResource(R.drawable.ic_play_selected);break;
            case "网购":holder.imageView.setImageResource(R.drawable.ic_shop_selected);break;
            case "其他":holder.imageView.setImageResource(R.drawable.ic_other_selected);break;
        }
        holder.textView1.setText(account.getDate());
        holder.textView2.setText(account.getType());
        holder.textView3.setText(account.getMoney()+"");
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class AccountViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView1,textView2,textView3;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.textView26);
            textView2=itemView.findViewById(R.id.textView7);
            textView3=itemView.findViewById(R.id.textView25);
            imageView=itemView.findViewById(R.id.imageView15);

        }
    }
}
