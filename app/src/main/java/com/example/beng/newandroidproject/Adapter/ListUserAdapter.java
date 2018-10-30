package com.example.beng.newandroidproject.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.beng.newandroidproject.Interface.SpinnerInterface;
import com.example.beng.newandroidproject.R;
import com.example.beng.newandroidproject.StaticVariable;
import com.example.beng.newandroidproject.User;

import java.util.List;

public class ListUserAdapter extends BaseAdapter {

    private Context context;
    private List<User> listUser;
    private SpinnerInterface spinnerInterface;

    public ListUserAdapter(Context context, List<User> listUser, SpinnerInterface spinnerInterface){
        this.context = context;
        this.listUser = listUser;
        this.spinnerInterface = spinnerInterface;
    }


    @Override
    public int getCount() {
        return this.listUser.size();
    }

    @Override
    public User getItem(int i) {
        return this.listUser.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(this.context).inflate(R.layout.list_user_item, viewGroup, false);
            final int index = i;
            TextView colorTextView = view.findViewById(R.id.user_color);
            final EditText userTextView  = view.findViewById(R.id.user_name);
            final LinearLayout layout_user = view.findViewById(R.id.view_item_user);
            Button buttonLock = view.findViewById(R.id.button_lock);
            if(listUser.get(i).isLocked()) {
                layout_user.setBackgroundColor(Color.parseColor(StaticVariable.COLOR_LINEAR));
                buttonLock.setText(R.string.unlock_button);
                userTextView.setText(listUser.get(i).getNama());
                userTextView.setEnabled(!listUser.get(i).isLocked());
            }
            else {
                layout_user.setBackgroundColor(0);
                buttonLock.setText(R.string.lock_button);
                userTextView.setText("");
                userTextView.setEnabled(!listUser.get(i).isLocked());
            }
            buttonLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    spinnerInterface.SpinnerUserItemClick(view, index, userTextView.getText().toString(),listUser.get(index).isLocked());
                }
            });
            switch (i){
                case 0:
                    colorTextView.setBackgroundResource(R.color.amber);
                    break;
                case 1:
                    colorTextView.setBackgroundResource(R.color.red);
                    break;
                case 2:
                    colorTextView.setBackgroundResource(R.color.deep_orange);
                    break;
                case 3:
                    colorTextView.setBackgroundResource(R.color.indigo);
                    break;
                case 4:
                    colorTextView.setBackgroundResource(R.color.green);
                    break;
                case 5:
                    colorTextView.setBackgroundResource(R.color.deep_brown);
                    break;
                case 6:
                    colorTextView.setBackgroundResource(R.color.blue);
                    break;
                case 7:
                    colorTextView.setBackgroundResource(R.color.deep_purple);
                    break;
                        default:
                            colorTextView.setBackgroundResource(R.color.brown);
                        break;


            }
        return view;
    }
}
