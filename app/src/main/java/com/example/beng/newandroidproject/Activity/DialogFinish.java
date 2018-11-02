package com.example.beng.newandroidproject.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.beng.newandroidproject.Adapter.RankUserAdapter;
import com.example.beng.newandroidproject.R;
import com.example.beng.newandroidproject.User;

import java.util.List;

public class DialogFinish extends Activity {
    Button okButton;
    ListView listUser;
    List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_finish);
        okButton = findViewById(R.id.button_ok);
        listUser = findViewById(R.id.list_rank_user);

        Intent intent = getIntent();
        final Intent toInGameActivityIntent = new Intent(this, InGameActivity.class);
        if(null != intent.getSerializableExtra("list_user")) {
            userList = (List<User>) intent.getSerializableExtra("list_user");
        }
        RankUserAdapter rankAdapter = new RankUserAdapter(userList, this);
        listUser.setAdapter(rankAdapter);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(toInGameActivityIntent);
            }
        });

    }

}
