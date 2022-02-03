package com.example.mareu.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.example.mareu.R;
import com.example.mareu.databinding.ActivityListMeetingsBinding;
import com.example.mareu.di.DI;
import com.example.mareu.events.DeleteMeetingEvent;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.service.MeetingApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListMeetingsActivity extends AppCompatActivity {

    // Initialisation des variables ---------------------------------------------------------------
    private ActivityListMeetingsBinding mListMeetingsBinding;
    private List<Meeting> mMeetings;
    private MeetingApiService mMeetingApiService = DI.getMeetingApiService();
    private Date mDate = new Date();    // Date du jour
    private Room mRoom;
    private Boolean filterIsSetOnDate = true;
    private Boolean filterIsSetOnRoom = false;
    private SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
    private Calendar now = Calendar.getInstance();
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
        // initDummyList();    // A la place de initList() pour la non persistance de la liste lors de la rotation du device
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
        /* TODO : A supprimer
        mMeetings = mMeetingApiService.getMeetings();
        mMeetings = new ArrayList<>(mMeetingApiService.getMeetings());
        */

        if (filterIsSetOnDate && ! filterIsSetOnRoom) {
            mMeetings = new ArrayList<>(mMeetingApiService.getMeetingsByDate(dfDate.format(mDate)));
        }
        else if (! filterIsSetOnDate && filterIsSetOnRoom) {
            mMeetings = new ArrayList<>(mMeetingApiService.getMeetingsByPlace(mRoom));
        }
        else if (filterIsSetOnDate && filterIsSetOnRoom) {
            mMeetings = new ArrayList<>(mMeetingApiService.getMeetings());  // Cas non implémenté
        }
        else {
            mMeetings = new ArrayList<>(mMeetingApiService.getMeetings());
        }


    }

    private void initDummyList() {
        // Pour la non persistance de la liste lors de la rotation du device

        /* // Test 1
        mMeetings = mMeetingApiService.getDummyMeetings();
        */

        /* // Test 2
        if (mMeetings != null) {
            mMeetings.clear();
            mMeetings.addAll(mMeetingApiService.getDummyMeetings());
        };
        */

        // // Test 3
        mMeetings = new ArrayList<>(mMeetingApiService.getDummyMeetings());
        mMeetings.clear();
        mMeetings.addAll(mMeetingApiService.getMeetingsByDate(dfDate.format(new Date())));
        //
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
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mListMeetingsBinding.recyclerView
                .getContext(), layoutManager.getOrientation());
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

        filterIsSetOnDate = false;
        filterIsSetOnRoom = false;
        mMeetings.clear();
        mMeetings.addAll(mMeetingApiService.getMeetings());

        if (mListMeetingsBinding.recyclerView.getAdapter() != null) {
            mListMeetingsBinding.recyclerView.getAdapter().notifyDataSetChanged();
        }

    }

    private void meetingDialog() {
        // TODO

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sélectionnez une salle...");
        builder.setItems(mMeetingApiService.getRoomsList(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO
                filterIsSetOnDate = false;
                filterIsSetOnRoom = true;
                mMeetings.clear();
                mRoom = mMeetingApiService.getRooms().get(which);
                mMeetings.addAll(mMeetingApiService.getMeetingsByPlace(mRoom));
                if (mListMeetingsBinding.recyclerView.getAdapter() != null) {
                    mListMeetingsBinding.recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });
        builder.show();
    }

    private void dateDialog() {

        int selectedYear = now.get(Calendar.YEAR);
        int selectedMonth = now.get(Calendar.MONTH);
        int selectedDayOfMonth = now.get(Calendar.DAY_OF_MONTH);

        // Set DatePickerDialog (date listener)
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, dayOfMonth);
                mDate = cal.getTime();
                filterIsSetOnDate = true;
                filterIsSetOnRoom = false;
                mMeetings.clear();
                mMeetings.addAll(mMeetingApiService.getMeetingsByDate(dfDate.format(mDate)));
                if (mListMeetingsBinding.recyclerView.getAdapter() != null) {
                    mListMeetingsBinding.recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        };

        // Create DatePickerDialog (Spinner mode)
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);

        // Show DatePickerDialog
        datePickerDialog.show();

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