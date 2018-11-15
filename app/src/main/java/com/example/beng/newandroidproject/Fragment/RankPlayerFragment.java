package com.example.beng.newandroidproject.Fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.beng.newandroidproject.Adapter.RankUserAdapter;
import com.example.beng.newandroidproject.Interface.FragmentCickListener;
import com.example.beng.newandroidproject.R;
import com.example.beng.newandroidproject.User;
import com.example.beng.newandroidproject.UserDao;
import com.example.beng.newandroidproject.UserRoomDatabase;

import java.util.List;

public class RankPlayerFragment extends DialogFragment{
    private ListView listViewUser;
    private RankUserAdapter userAdapter;
    private List<User> listUser;
    private UserDao userDao;
    private FragmentCickListener fragmentCickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank_player, container, false);
        Button buttonOk = view.findViewById(R.id.button_ok);
        listViewUser = view.findViewById(R.id.list_view_rank);
        userDao = UserRoomDatabase.getDatabase(getContext()).userDao();
        try {
            listUser = new getAllSaveRankedAsync(userDao).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        userAdapter = new RankUserAdapter(listUser,getContext());
        listViewUser.setAdapter(userAdapter);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentCickListener.closeDialogFragment();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof FragmentCickListener){
            fragmentCickListener = (FragmentCickListener) context;
        }else {
            throw new ClassCastException(context.toString() + " must implement " + "FragmentCickListener");
        }
    }


    private static class getAllSaveRankedAsync extends AsyncTask<Void, Void, List<User>>{
        private UserDao userDao;

        private getAllSaveRankedAsync(UserDao userDao){
            this.userDao = userDao;
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            return userDao.getAllUsersRanked();
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
        }
    }
}
