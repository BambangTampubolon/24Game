package com.example.beng.newandroidproject.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.beng.newandroidproject.R;
import com.example.beng.newandroidproject.interfaces.FragmentPauseInterface;

public class FragmentPause extends DialogFragment{

    private FragmentPauseInterface fragmentPauseInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pause, container, false);
        Bundle bundle = getArguments();
        String title = bundle.getString("title_fragment");
        TextView textTitle = view.findViewById(R.id.text_title);
        textTitle.setText(title);
        Button resumeButton = view.findViewById(R.id.resume_button);
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentPauseInterface.ResumeGame();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentPauseInterface = (FragmentPauseInterface) context;
    }
}
