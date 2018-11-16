package com.example.beng.newandroidproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.beng.newandroidproject.R;
import com.example.beng.newandroidproject.User;

import java.util.List;

public class UserAdapter extends BaseAdapter{
    private List<User> userList;
    private Context context;

    public UserAdapter(List<User> userList, Context context){
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
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(this.context).inflate(R.layout.user_floating_layout, viewGroup, false);
        FrameLayout userLayout = view.findViewById(R.id.frame_player);
        TextView userNameView = view.findViewById(R.id.username_player);
        TextView userScoreView = view.findViewById(R.id.score_player);
        userNameView.setText(userList.get(i).getNama());
        userScoreView.setText(userList.get(i).getTotalCorrect().toString());
        return view;
    }
}
