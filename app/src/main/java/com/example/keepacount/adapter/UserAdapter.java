package com.example.keepacount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keepacount.R;
import com.example.keepacount.entity.UserMsg;
import com.example.keepacount.fragment.Fragment4;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private int resource;
    private List<UserMsg> listData;

    public UserAdapter(Context context, int resource, List listData) {
        this.context = context;
        this.resource = resource;
        this.listData = listData;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        UserAdapter.UserViewHolder viewHolder=new UserAdapter.UserViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        UserMsg u=listData.get(position);
        holder.textView1.setText(u.head);
        holder.textView2.setText(u.body);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        TextView textView1,textView2;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.textView23);
            textView2=itemView.findViewById(R.id.textView24);
        }
    }
}
