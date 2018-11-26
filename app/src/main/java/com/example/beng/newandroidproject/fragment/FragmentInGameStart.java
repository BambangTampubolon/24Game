package com.example.beng.newandroidproject.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.beng.newandroidproject.R;
import com.example.beng.newandroidproject.interfaces.InGameStartInterface;

public class FragmentInGameStart extends DialogFragment{

    private InGameStartInterface inGameStartInterface;
    private TextView textView;
    private Animation animation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingame_start, container, false);
        final Button startButton = view.findViewById(R.id.start_button);
        final Button rankButton = view.findViewById(R.id.rank_button);
        textView = view.findViewById(R.id.text_timer);

        final CountDownTimer countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {
                textView.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                inGameStartInterface.startGameTimer();
            }
        };

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setVisibility(View.GONE);
                rankButton.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.startAnimation(animation);
                countDownTimer.start();
            }
        });

        rankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inGameStartInterface.showRankStanding();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        inGameStartInterface = (InGameStartInterface) context;
        animation = AnimationUtils.loadAnimation(context, R.anim.blink_animation);
    }
}
