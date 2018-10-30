package com.example.beng.newandroidproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserListAdapter  extends RecyclerView.Adapter<UserListAdapter.UserViewHolder>{

    private LayoutInflater mInflater;
    private List<User> mUsers;

    public UserListAdapter(Context context){
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item,parent,false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User current = mUsers.get(position);
        holder.userItemView.setText(current.getNama());
    }

    @Override
    public int getItemCount() {
        if(mUsers!=null){
            return mUsers.size();
        }
        return 0;
    }

    public void setUsers(List<User> Users){
        mUsers = Users;
        notifyDataSetChanged();
    }


    class UserViewHolder extends RecyclerView.ViewHolder{
        private final TextView userItemView;

        private UserViewHolder(View userView){
            super(userView);
            userItemView = userView.findViewById(R.id.textView);
        }
    }
}
