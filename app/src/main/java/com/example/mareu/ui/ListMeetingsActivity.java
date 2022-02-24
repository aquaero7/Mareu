package com.example.mareu.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.mareu.R;
import com.example.mareu.databinding.ActivityListMeetingsBinding;
import com.example.mareu.di.DI;
import com.example.mareu.events.CreateMeetingEvent;
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
import java.util.Locale;

public class ListMeetingsActivity extends AppCompatActivity {

    private ActivityListMeetingsBinding mListMeetingsBinding;
    private List<Meeting> mMeetings;
    private final MeetingApiService mMeetingApiService = DI.getMeetingApiService();
    private Date mDate;         // Date du filtre
    private Room mRoom = null;  // Salle du filtre (aucune par défaut)
    private boolean filterIsSetOnDate = true, filterIsSetOnRoom = false;
    private final SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    private final SimpleDateFormat dfDateLong = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.FRANCE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Init UI --------------------------------------------------------------------------------
        initView();
        mDate = new Date(); // Date du jour
        setUpFilterStatus(true, false);
        initList();
        createRecyclerView();
        initRecyclerView();
        setUpAddMeetingButton();
        // Fin Init UI ----------------------------------------------------------------------------
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
        // Binding & init View --------------------------------------------------------------------
        mListMeetingsBinding = ActivityListMeetingsBinding.inflate(getLayoutInflater());
        View view = mListMeetingsBinding.getRoot();
        setContentView(view);
    }

    private void initList() {
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

    private void createRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mListMeetingsBinding.recyclerView.setLayoutManager(layoutManager);
    }

    private void initRecyclerView() {
        MyMeetingRecyclerViewAdapter myMeetingRecyclerViewAdapter = new MyMeetingRecyclerViewAdapter(mMeetings);
        mListMeetingsBinding.recyclerView.setAdapter(myMeetingRecyclerViewAdapter);
        // boundItems(layoutManager);  // Séparation des items de la liste
    }
    // Fin Méthodes init UI -----------------------------------------------------------------------


    // Séparation des items de la liste -----------------------------------------------------------
    private void boundItems(LinearLayoutManager layoutManager) {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mListMeetingsBinding.recyclerView
                .getContext(), layoutManager.getOrientation());
        mListMeetingsBinding.recyclerView.addItemDecoration(dividerItemDecoration);
    }

    // Accrochage du menu à la vue ----------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // Actions selon sélection dans menu ----------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter_date) {
            dateDialog();
            return true;
        } else if (item.getItemId() == R.id.filter_place) {
            meetingDialog();
            return true;
        } else if (item.getItemId() == R.id.filters_reset) {
            resetFilters();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    private void resetFilters() {
        mMeetings.clear();
        mMeetings.addAll(mMeetingApiService.getMeetings());
        if (mListMeetingsBinding.recyclerView.getAdapter() != null) {
            setUpFilterStatus(false, false);
            mListMeetingsBinding.recyclerView.getAdapter().notifyDataSetChanged();
        }

    }

    private void meetingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.hint_meeting_room));
        builder.setItems(mMeetingApiService.getRoomsItems(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMeetings.clear();
                mRoom = mMeetingApiService.getRooms().get(which);
                mMeetings.addAll(mMeetingApiService.getMeetingsByPlace(mRoom));
                if (mListMeetingsBinding.recyclerView.getAdapter() != null) {
                    setUpFilterStatus(false, true);
                    mListMeetingsBinding.recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });
        builder.show();
    }

    private void dateDialog() {
        // Set DatePickerDialog (date listener)
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mDate = cal.getTime();
                mMeetings.clear();
                mMeetings.addAll(mMeetingApiService.getMeetingsByDate(dfDate.format(mDate)));
                if (mListMeetingsBinding.recyclerView.getAdapter() != null) {
                    setUpFilterStatus(true, false);
                    mListMeetingsBinding.recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        };

        // Create DatePickerDialog
        Calendar now = Calendar.getInstance();
        int selectedYear = now.get(Calendar.YEAR);
        int selectedMonth = now.get(Calendar.MONTH);
        int selectedDayOfMonth = now.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);

        // Show DatePickerDialog
        datePickerDialog.show();
    }

    private void setUpFilterStatus(Boolean byDate, Boolean byRoom) {
        if (byDate && !byRoom) {
            filterIsSetOnDate = true;
            filterIsSetOnRoom = false;
            mListMeetingsBinding.header.setText(getString(R.string.header_meetings_list_filtered_by_date) + dfDateLong.format(mDate));
        }
        else if (!byDate && byRoom) {
            filterIsSetOnDate = false;
            filterIsSetOnRoom = true;
            mListMeetingsBinding.header.setText(getString(R.string.header_meetings_list_filtered_by_place) + mRoom.getName());
        }
        else if (byDate && byRoom) {
            filterIsSetOnDate = true;
            filterIsSetOnRoom = true;
            // Non implémenté
        }
        else {
            filterIsSetOnDate = false;
            filterIsSetOnRoom = false;
            mListMeetingsBinding.header.setText(getString(R.string.header_meetings_list));
        }
    }
    // Fin Actions selon sélection dans menu ------------------------------------------------------

    // Abonnement aux events de l'EventBus --------------------------------------------------------
    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        try {
            mMeetingApiService.deleteMeeting(event.meeting);
            initList();
            initRecyclerView();
            Toast.makeText(this, "La réunion \"" + event.meeting.getName() + "\" a été supprimée !", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception in " + getClass() + " / " + this + " : " + e.getMessage());
            Toast.makeText(this, getString(R.string.exception_deleteMeeting) + " \"" + event.meeting.getName() + "\"", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Fired if the user create a meeting from AddMeetingActivity
     * @param event
     */
    @Subscribe
    public void onCreateMeeting(CreateMeetingEvent event) {
        try {
            mMeetingApiService.createMeeting(event.meeting);
            initList();
            initRecyclerView();
            Toast.makeText(this, "La réunion \"" + event.meeting.getName() + "\" a été créée !", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception in " + getClass() + " / " + this + " : " + e.getMessage());
            Toast.makeText(this, getString(R.string.exception_createMeeting) + " \"" + event.meeting.getName() + "\"", Toast.LENGTH_SHORT).show();
        }
    }
    // Fin Abonnement aux events de l'EventBus ----------------------------------------------------

    private void setUpAddMeetingButton() {
        mListMeetingsBinding.addMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListMeetingsActivity.this, AddMeetingActivity.class));
            }
        });
    }
}