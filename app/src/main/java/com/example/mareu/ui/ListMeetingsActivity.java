package com.example.mareu.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
// // Pour accrochage du menu à la vue ------------------------------------------------------------
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.mareu.R;
import com.example.mareu.databinding.ActivityListMeetingsBinding;
import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.MeetingApiService;

import java.util.List;
// // Fin -----------------------------------------------------------------------------------------

public class ListMeetingsActivity extends AppCompatActivity {

    // // Initialisation des variables ------------------------------------------------------------
    private ActivityListMeetingsBinding mListMeetingsBinding;
    private List<Meeting> mMeetings;
    private MeetingApiService mMeetingApiService = DI.getMeetingApiService();
    // // Fin -------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // // Init UI -----------------------------------------------------------------------------
        initView();
        initList();
        initRecyclerView();
        // // Fin ---------------------------------------------------------------------------------
    }


    // // -----------------------------------------------------------------------------------------
    private void initView() {
        /* // Init View initiale ------------------------------------------------------------------
        setContentView(R.layout.activity_list_meetings);
        */ // Fin ---------------------------------------------------------------------------------

        // // Binding & init View -----------------------------------------------------------------
        mListMeetingsBinding = ActivityListMeetingsBinding.inflate(getLayoutInflater());
        View view = mListMeetingsBinding.getRoot();
        setContentView(view);
        // // Fin ---------------------------------------------------------------------------------
    }

    private void initList() {
        mMeetings = mMeetingApiService.getMeetings();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mListMeetingsBinding.Recyclerview.setLayoutManager(layoutManager);

        MyMeetingRecyclerViewAdapter myMeetingRecyclerViewAdapter = new MyMeetingRecyclerViewAdapter(mMeetings);
        mListMeetingsBinding.Recyclerview.setAdapter(myMeetingRecyclerViewAdapter);
    }
    // // Fin -------------------------------------------------------------------------------------

    // // Accrochage du menu à la vue -------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    // // Fin -------------------------------------------------------------------------------------

    // // Actions selon sélection dans menu --------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_date:
                dateDialog();
                return true;
            case R.id.filter_place:
                meetingDialog();
                return true;
            case R.id.filters_reset:
                resetfilters();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void resetfilters() {
        // TODO
    }

    private void meetingDialog() {
        // TODO
    }

    private void dateDialog() {
        // TODO
    }
    // // Fin -------------------------------------------------------------------------------------

}