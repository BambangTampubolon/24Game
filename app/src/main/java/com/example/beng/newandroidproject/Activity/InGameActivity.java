package com.example.beng.newandroidproject.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beng.newandroidproject.Adapter.CardAdapter;
import com.example.beng.newandroidproject.Entity.Card;
import com.example.beng.newandroidproject.Interface.CardAdapterInterface;
import com.example.beng.newandroidproject.R;
import com.example.beng.newandroidproject.User;
import com.example.beng.newandroidproject.UserDao;
import com.example.beng.newandroidproject.UserRoomDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InGameActivity extends AppCompatActivity implements CardAdapterInterface{
    private List<Card> listRecyclerCard = new ArrayList<>();
    private List<Card> initialDeckCard;
    private RecyclerView recyclerCardRandomed;
    private CardAdapter cardAdapter;
    private Button buttonStart, buttonAnswer;
    private LinearLayoutManager linearLayoutManager;
    private UserDao userDao;
    private List<User> userGet;
    private FrameLayout frameLayout1, frameLayout2, frameLayout3, frameLayout4,
            frameLayout5, frameLayout6, frameLayout7, frameLayout8;
    private TextView userName1, userName2, userName3, userName4, userName5,
            userName6, userName7, userName8, timerText;
    private TextView userScore1, userScore2, userScore3, userScore4, userScore5,
            userScore6, userScore7, userScore8;
    private List<FrameLayout> usersFrameLayout = new ArrayList<>();
    private List<TextView> usersNameText = new ArrayList<>();
    private List<TextView> usersScoreText = new ArrayList<>();
    private long[] idsUser;
    private static int timerCount;

    public void setTimerCount(int timerCount) {
        this.timerCount = timerCount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("checkmasukdimodemana", "onCreate: ");
        setContentView(R.layout.activity_in_game);
        frameLayout1 = findViewById(R.id.frame_player1);
        frameLayout2 = findViewById(R.id.frame_player2);
        frameLayout3 = findViewById(R.id.frame_player3);
        frameLayout4 = findViewById(R.id.frame_player4);
        frameLayout5 = findViewById(R.id.frame_player5);
        frameLayout6 = findViewById(R.id.frame_player6);
        frameLayout7 = findViewById(R.id.frame_player7);
        frameLayout8 = findViewById(R.id.frame_player8);
        userName1 = findViewById(R.id.username_player1);
        userName2 = findViewById(R.id.username_player2);
        userName3 = findViewById(R.id.username_player3);
        userName4 = findViewById(R.id.username_player4);
        userName5 = findViewById(R.id.username_player5);
        userName6 = findViewById(R.id.username_player6);
        userName7 = findViewById(R.id.username_player7);
        userName8 = findViewById(R.id.username_player8);
        userScore1 = findViewById(R.id.score_player1);
        userScore2 = findViewById(R.id.score_player2);
        userScore3 = findViewById(R.id.score_player3);
        userScore4 = findViewById(R.id.score_player4);
        userScore5 = findViewById(R.id.score_player5);
        userScore6 = findViewById(R.id.score_player6);
        userScore7 = findViewById(R.id.score_player7);
        userScore8 = findViewById(R.id.score_player8);
        timerText = findViewById(R.id.timer_count);
        initializeListButtonUser();

        recyclerCardRandomed = findViewById(R.id.recycler_random_card);
        buttonStart = findViewById(R.id.startButton);
        buttonAnswer = findViewById(R.id.answerButton);
        userDao = UserRoomDatabase.getDatabase(this).userDao();

        cardAdapter = new CardAdapter(listRecyclerCard, this, this);
        linearLayoutManager = new LinearLayoutManager(InGameActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerCardRandomed.setLayoutManager(linearLayoutManager);
        recyclerCardRandomed.setAdapter(cardAdapter);

        final Intent intentToAnswer = new Intent(this, AnswerActivity.class);
        final Intent intent = getIntent();
        initialDeckCard = populateDeckCard();
        if(null != intent.getStringExtra("count_down_timer")) {
            setTimerCount(Integer.valueOf(intent.getStringExtra("count_down_timer")));
        }
        if(null == idsUser){
            idsUser = intent.getLongArrayExtra("idsSaved");
        }
            try {
                userGet = new getAllSavedAsyncTasl(userDao, idsUser).execute().get();
                setVisibleLayoutUser(userGet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        final Intent intentToDialogResult = new Intent(this, DialogResult.class);
        final Intent intentToDialogFinish = new Intent(this, DialogFinish.class);
        final Toast toast = Toast.makeText(this, "No one answer", Toast.LENGTH_SHORT);

        final CountDownTimer countDownTimer = new CountDownTimer(timerCount*1000, 1000) {
            @Override
            public void onTick(long l) {
                timerText.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                if(isAnyoneAnswer()){
                    intentToAnswer.putExtra("listCardRandomed", (Serializable) listRecyclerCard);
                    intentToAnswer.putExtra("userList", (Serializable) getAllAnsweringUser());
                    intentToAnswer.putExtra("count_down_timer", timerCount);
                    startActivity(intentToAnswer);
                }else {
                    toast.show();
                    clearCardRandomed();
                }

            }
        };

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(initialDeckCard.size() == 0){
                    intentToDialogFinish.putExtra("list_user", (Serializable) userGet);
                    startActivity(intentToDialogFinish);
                    countDownTimer.cancel();
                }else {
                    getNumberToCalculate();
                    cardAdapter.notifyDataSetChanged();
                    countDownTimer.start();
                }
            }
        });

        frameLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusAnswerUser(0);
            }
        });

        frameLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusAnswerUser(1);
            }
        });

        frameLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusAnswerUser(2);
            }
        });

        frameLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusAnswerUser(3);
            }
        });

        frameLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusAnswerUser(4);
            }
        });

        frameLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusAnswerUser(5);
            }
        });

        frameLayout7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusAnswerUser(6);
            }
        });

        frameLayout8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusAnswerUser(7);
            }
        });

        buttonAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToAnswer.putExtra("listCardRandomed", (Serializable) listRecyclerCard);
                intentToAnswer.putExtra("userList", (Serializable) getAllAnsweringUser());
                startActivity(intentToAnswer);
            }
        });
    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        Log.i("checkmasukdimodemana", "onRestore: ");
//        idsUser = savedInstanceState.getLongArray("idsUser");
//        timerCount = savedInstanceState.getInt("timer_Count");
//        initialDeckCard = (List<Card>) savedInstanceState.getSerializable("cardList");
//        try {
//            userGet = new getAllSavedAsyncTasl(userDao, idsUser).execute().get();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        Log.i("checkmasukdimodemana", "onSaveInstanceState: " + "sampesini");
//        super.onSaveInstanceState(outState, outPersistentState);
//        outState.putLongArray("idsUser", idsUser);
//        outState.putSerializable("cardList", (Serializable) initialDeckCard);
//        outState.putInt("timer_Count", this.timerCount);
//    }

    @Override
    public void cardSelected(int position) {

    }


    //getting randomed listcard to calculate
    private void getNumberToCalculate(){
        Random random = new Random();
        listRecyclerCard.clear();
        int selected;
        Log.i("ceksebelum", "getNumberToCalculate: " + initialDeckCard.size());
        for(int i=0; i<4; i++){
            if(initialDeckCard.size() == 1){
                selected = 0;
                Card myCard = initialDeckCard.get(selected);
                listRecyclerCard.add(myCard);
                initialDeckCard.remove(selected);
            }else {
                selected = random.nextInt(initialDeckCard.size() - 1);
                Card myCard = initialDeckCard.get(selected);
                listRecyclerCard.add(myCard);
                initialDeckCard.remove(selected);
            }
        }
        Log.i("ceksesudah", "getNumberToCalculate: " + initialDeckCard.size());
    }

    //populate 1 deck card that will be randomed
    public List<Card> populateDeckCard(){
        List<Card> myCard = new ArrayList<>();
        for(int i = 1; i<=4; i++){
            for (int j=2; j<=10; j++){
                Card newCard = new Card();
                newCard.setType(i);
                newCard.setValue(j);
                newCard.setSymbol(String.valueOf(j));
                newCard.setClicked(false);
                myCard.add(newCard);
            }
        }
        for(int y=1; y<=4;y++){
            Card newCard = new Card();
            newCard.setType(y);
            newCard.setValue(1);
            newCard.setSymbol("A");
            newCard.setClicked(false);
            myCard.add(newCard);
        }
        for(int y=1; y<=4;y++){
            Card newCard = new Card();
            newCard.setType(y);
            newCard.setValue(10);
            newCard.setSymbol("J");
            newCard.setClicked(false);
            myCard.add(newCard);
        }
        for(int y=1; y<=4;y++){
            Card newCard = new Card();
            newCard.setType(y);
            newCard.setValue(10);
            newCard.setSymbol("Q");
            newCard.setClicked(false);
            myCard.add(newCard);
        }
        for(int y=1; y<=4;y++){
            Card newCard = new Card();
            newCard.setType(y);
            newCard.setValue(10);
            newCard.setSymbol("K");
            newCard.setClicked(false);
            myCard.add(newCard);
        }
        return myCard;
    }

    private void initializeListButtonUser(){
        usersFrameLayout.add(frameLayout1);
        usersFrameLayout.add(frameLayout2);
        usersFrameLayout.add(frameLayout3);
        usersFrameLayout.add(frameLayout4);
        usersFrameLayout.add(frameLayout5);
        usersFrameLayout.add(frameLayout6);
        usersFrameLayout.add(frameLayout7);
        usersFrameLayout.add(frameLayout8);
        usersNameText.add(userName1);
        usersNameText.add(userName2);
        usersNameText.add(userName3);
        usersNameText.add(userName4);
        usersNameText.add(userName5);
        usersNameText.add(userName6);
        usersNameText.add(userName7);
        usersNameText.add(userName8);
        usersScoreText.add(userScore1);
        usersScoreText.add(userScore2);
        usersScoreText.add(userScore3);
        usersScoreText.add(userScore4);
        usersScoreText.add(userScore5);
        usersScoreText.add(userScore6);
        usersScoreText.add(userScore7);
        usersScoreText.add(userScore8);
    }

    private void setVisibleLayoutUser(List<User> userList){
        Log.i("checkuserrule", "setVisibleLayoutUser: " + userList.size());
        for(int i = 0; i < userList.size(); i++){
            Log.i("checkuserrules", "looping: " + i);
            usersFrameLayout.get(i).setVisibility(View.VISIBLE);
            usersNameText.get(i).setText(userList.get(i).getNama());
            usersScoreText.get(i).setText(userList.get(i).getTotalCorrect().toString());
        }
    }

    private void setBackgroundClicked(int i){
        usersFrameLayout.get(i).setBackground(getDrawable(R.drawable.border));
    }

    private static class getAllSavedAsyncTasl extends AsyncTask<Void, Void, List<User>>{
        private UserDao userDao;
        private long[]  idsUser;

        private getAllSavedAsyncTasl(UserDao userDao, long[] idSaved){
            this.userDao = userDao;
            this.idsUser = idSaved;
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            return userDao.getAllUsers();
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
        }
    }

    public void clearCardRandomed(){
        listRecyclerCard.clear();
        cardAdapter.notifyDataSetChanged();
    }

    private void changeStatusAnswerUser(int index){
        if(userGet.get(index).isAnswering()){
            usersFrameLayout.get(index).setBackground(null);
            userGet.get(index).setAnswering(false);
        }else {
            usersFrameLayout.get(index).setBackground(getDrawable(R.drawable.border));
            userGet.get(index).setAnswering(true);
        }

    }

    private List<User> getAllAnsweringUser(){
        List<User> answeringUser = new ArrayList<>();
        for(User user : userGet){
            if(user.isAnswering()){
                answeringUser.add(user);
            }
        }
        return answeringUser;
    }

    private boolean isAnyoneAnswer(){
        List<User> answerUser = getAllAnsweringUser();
        return answerUser.size() != 0;
    }


}
