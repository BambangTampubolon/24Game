package com.example.beng.newandroidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewUserActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.exapmple";
    private EditText editTextUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        editTextUser = findViewById(R.id.edit_word);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if(TextUtils.isEmpty(editTextUser.getText())){
                    setResult(RESULT_CANCELED, replyIntent);
                }else {
                    String user = editTextUser.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, user);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
