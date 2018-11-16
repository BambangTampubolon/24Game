package com.example.beng.newandroidproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.beng.newandroidproject.R;
import com.example.beng.newandroidproject.User;

public class DialogResult extends Activity {
    private TextView textResult;
    private Button okButton;
    private int answerResult;
    private CountDownTimer countDownTimer;
    private String userName;
    private User userData;
    boolean isLastUser;

    public void setLastUser(boolean lastUser) {
        isLastUser = lastUser;
    }

    public boolean isLastUser() {
        return isLastUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_result);
        this.setFinishOnTouchOutside(false);
        textResult = findViewById(R.id.resultText);
        okButton = findViewById(R.id.okButton);
        Intent intent = getIntent();
        userName = intent.getStringExtra("user_name");
        setLastUser(intent.getBooleanExtra("isLastPerson", false));
        final boolean isTrue = intent.getBooleanExtra("result", false);
        Log.i("cekbooleanistrue", "onCreate: " + isTrue);
        if(isTrue){
            textResult.setText("Jawaban "  + userName +" benar");
        }else {
            textResult.setText("Jawaban "  + userName +" salah");
        }

        countDownTimer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if(isLastUser()){
                    moveToQuizActivity(null, isTrue);
                }else {
                    moveToAnswerActivity();
                }
            }
        };
        Log.i("checkbollastuser", "onCreate: " + isLastUser());
        countDownTimer.start();
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLastUser()){
                    moveToQuizActivity(null, isTrue);
                }else {
                    moveToAnswerActivity();
                }

            }
        });
    }

    public void moveToAnswerActivity(){
        Intent intent = new Intent(this, AnswerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void moveToQuizActivity(User player, boolean isTrue){
        Intent intent = new Intent(this, InGameActivity.class);
        intent.putExtra("result", isTrue);
        startActivity(intent);
    }
}
