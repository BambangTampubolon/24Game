package com.example.beng.newandroidproject.Activity;

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
    private User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_result);
        this.setFinishOnTouchOutside(false);
        textResult = findViewById(R.id.resultText);
        okButton = findViewById(R.id.okButton);
        Intent intent = getIntent();
        final boolean isTrue = intent.getBooleanExtra("result", false);
        Log.i("cekbooleanistrue", "onCreate: " + isTrue);
        if(isTrue){
            textResult.setText("Jawaban benar");
        }else {
            textResult.setText("Jawaban salah");
        }

        countDownTimer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                MoveToQuizActivity(null, isTrue);
            }
        };

        countDownTimer.start();
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoveToQuizActivity(null, isTrue);
            }
        });
    }

    public void MoveToQuizActivity(User player, boolean isTrue){
        Intent intent = new Intent(this, InGameActivity.class);
//        intent.putExtra("answeringUser", userData);
        intent.putExtra("result", isTrue);
        startActivity(intent);
    }
}
