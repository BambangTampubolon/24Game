package com.example.beng.newandroidproject.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.beng.newandroidproject.Adapter.ListUserAdapter;
import com.example.beng.newandroidproject.Interface.FragmentPlayerInterface;
import com.example.beng.newandroidproject.Interface.SpinnerInterface;
import com.example.beng.newandroidproject.R;
import com.example.beng.newandroidproject.User;

import java.util.ArrayList;
import java.util.List;

public class FragmentSettingPlayer extends Fragment implements SpinnerInterface{
    public static final String EXTRA_TEXT ="text";
    private List<User> listUserSelected;
    private ListUserAdapter adapterListUser;
    private ListView listViewUser;
    private Spinner spinner;
    private FloatingActionButton buttonNext;
    ArrayAdapter<CharSequence> adapterSpinner;
    FragmentPlayerInterface fragmentPlayerInterface;


    public List<User> getListUserSelected() {
        return listUserSelected;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_player, container, false);
        spinner = view.findViewById(R.id.spinner_user_option);
        listViewUser = view.findViewById(R.id.list_user_view);
        buttonNext = view.findViewById(R.id.done_button);
        Log.i("checkheadless", "onCreateView: " + getArguments());
        if(null != savedInstanceState){
            Log.i("checkheadless", "onCreateView: " + savedInstanceState.toString());
        }
        adapterSpinner = ArrayAdapter.createFromResource(getContext(), R.array.list_spinner,
                android.R.layout.simple_spinner_item);
        listUserSelected = new ArrayList<>();
        adapterListUser = new ListUserAdapter(getContext(), listUserSelected, this);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapterSpinner);
        listViewUser.setAdapter(adapterListUser);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendListUser();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentPlayerInterface = (FragmentPlayerInterface) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void SpinnerUserItemClick(View v, int position, String userName, boolean isLocked) {
        listUserSelected.get(position).setNama(userName);
        listUserSelected.get(position).setLocked(!isLocked);
        adapterListUser.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void sendListUser(){
        Log.i("checklistuser", "sendListUser: " + listUserSelected.size());
        List<User> returnedUser = new ArrayList<>();
        for(User a: listUserSelected){
            returnedUser.add(a);
        }
        fragmentPlayerInterface.sentDataToActivity(getListUserSelected());
    }
}
