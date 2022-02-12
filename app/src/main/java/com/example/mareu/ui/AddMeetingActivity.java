package com.example.mareu.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;

import com.example.mareu.R;
import com.example.mareu.databinding.ActivityAddMeetingBinding;
import com.example.mareu.di.DI;
import com.example.mareu.events.CreateMeetingEvent;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.ReservationSlot;
import com.example.mareu.model.Room;
import com.example.mareu.service.MeetingApiService;
import com.google.android.material.textfield.TextInputLayout;
import org.greenrobot.eventbus.EventBus;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddMeetingActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private ActivityAddMeetingBinding mActivityAddMeetingBinding;
    private MeetingApiService mMeetingApiService = DI.getMeetingApiService();
    private static final int DEFAULT_MEETING_DURATION_IN_MIN = 45;
    List<String> participantsList = new ArrayList<>();
    ArrayAdapter participantsListAdapter;
    private TextInputLayout nameLyt, dateLyt, startTimeLyt, endTimeLyt, roomLyt;
    private AutoCompleteTextView roomTextView;
    private TextInputLayout addParticipantLyt;
    private ImageButton addParticipantButton;
    private ListView participantsListView;
    private Button returnButton, createMeetingButton;
    private Date mDate, mTime;
    private SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    private SimpleDateFormat dfDateLong = new SimpleDateFormat("dd MMMM yyyy", Locale.FRANCE);
    private SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm", Locale.FRANCE);
    private Calendar now;
    private Boolean mailOK, fieldsOK, slotOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init UI --------------------------------------------------------------------------------
        initView();
        initListeners();
        now = Calendar.getInstance();
        // Fin init UI ----------------------------------------------------------------------------
    }

    // Méthodes -----------------------------------------------------------------------------------

    // Init View & binding ------------------------------------------------------------------------
    private void initView() {
        // Init view
        mActivityAddMeetingBinding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        View view = mActivityAddMeetingBinding.getRoot();
        setContentView(view);

        // Binding
        nameLyt = mActivityAddMeetingBinding.nameMeetingLayout;
        dateLyt = mActivityAddMeetingBinding.dateMeetingLayout;
        startTimeLyt = mActivityAddMeetingBinding.startTimeMeetingLayout;
        endTimeLyt = mActivityAddMeetingBinding.endTimeMeetingLayout;
        roomLyt = mActivityAddMeetingBinding.roomMeetingLayout;
        roomTextView = mActivityAddMeetingBinding.roomMeetingTextView;
        addParticipantLyt = mActivityAddMeetingBinding.addParticipantLayout;
        addParticipantButton = mActivityAddMeetingBinding.buttonAddParticipant;
        participantsListView = mActivityAddMeetingBinding.participantsListView;
        returnButton = mActivityAddMeetingBinding.buttonReturn;
        createMeetingButton = mActivityAddMeetingBinding.buttonCreateMeeting;

        // Init roomsList
        CharSequence[] roomsList = mMeetingApiService.getRoomsList();
        ArrayAdapter roomDropdownAdapter = new ArrayAdapter(this, R.layout.list_item, roomsList);
        roomTextView.setAdapter(roomDropdownAdapter);

        // Init participantsList
        participantsListAdapter = new ArrayAdapter(this, R.layout.list_item, participantsList);
        participantsListView.setAdapter(participantsListAdapter);
    }
    // Fin Init View & binding --------------------------------------------------------------------

    // Init listeners -----------------------------------------------------------------------------
    private void initListeners() {
        nameLyt.getEditText().setOnTouchListener(this);
        dateLyt.getEditText().setOnClickListener(this);
        startTimeLyt.getEditText().setOnClickListener(this);
        endTimeLyt.getEditText().setOnClickListener(this);
        roomLyt.getEditText().setOnClickListener(this);
        addParticipantLyt.getEditText().setOnClickListener(this);
        addParticipantButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        createMeetingButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == dateLyt.getEditText()) { dateLyt.setError("");
            try { dateSetting(); }
            catch (ParseException e) { e.printStackTrace(); System.out.println("Error " + e.getMessage()); } }
        if (v == startTimeLyt.getEditText()) { startTimeLyt.setError("");
            try { timeSetting(true, false); }
            catch (ParseException e) { e.printStackTrace(); System.out.println("Error " + e.getMessage()); } }
        if (v == endTimeLyt.getEditText()) { endTimeLyt.setError("");
            try { timeSetting(false, true); }
            catch (ParseException e) { e.printStackTrace(); System.out.println("Error " + e.getMessage()); } }
        if (v == roomLyt.getEditText()) { roomLyt.setError(""); }
        if (v == addParticipantLyt.getEditText()) { addParticipantLyt.setError(""); }
        if (v == addParticipantButton) { addParticipant(); }
        if (v == returnButton) { finish(); }
        if (v == createMeetingButton) {
            try { performCkecks(); }
            catch (ParseException e) { e.printStackTrace(); System.out.println("Error " + e.getMessage()); } }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == nameLyt.getEditText()) { nameLyt.setError(""); }
        return false;
    }
    // Fin Init listeners -------------------------------------------------------------------------

    public void dateSetting() throws ParseException {
        // Set DatePickerDialog (date listener)
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calD = Calendar.getInstance();
                calD.set(Calendar.YEAR, year);
                calD.set(Calendar.MONTH, month);
                calD.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mDate = calD.getTime();
                dateLyt.getEditText().setText(dfDateLong.format(mDate));
            }
        };

        // Create DatePickerDialog
        int selectedYear = now.get(Calendar.YEAR);
        int selectedMonth = now.get(Calendar.MONTH);
        int selectedDayOfMonth = now.get(Calendar.DAY_OF_MONTH);

        // Le picker affiche la date déja définie si le champ n'est pas vide, sinon la date du jour
        if (!dateLyt.getEditText().getText().toString().isEmpty()) {
            Date oldDate = dfDateLong.parse(dateLyt.getEditText().getText().toString());
            Calendar oldCalDate = Calendar.getInstance();
            oldCalDate.setTime(oldDate);
            selectedYear = oldCalDate.get(Calendar.YEAR);
            selectedMonth = oldCalDate.get(Calendar.MONTH);
            selectedDayOfMonth = oldCalDate.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);

        // Show DatePickerDialog
        datePickerDialog.show();
    }

    public void timeSetting(Boolean start, Boolean end) throws ParseException {
        // Set TimePickerDialog (time listener)
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calH = Calendar.getInstance();
                calH.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calH.set(Calendar.MINUTE, minute);
                mTime = calH.getTime();
                if (start) { startTimeLyt.getEditText().setText(dfTime.format(mTime).replace(":", "h"));
                    // Preset end time calculated with default duration
                    if (endTimeLyt.getEditText().getText().toString().isEmpty()) {
                        Calendar calH2 = Calendar.getInstance();
                        calH2.setTime(mTime);
                        calH2.add(Calendar.MINUTE,  DEFAULT_MEETING_DURATION_IN_MIN);
                        Date mTime2 = calH2.getTime();
                        endTimeLyt.getEditText().setText(dfTime.format(mTime2).replace(":", "h"));
                    }
                }
                if (end) endTimeLyt.getEditText().setText(dfTime.format(mTime).replace(":", "h"));
            }
        };

        // Create TimePickerDialog
        int selectedHourOfDay = now.get(Calendar.HOUR_OF_DAY);
        int selectedMinute = now.get(Calendar.MINUTE);

        // Le picker affiche l'heure déja définie si le champ n'est pas vide, sinon l'heure courante
        if (start && !startTimeLyt.getEditText().getText().toString().isEmpty()) {
            Date oldTime = dfTime.parse(startTimeLyt.getEditText().getText().toString().replace("h", ":"));
            Calendar oldCalTime = Calendar.getInstance();
            oldCalTime.setTime(oldTime);
            selectedHourOfDay = oldCalTime.get(Calendar.HOUR_OF_DAY);
            selectedMinute = oldCalTime.get(Calendar.MINUTE);
        } else if (end && !endTimeLyt.getEditText().getText().toString().isEmpty()) {
            Date oldTime = dfTime.parse(endTimeLyt.getEditText().getText().toString().replace("h", ":"));
            Calendar oldCalTime = Calendar.getInstance();
            oldCalTime.setTime(oldTime);
            selectedHourOfDay = oldCalTime.get(Calendar.HOUR_OF_DAY);
            selectedMinute = oldCalTime.get(Calendar.MINUTE);
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                timeSetListener, selectedHourOfDay, selectedMinute, true);

        // Show DatePickerDialog
        timePickerDialog.show();
    }

    public void addParticipant() {
        checkMail();
        if (mailOK) {
            participantsList.add(addParticipantLyt.getEditText().getText().toString().toLowerCase(Locale.ROOT));
            participantsListAdapter.notifyDataSetChanged();
            addParticipantLyt.getEditText().getText().clear();
        } else { addParticipantLyt.setError(getString(R.string.error_email)); }
    }

    public void performCkecks() throws ParseException {
        checkFields();
        if (fieldsOK) {
            checkSlot();
            if (slotOK) {
                if (participantsList.isEmpty()) { showAlertDialog(); }
                else { createMeeting(); }
            }
        }
    }

    public void checkMail() {
        mailOK = true;
        String mailAddress = addParticipantLyt.getEditText().getText().toString();
        int occurAt = 0;
        for (int i = 0; i < mailAddress.length(); i++) {
            if (mailAddress.charAt(i) == '@') { occurAt ++; }
        }

        if (mailAddress.indexOf("@") < 1 || occurAt != 1
                || mailAddress.charAt(mailAddress.length()-3) == '@'
                || mailAddress.charAt(mailAddress.length()-2) == '@'
                || mailAddress.charAt(mailAddress.length()-1) == '@'
                || mailAddress.indexOf(".") < 1 || mailAddress.charAt(mailAddress.length()-1) == '.'
        ) { mailOK = false; }
    }

    public void checkFields() {
        fieldsOK = true;
        if (nameLyt.getEditText().getText().toString().isEmpty()) {
            nameLyt.setError(getString(R.string.error_meeting_name)); fieldsOK = false;
        }
        if (dateLyt.getEditText().getText().toString().isEmpty()) {
            dateLyt.setError(getString(R.string.error_meeting_date)); fieldsOK = false;
        }
        if (startTimeLyt.getEditText().getText().toString().isEmpty()) {
            startTimeLyt.setError(getString(R.string.error_meeting_time)); fieldsOK = false;
        }
        if (endTimeLyt.getEditText().getText().toString().isEmpty()) {
            endTimeLyt.setError(getString(R.string.error_meeting_time)); fieldsOK = false;
        }
        if (roomTextView.getText().toString().isEmpty()) {
            roomLyt.setError(getString(R.string.error_meeting_room)); fieldsOK = false;
        }
        if (!startTimeLyt.getEditText().getText().toString().isEmpty()
                && !endTimeLyt.getEditText().getText().toString().isEmpty()
                && (endTimeLyt.getEditText().getText().toString())
                    .compareTo(startTimeLyt.getEditText().getText().toString()) <= 0) {
            endTimeLyt.setError(getString(R.string.error_meeting_endtime)); fieldsOK = false;
        }
    }

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_alertDialog))
                .setMessage(getString(R.string.alertDialog_empty_participants_list))
                .setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try { createMeeting(); }
                        catch (ParseException e) { e.printStackTrace(); }
                    }
                })
                .setNegativeButton("NON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .create()
                .show();
    }

    public void checkSlot() throws ParseException {
        slotOK = true;
        String selectedDate = dfDate.format(dfDateLong.parse(dateLyt.getEditText().getText().toString()));
        String selectedStart = startTimeLyt.getEditText().getText().toString().replace("h", ":");
        String selectedEnd = endTimeLyt.getEditText().getText().toString().replace("h", ":");
        Room selectedRoom = mMeetingApiService.getRoomByName(roomLyt.getEditText().getText().toString().split(" ")[0]);

        for (ReservationSlot slot : selectedRoom.getReservationSlots()) {
            if (selectedDate.equals(slot.getDate())) {
                if ((selectedEnd.compareTo(slot.getStart()) >= 0) && (selectedStart.compareTo(slot.getEnd()) <= 0)) {
                    slotOK = false;
                    roomLyt.setError(getString(R.string.error_slot));
                }
            }
        }
    }

    public void createMeeting() throws ParseException {
        Meeting meetingToAdd = new Meeting(System.currentTimeMillis(),
                nameLyt.getEditText().getText().toString(),
                dfDate.format(dfDateLong.parse(dateLyt.getEditText().getText().toString())),
                startTimeLyt.getEditText().getText().toString().replace("h", ":"),
                endTimeLyt.getEditText().getText().toString().replace("h", ":"),
                mMeetingApiService.getRoomByName(roomLyt.getEditText().getText().toString().split(" ")[0]),
                participantsList);

        // Envoi de l'event après un délai nécessaire au réabonnement de ListMeetingActivity à l'EventBus
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() { EventBus.getDefault().post(new CreateMeetingEvent(meetingToAdd)); }
        }, 500);

        finish();
    }

}