package com.example.beng.newandroidproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.beng.newandroidproject.R;
import com.example.beng.newandroidproject.User;

import java.util.List;

public class RankUserAdapter extends BaseAdapter {
    private Context context;
    private List<User> userList;

    public RankUserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public User getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return userList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(this.context).inflate(R.layout.user_rank_item, viewGroup, false);
        TextView userRank = view.findViewById(R.id.rank_user);
        TextView userName = view.findViewById(R.id.user_name_rank);
        TextView answerCount = view.findViewById(R.id.user_answer_count);
        userRank.setText(String.valueOf(i+1));
        userName.setText(userList.get(i).getNama());
        answerCount.setText(userList.get(i).getTotalCorrect().toString());
        return view;
    }
}
