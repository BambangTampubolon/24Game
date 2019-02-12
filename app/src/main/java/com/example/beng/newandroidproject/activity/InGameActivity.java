package com.example.beng.newandroidproject.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beng.newandroidproject.adapter.CardAdapter;
import com.example.beng.newandroidproject.entity.Card;
import com.example.beng.newandroidproject.fragment.FragmentFinishGame;
import com.example.beng.newandroidproject.fragment.FragmentInGameStart;
import com.example.beng.newandroidproject.fragment.FragmentPause;
import com.example.beng.newandroidproject.fragment.RankPlayerFragment;
import com.example.beng.newandroidproject.interfaces.CardAdapterInterface;
import com.example.beng.newandroidproject.interfaces.DialogOptionInterface;
import com.example.beng.newandroidproject.interfaces.FragmentCickListener;
import com.example.beng.newandroidproject.R;
import com.example.beng.newandroidproject.User;
import com.example.beng.newandroidproject.UserDao;
import com.example.beng.newandroidproject.UserRoomDatabase;
import com.example.beng.newandroidproject.interfaces.FragmentPauseInterface;
import com.example.beng.newandroidproject.interfaces.InGameStartInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InGameActivity extends Activity implements CardAdapterInterface, FragmentCickListener, DialogOptionInterface, InGameStartInterface,
        FragmentPauseInterface{
    private List<Card> listRecyclerCard = new ArrayList<>();
    private List<Card> initialDeckCard;
    private CardAdapter cardAdapter;
    private List<User> userGet;
    private FrameLayout frameLayout1, frameLayout2, frameLayout3, frameLayout4,
            frameLayout5, frameLayout6, frameLayout7, frameLayout8;
    private Button button1, button2, button3, button4,
            button5, button6, button7, button8;
    private TextView userName1, userName2, userName3, userName4, userName5,
            userName6, userName7, userName8, timerText;
    private TextView userScore1, userScore2, userScore3, userScore4, userScore5,
            userScore6, userScore7, userScore8;
    private List<FrameLayout> usersFrameLayout = new ArrayList<>();
    private List<TextView> usersNameText = new ArrayList<>();
    private List<TextView> usersScoreText = new ArrayList<>();
    private long[] idsUser;
    private int timerCount, timeRemaining;
    private FragmentManager fragmentManager;
    private RankPlayerFragment rankPlayerFragment;
    private FragmentFinishGame fragmentFinishGame;
    private FragmentInGameStart fragmentInGameStart;
    private FragmentPause fragmentPause;
    private Intent intentToDialogFinish;
    private Intent intentToAnswer;
    private Toast toast;
    private CountDownTimer countDownTimer;
    private Animation animation;

    public void setTimerCount(int timerCount) {
        this.timerCount = timerCount;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);
        frameLayout1 = findViewById(R.id.frame_player1);
        frameLayout2 = findViewById(R.id.frame_player2);
        frameLayout3 = findViewById(R.id.frame_player3);
        frameLayout4 = findViewById(R.id.frame_player4);
        frameLayout5 = findViewById(R.id.frame_player5);
        frameLayout6 = findViewById(R.id.frame_player6);
        frameLayout7 = findViewById(R.id.frame_player7);
        frameLayout8 = findViewById(R.id.frame_player8);
        button1 = findViewById(R.id.player1);
        button2 = findViewById(R.id.player2);
        button3 = findViewById(R.id.player3);
        button4 = findViewById(R.id.player4);
        button5 = findViewById(R.id.player5);
        button6 = findViewById(R.id.player6);
        button7 = findViewById(R.id.player7);
        button8 = findViewById(R.id.player8);
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
        Button pauseButton = findViewById(R.id.pause_button);
        animation = AnimationUtils.loadAnimation(this, R.anim.blink_animation);
        initializeListButtonUser();

        RecyclerView recyclerCardRandomed = findViewById(R.id.recycler_random_card);
        UserDao userDao = UserRoomDatabase.getDatabase(this).userDao();

        cardAdapter = new CardAdapter(listRecyclerCard, this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(InGameActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerCardRandomed.setLayoutManager(linearLayoutManager);
        recyclerCardRandomed.setAdapter(cardAdapter);

        intentToAnswer = new Intent(this, AnswerActivity.class);
        final Intent intent = getIntent();
        initialDeckCard = populateDeckCard();
        if(0 != intent.getIntExtra("count_down_timer", 0)) {
            setTimerCount(intent.getIntExtra("count_down_timer", 0));
        }
        if(null == idsUser){
            idsUser = intent.getLongArrayExtra("idsSaved");
        }
        setCountDownTimer(timerCount);
        try {
            userGet = new getAllSavedAsyncTasl(userDao).execute().get();
            setVisibleLayoutUser(userGet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        intentToDialogFinish = new Intent(this, DialogFinish.class);
        toast = Toast.makeText(this, "No one answer", Toast.LENGTH_SHORT);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusAnswerUser(0);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusAnswerUser(1);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusAnswerUser(2);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusAnswerUser(3);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusAnswerUser(4);
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusAnswerUser(5);
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusAnswerUser(6);
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusAnswerUser(7);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
                setTimerCount(timeRemaining);
                showPauseFragment();
            }
        });
    }

    public void showPauseFragment(){
        fragmentManager = getFragmentManager();
        fragmentPause = new FragmentPause();
        Bundle bundle = new Bundle();
        bundle.putString("title_fragment", "GAME PAUSED \\n RESUME");
        fragmentPause.setArguments(bundle);
        fragmentPause.show(fragmentManager, "tag");
    }

    public void showRankPlayer(){
        fragmentManager = getFragmentManager();
        rankPlayerFragment = new RankPlayerFragment();
        rankPlayerFragment.show(fragmentManager, "tag");
    }

    public void showStartFragment(){
        fragmentManager = getFragmentManager();
        if(null == fragmentInGameStart){
            fragmentInGameStart = new FragmentInGameStart();

        }
        if(!fragmentInGameStart.isVisible()){
            fragmentInGameStart.show(fragmentManager, "OPENING");
        }
    }

    private void setCountDownTimer(long time){
        countDownTimer = new CountDownTimer(timerCount*1000, 1000) {
            @Override
            public void onTick(long l) {
                timerText.setText(String.valueOf(l/1000));
                setTimeRemaining((int) l);
                timerText.startAnimation(animation);
            }

            @Override
            public void onFinish() {
                timerText.setText(String.valueOf(0));
                if(isAnyoneAnswer()){
                    intentToAnswer.putExtra("listCardRandomed", (Serializable) listRecyclerCard);
                    intentToAnswer.putExtra("userList", (Serializable) getAllAnsweringUser());
                    intentToAnswer.putExtra("count_down_timer", timerCount);
                    refreshStatusAnswer();
                    startActivity(intentToAnswer);
                }else {
                    toast.show();
                    clearCardRandomed();
                    showStartFragment();
                }

            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
        showStartFragment();
    }

    @Override
    public void cardSelected(int position) {

    }

    //getting randomed listcard to calculate
    private void getNumberToCalculate(){
        Random random = new Random();
        listRecyclerCard.clear();
        int selected;
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
                newCard.setIndexClick(0);
                myCard.add(newCard);
            }
        }
        for(int y=1; y<=4;y++){
            Card newCard = new Card();
            newCard.setType(y);
            newCard.setValue(1);
            newCard.setSymbol("A");
            newCard.setClicked(false);
            newCard.setIndexClick(0);
            myCard.add(newCard);
        }
        for(int y=1; y<=4;y++){
            Card newCard = new Card();
            newCard.setType(y);
            newCard.setValue(10);
            newCard.setSymbol("J");
            newCard.setClicked(false);
            newCard.setIndexClick(0);
            myCard.add(newCard);
        }
        for(int y=1; y<=4;y++){
            Card newCard = new Card();
            newCard.setType(y);
            newCard.setValue(10);
            newCard.setSymbol("Q");
            newCard.setClicked(false);
            newCard.setIndexClick(0);
            myCard.add(newCard);
        }
        for(int y=1; y<=4;y++){
            Card newCard = new Card();
            newCard.setType(y);
            newCard.setValue(10);
            newCard.setSymbol("K");
            newCard.setClicked(false);
            newCard.setIndexClick(0);
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
        for(int i = 0; i < userList.size(); i++){
            usersFrameLayout.get(i).setVisibility(View.VISIBLE);
            usersNameText.get(i).setText(userList.get(i).getNama());
            usersScoreText.get(i).setText(userList.get(i).getTotalCorrect().toString());
        }
    }

    @Override
    public void closeDialogFragment() {
        rankPlayerFragment.dismiss();
    }

    @Override
    public void okButtonPressed() {
        fragmentFinishGame.dismiss();
        Intent intentToSettingRule = new Intent(this, SettingRuleActivity.class);
        intentToSettingRule.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentToSettingRule.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentToSettingRule);
    }

    @Override
    public void cancelButtonPressed() {
        fragmentFinishGame.dismiss();
    }

    @Override
    public void startGameTimer() {
        fragmentInGameStart.dismiss();
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

    @Override
    public void showRankStanding() {
        showRankPlayer();
    }

    @Override
    public void ResumeGame() {
        this.fragmentPause.dismiss();
        countDownTimer.start();
    }

    private static class getAllSavedAsyncTasl extends AsyncTask<Void, Void, List<User>>{
        private UserDao userDao;

        private getAllSavedAsyncTasl(UserDao userDao){
            this.userDao = userDao;
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
        Log.i("checkindex", "changeStatusAnswerUser: " + index);
        if(userGet.get(index).isAnswering()){
            usersFrameLayout.get(index).setBackground(null);
            userGet.get(index).setAnswering(false);
        }else {
            usersFrameLayout.get(index).setBackground(getDrawable(R.drawable.border));
            userGet.get(index).setAnswering(true);
        }
    }

    private void refreshStatusAnswer(){
        for(int index = 0; index < userGet.size(); index++){
            usersFrameLayout.get(index).setBackground(null);
            userGet.get(index).setAnswering(false);
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


    @Override
    public void onBackPressed() {
        fragmentManager = getFragmentManager();
        fragmentFinishGame = new FragmentFinishGame();
        fragmentFinishGame.show(fragmentManager, "finish");
    }
}
