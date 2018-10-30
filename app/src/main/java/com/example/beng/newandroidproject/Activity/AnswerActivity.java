package com.example.beng.newandroidproject.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.beng.newandroidproject.Adapter.CardAdapter;
import com.example.beng.newandroidproject.Adapter.ListUserAdapter;
import com.example.beng.newandroidproject.Adapter.UserAdapter;
import com.example.beng.newandroidproject.Entity.Card;
import com.example.beng.newandroidproject.Interface.CardAdapterInterface;
import com.example.beng.newandroidproject.R;
import com.example.beng.newandroidproject.User;
import com.example.beng.newandroidproject.UserDao;
import com.example.beng.newandroidproject.UserListAdapter;
import com.example.beng.newandroidproject.UserRoomDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AnswerActivity extends AppCompatActivity implements CardAdapterInterface{

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
        Intent intentFromInGame = getIntent();
        cardList = (List<Card>) intentFromInGame.getSerializableExtra("listCardRandomed");
        answeringUser = (User) intentFromInGame.getSerializableExtra("userAnswer");
        userList = (List<User>) intentFromInGame.getSerializableExtra("userList");
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
                checkCardSelected();
            }
        });

        operatorMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexOperator = setOperator(2);
                checkCardSelected();
            }
        });

        operatorTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexOperator = setOperator(3);
                checkCardSelected();
            }
        });

        operatorDivide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexOperator = setOperator(4);
                checkCardSelected();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardList.clear();
                cardList.addAll(cardSelected);
                cardAdapter.notifyDataSetChanged();
                indexOperator = -1;
                indexCard1 = -1;
                indexCard2 = -1;
                isSelectedOperator = false;
                isSelectedCard1 = false;
                isSelectedCard2 = false;
            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishedAnswer();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

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

    private void finishedAnswer(){
        Intent intentToDialogResult = new Intent(this, DialogResult.class);
        if(cardList.size() == 1){
            if(cardList.get(0).getValue() == 24) {
                intentToDialogResult.putExtra("result", true);
                new addCountUser(userDao, userList.get(0).getId(), userList.get(0).getTotalCorrect() + 1 ).execute();
            }else{
                intentToDialogResult.putExtra("result", false);
            }
        }else {
            intentToDialogResult.putExtra("result", false);
        }
        startActivity(intentToDialogResult);
    }

    @Override
    public void cardSelected(int position) {
        setCardSelected(position);
        checkCardSelected();
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
}
