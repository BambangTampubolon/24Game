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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.beng.newandroidproject.adapter.CardAdapter;
import com.example.beng.newandroidproject.adapter.UserAdapter;
import com.example.beng.newandroidproject.entity.Card;
import com.example.beng.newandroidproject.fragment.FragmentPause;
import com.example.beng.newandroidproject.interfaces.CardAdapterInterface;
import com.example.beng.newandroidproject.R;
import com.example.beng.newandroidproject.User;
import com.example.beng.newandroidproject.UserDao;
import com.example.beng.newandroidproject.UserRoomDatabase;
import com.example.beng.newandroidproject.interfaces.FragmentPauseInterface;

import java.util.ArrayList;
import java.util.List;

public class AnswerActivity extends Activity implements CardAdapterInterface, FragmentPauseInterface {
    private static List<User> listBackup;
    private List<Card> cardList;
    private List<Card> cardSelected;
    private User answeringUser;
    private RecyclerView recyclerCard;
    private CardAdapter cardAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isSelectedCard1 = false;
    private boolean isSelectedCard2 = false;
    private boolean isSelectedOperator = false;
    private TextView operatorPlus, operatorMinus, operatorTimes, operatorDivide;
    private int indexCard1 = -1;
    private int indexCard2 = -1;
    private int indexOperator = -1;
    private Button resetButton, goButton;
    private UserDao userDao;
    private ListView listViewUser;
    private List<User> userList;
    private UserAdapter listUserAdapter;
    private static int timerCountdown;
    private TextView counterText;
    private int counterPlayer;
    private User[] userBackup;
    private FragmentPause fragmentPause;
    private FragmentManager fragmentManager;

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public static void setTimerCountdown(int timerCountdown) {
        AnswerActivity.timerCountdown = timerCountdown;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_answer);
        operatorPlus = findViewById(R.id.operator1);
        operatorMinus = findViewById(R.id.operator2);
        operatorTimes = findViewById(R.id.operator3);
        operatorDivide = findViewById(R.id.operator4);
        resetButton = findViewById(R.id.reset_button);
        goButton = findViewById(R.id.go_button);
        listViewUser = findViewById(R.id.list_view_user);
        counterText = findViewById(R.id.timer_count);
        Intent intentFromInGame = getIntent();
        if(null != intentFromInGame.getSerializableExtra("listCardRandomed")){
            Log.i("checkmasuksinigak", "onCreate: ");
            cardList = (List<Card>) intentFromInGame.getSerializableExtra("listCardRandomed");
            setUserList((List<User>) intentFromInGame.getSerializableExtra("userList"));
            counterPlayer = 0;
        }

        answeringUser = (User) intentFromInGame.getSerializableExtra("userAnswer");

        setTimerCountdown(intentFromInGame.getIntExtra("count_down_timer", 0));
        listUserAdapter = new UserAdapter(userList, this);
        listViewUser.setAdapter(listUserAdapter);
        cardSelected = new ArrayList<>();
        cardSelected.addAll(cardList);

        userDao = UserRoomDatabase.getDatabase(this).userDao();
        recyclerCard = findViewById(R.id.recycler_randomed_card);
        cardAdapter = new CardAdapter(cardList, this, this);
        linearLayoutManager = new LinearLayoutManager(AnswerActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerCard.setLayoutManager(linearLayoutManager);
        recyclerCard.setAdapter(cardAdapter);

        operatorPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexOperator = setOperator(1);
                changeBackgroundonOperator(1, isSelectedOperator);
                checkCardSelected();
            }
        });

        operatorMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexOperator = setOperator(2);
                changeBackgroundonOperator(2, isSelectedOperator);
                checkCardSelected();
            }
        });

        operatorTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexOperator = setOperator(3);
                changeBackgroundonOperator(3, isSelectedOperator);
                checkCardSelected();
            }
        });

        operatorDivide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexOperator = setOperator(4);
                changeBackgroundonOperator(4, isSelectedOperator);
                checkCardSelected();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetConditionCard();
            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishedAnswer();
            }
        });

        CountDownTimer countDownTimer = new CountDownTimer(timerCountdown*1000, 1000) {
            @Override
            public void onTick(long l) {
                counterText.setText(String.valueOf(l));
            }

            @Override
            public void onFinish() {
                finishedAnswer();
            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
        showIntialFragment();
    }

    private void showIntialFragment(){
        fragmentManager = getFragmentManager();
        fragmentPause = (FragmentPause) fragmentManager.findFragmentByTag("intial");
        if(null == fragmentPause){
            fragmentPause = new FragmentPause();
            Bundle bundle = new Bundle();
            bundle.putString("title_fragment", userList.get(counterPlayer).getNama());
            fragmentPause.setArguments(bundle);
        }
        if(!fragmentPause.isVisible()){
            fragmentPause.show(fragmentManager, "intial");
        }
    }

    private void setCardSelected(int position){
        if(position == indexCard1){
            if(!isSelectedCard2){
                isSelectedCard1 = false;
                indexCard1 = -1;
            }else {
                indexCard1 = indexCard2;
                indexCard2 = -1;
                isSelectedCard2 = false;
            }
            return;
        }
        if(position == indexCard2){
            isSelectedCard2 = false;
            indexCard2 = -1;
            return;
        }
        if(!isSelectedCard1 || !isSelectedCard2){
            if(!isSelectedCard1){
                isSelectedCard1 = true;
                indexCard1 = position;
            }else {
                isSelectedCard2 = true;
                indexCard2 = position;
            }
        }else {
            indexCard1 = indexCard2;
            indexCard2 = position;
        }
    }

    private void checkCardSelected() {
        Card resultCard = new Card();
        int result = 0;
        if (isSelectedCard1 && isSelectedCard2 && isSelectedOperator) {
            switch (indexOperator) {
                case 1:
                    result = cardList.get(indexCard1).getValue() + cardList.get(indexCard2).getValue();
                    break;
                case 2:
                    result = cardList.get(indexCard1).getValue() - cardList.get(indexCard2).getValue();
                    break;
                case 3:
                    result = cardList.get(indexCard1).getValue() * cardList.get(indexCard2).getValue();
                    break;
                case 4:
                    result = cardList.get(indexCard1).getValue() / cardList.get(indexCard2).getValue();
                    break;
                default:
                    break;
            }
            resultCard.setValue(result);
            resultCard.setClicked(false);
            resultCard.setDiscarded(false);
            resultCard.setSymbol("H");
            resultCard.setIndexClick(0);
            swapResultCard(resultCard);
        }
    }

    private void swapResultCard(Card resultCard){
        cardList.remove(indexCard1);
        cardList.add(indexCard1, resultCard);
        cardList.remove(indexCard2);
        indexCard2 = -1;
        indexCard1 = -1;
        indexOperator = -1;
        isSelectedCard1 = false;
        isSelectedCard2 = false;
        isSelectedOperator = false;
        cardAdapter.notifyDataSetChanged();
        clearBackgroundonOperator();
        clearAllBackgroundCard();
    }

    private int setOperator(int indexOperator){
        if(!isSelectedOperator){
            isSelectedOperator = true;
            return  indexOperator;
        }else {
            if(indexOperator == this.indexOperator){
                isSelectedOperator = false;
                return  -1;
            }else{
                return indexOperator;
            }
        }
    }

    private void clearBackgroundonOperator(){
        operatorPlus.setBackground(null);
        operatorMinus.setBackground(null);
        operatorTimes.setBackground(null);
        operatorDivide.setBackground(null);
    }

    private void changeBackgroundonOperator(int indexOperator, boolean isSelectedOperator){
        clearBackgroundonOperator();
        switch (indexOperator){
            case 1:
                if(isSelectedOperator){
                    operatorPlus.setBackground(getDrawable(R.drawable.border));
                }else {
                    operatorPlus.setBackground(null);
                }
                break;
            case 2:
                if(isSelectedOperator){
                    operatorMinus.setBackground(getDrawable(R.drawable.border));
                }else {
                    operatorMinus.setBackground(null);
                }
                break;
            case 3:
                if(isSelectedOperator){
                    operatorTimes.setBackground(getDrawable(R.drawable.border));
                }else {
                    operatorTimes.setBackground(null);
                }
                break;
            case 4:
                if(isSelectedOperator){
                    operatorDivide.setBackground(getDrawable(R.drawable.border));
                }else {
                    operatorDivide.setBackground(null);
                }
                break;
                default:
                    break;
        }
    }

    private void finishedAnswer(){
        Intent intentToDialogResult = new Intent(this, DialogResult.class);
        if(cardList.size() == 1){
            if(cardList.get(0).getValue() == 24) {
                intentToDialogResult.putExtra("result", true);
                new addCountUser(userDao, userList.get(counterPlayer).getId(), userList.get(counterPlayer).getTotalCorrect() + 1 ).execute();
            }else{
                intentToDialogResult.putExtra("result", false);
            }
        }else {
            intentToDialogResult.putExtra("result", false);
        }
        intentToDialogResult.putExtra("user_name", userList.get(counterPlayer).getNama());
        if(counterPlayer == userList.size() -1){
            intentToDialogResult.putExtra("isLastPerson", true);
        }else {
            intentToDialogResult.putExtra("isLastPerson", false);
        }
        counterPlayer += 1;
        resetConditionCard();
        startActivity(intentToDialogResult);
    }

    @Override
    public void cardSelected(int position) {
        setCardSelected(position);
        checkCardSelected();
        setBackgroundCardClicked();
    }

    @Override
    public void ResumeGame() {
        this.fragmentPause.dismiss();
    }

    private static class addCountUser extends AsyncTask<Void, Void, Void> {
        private UserDao userDao;
        private int userId;
        private int totalCount;

        private addCountUser(UserDao userDao, int idSaved, int totalCount){
            this.userDao = userDao;
            this.userId = idSaved;
            this.totalCount = totalCount;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            userDao.addCorrectCount(userId, totalCount);
            return null;
        }
    }

    private void clearAllBackgroundCard(){
        for(Card a: cardList){
            a.setClicked(false);
            a.setIndexClick(0);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    private void setBackgroundCardClicked(){
        clearAllBackgroundCard();
        if(isSelectedCard1){
            cardList.get(indexCard1).setClicked(true);
            cardList.get(indexCard1).setIndexClick(1);
        }
        if(isSelectedCard2){
            cardList.get(indexCard2).setClicked(true);
            cardList.get(indexCard2).setIndexClick(2);
        }
        cardAdapter.notifyDataSetChanged();
    }


    public void resetConditionCard(){
        cardList.clear();
        cardList.addAll(cardSelected);
        indexOperator = -1;
        indexCard1 = -1;
        indexCard2 = -1;
        isSelectedOperator = false;
        isSelectedCard1 = false;
        isSelectedCard2 = false;
        cardAdapter.notifyDataSetChanged();
    }
}
