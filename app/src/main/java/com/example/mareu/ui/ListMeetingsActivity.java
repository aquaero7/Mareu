package com.example.mareu.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.mareu.R;
import com.example.mareu.databinding.ActivityListMeetingsBinding;
import com.example.mareu.di.DI;
import com.example.mareu.events.DeleteMeetingEvent;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.MeetingApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class ListMeetingsActivity extends AppCompatActivity {

    // Initialisation des variables ---------------------------------------------------------------
    private ActivityListMeetingsBinding mListMeetingsBinding;
    private List<Meeting> mMeetings;
    private MeetingApiService mMeetingApiService = DI.getMeetingApiService();
    // Fin ----------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init UI --------------------------------------------------------------------------------
        initView();
        initList();
        createRecyclerView();
        initRecyclerView();
        // Fin ------------------------------------------------------------------------------------
    }

    @Override
    protected void onResume() {
        super.onResume();
        initList();
        initRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    // Méthodes init UI ---------------------------------------------------------------------------
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

    private void createRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mListMeetingsBinding.recyclerView.setLayoutManager(layoutManager);
    }

    private void initRecyclerView() {
        MyMeetingRecyclerViewAdapter myMeetingRecyclerViewAdapter = new MyMeetingRecyclerViewAdapter(mMeetings);
        mListMeetingsBinding.recyclerView.setAdapter(myMeetingRecyclerViewAdapter);

        // boundItems(layoutManager);  // Séparation des items de la liste
    }
    // Fin ----------------------------------------------------------------------------------------


    // Séparation des items de la liste -----------------------------------------------------------
    private void boundItems(LinearLayoutManager layoutManager) {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mListMeetingsBinding.recyclerView.getContext(), layoutManager.getOrientation());
        mListMeetingsBinding.recyclerView.addItemDecoration(dividerItemDecoration);
    }
    // Fin ----------------------------------------------------------------------------------------


    // Accrochage du menu à la vue ----------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    // Fin ----------------------------------------------------------------------------------------

    // Actions selon sélection dans menu ----------------------------------------------------------
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
    // Fin ----------------------------------------------------------------------------------------

    // Abonnement aux events de l'EventBus --------------------------------------------------------
    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        mMeetingApiService.deleteMeeting(event.meeting);
        initList();
        initRecyclerView();
    }
    // Fin ----------------------------------------------------------------------------------------

}