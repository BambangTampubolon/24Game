package com.example.beng.newandroidproject.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.beng.newandroidproject.adapter.ListUserAdapter;
import com.example.beng.newandroidproject.fragment.FragmentSettingAnswerTime;
import com.example.beng.newandroidproject.fragment.FragmentSettingPlayer;
import com.example.beng.newandroidproject.fragment.FragmentSettingRoundTime;
import com.example.beng.newandroidproject.fragment.StateSelectionFragment;
import com.example.beng.newandroidproject.interfaces.FragmentAnswerInterface;
import com.example.beng.newandroidproject.interfaces.FragmentPlayerInterface;
import com.example.beng.newandroidproject.interfaces.FragmentRoundInterface;
import com.example.beng.newandroidproject.interfaces.OnMenuClickListener;
import com.example.beng.newandroidproject.interfaces.SpinnerInterface;
import com.example.beng.newandroidproject.R;
import com.example.beng.newandroidproject.StaticVariable;
import com.example.beng.newandroidproject.User;
import com.example.beng.newandroidproject.UserDao;
import com.example.beng.newandroidproject.UserRoomDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SettingRuleActivity extends Activity implements AdapterView.OnItemSelectedListener,
        SpinnerInterface, OnMenuClickListener, FragmentPlayerInterface, FragmentAnswerInterface, FragmentRoundInterface{
    private List<User> listUserSelected;
    private ListUserAdapter adapterListUser;
    private Button dummyButton;
    private UserDao userDao;
    private LinearLayout linearLeft, linearRight;
    private int timerRound;

    StateSelectionFragment stateSelectionFragment;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;
    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_rule);
        dummyButton = findViewById(R.id.dummy_button);
        listUserSelected = new ArrayList<>();
        adapterListUser = new ListUserAdapter(this, listUserSelected, this);
        userDao = UserRoomDatabase.getDatabase(this).userDao();

        stateSelectionFragment = (StateSelectionFragment) getFragmentManager().findFragmentByTag("headless");
        if(null == stateSelectionFragment){
            stateSelectionFragment = new StateSelectionFragment();
            getFragmentManager().beginTransaction().add(stateSelectionFragment, "headless").commit();
        }

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        final Intent toMainActivityIntent = new Intent(this, InGameActivity.class);
        dummyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    long[] idsSaved = new insertAllUserAsyncTask(userDao, listUserSelected, SettingRuleActivity.this).execute().get();
                    toMainActivityIntent.putExtra("count_down_timer", timerRound);
                    toMainActivityIntent.putExtra("idsSaved",idsSaved);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                startActivity(toMainActivityIntent);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        listUserSelected.clear();
        adapterListUser.notifyDataSetChanged();
        int countSelected = Integer.valueOf((String) adapterView.getItemAtPosition(i));
        User newUser;
        for(int index=0; index<countSelected; index++){
            newUser = new User();
            newUser.setLocked(false);
            newUser.setTotalCorrect(0);
            listUserSelected.add(newUser);
        }
        adapterListUser.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void SpinnerUserItemClick(View v, int position, String userName, boolean isLocked) {
        listUserSelected.get(position).setNama(userName);
        listUserSelected.get(position).setLocked(!isLocked);
        adapterListUser.notifyDataSetChanged();
    }

    @Override
    public void onButtonMenuSelected(String value) {
        stateSelectionFragment.lastSelection = value;
        FragmentTransaction transaction;
        if(value.equalsIgnoreCase("player")){
            FragmentSettingPlayer fragmentPlayer = (FragmentSettingPlayer) getFragmentManager().findFragmentByTag(StaticVariable.PLAYER_FRAGMENT);
            if(null == fragmentPlayer){
                Bundle args = new Bundle();
                args.putString(FragmentSettingPlayer.EXTRA_TEXT, value);
                fragmentPlayer = new FragmentSettingPlayer();
                fragmentPlayer.setArguments(args);
            }
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_menu_container, fragmentPlayer, StaticVariable.PLAYER_FRAGMENT);
            transaction.addToBackStack(null);
            transaction.commit();
        }else if (value.equalsIgnoreCase("roundtimer")){
            FragmentSettingRoundTime fragmentRoundTime = (FragmentSettingRoundTime) getFragmentManager().findFragmentByTag(StaticVariable.ROUND_FRAGMENT);
            if(null == fragmentRoundTime){
                Bundle args = new Bundle();
                args.putString(FragmentSettingRoundTime.EXTRA_TEXT, value);
                fragmentRoundTime = new FragmentSettingRoundTime();
                fragmentRoundTime.setArguments(args);
            }
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_menu_container, fragmentRoundTime, StaticVariable.ROUND_FRAGMENT);
            transaction.addToBackStack(null);
            transaction.commit();
        }else if (value.equalsIgnoreCase("answertimer")){
            FragmentSettingAnswerTime fragmentAnswerTime = (FragmentSettingAnswerTime) getFragmentManager().findFragmentByTag(StaticVariable.ANSWER_FRAGMENT);
            if(null == fragmentAnswerTime){
                Bundle args = new Bundle();
                args.putString(FragmentSettingAnswerTime.EXTRA_TEXT, value);
                fragmentAnswerTime = new FragmentSettingAnswerTime();
                fragmentAnswerTime.setArguments(args);
            }
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_menu_container, fragmentAnswerTime, StaticVariable.ANSWER_FRAGMENT);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void sentDataToActivity(int time) {

    }

    @Override
    public void sentDataToActivity(List<User> userList) {
        Log.i("checkusersave", "sentDataToActivity: " + userList.size());
        this.listUserSelected = userList;
    }

    @Override
    public void sentRoundDataToActivity(int time) {
        timerRound = time;
    }

    private static class insertAllUserAsyncTask extends AsyncTask<Void,Void, long[]>{
        private User[] users;
        private UserDao userDao;

        public insertAllUserAsyncTask(UserDao userDao, List<User> users, Context context){
            this.users = users.toArray(new User[users.size()]);
            this.userDao = userDao;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected long[] doInBackground(Void... voids) {
            userDao.deleteAll();
            return userDao.insertUsers(users);
        }

        @Override
        protected void onPostExecute(long[] longs) {
            super.onPostExecute(longs);
        }
    }
}
