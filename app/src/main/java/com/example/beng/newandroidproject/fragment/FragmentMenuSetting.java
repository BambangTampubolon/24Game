package com.example.beng.newandroidproject.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.beng.newandroidproject.interfaces.OnMenuClickListener;
import com.example.beng.newandroidproject.R;

public class FragmentMenuSetting extends Fragment{

    public OnMenuClickListener onMenuClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_setting, container, false);
        Button playerButton = view.findViewById(R.id.button_player);
        Button buttonRoundTimer = view.findViewById(R.id.button_round_time);
        Button buttonAnswerTimer = view.findViewById(R.id.button_answer_time);

        playerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMenuClickListener.onButtonMenuSelected("player");
            }
        });

        buttonRoundTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMenuClickListener.onButtonMenuSelected("roundtimer");
            }
        });

        buttonAnswerTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMenuClickListener.onButtonMenuSelected("answertimer");
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnMenuClickListener){
            onMenuClickListener = (OnMenuClickListener) context;
        }else {
            throw new ClassCastException(context.toString() + "must implement" + "OnMenuSelectedListener");
        }
    }
}
