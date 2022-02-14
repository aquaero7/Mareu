package com.example.mareu.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application; // TODO : A supprimer
import android.app.DatePickerDialog;    // TODO : A supprimer
import android.app.TimePickerDialog;    // TODO : A supprimer
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;   // TODO : A supprimer
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;   // TODO : A supprimer
import android.widget.Toast;

import com.example.mareu.R;
import com.example.mareu.databinding.ActivityAddMeetingBinding;
import com.example.mareu.di.DI;
import com.example.mareu.events.CreateMeetingEvent;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.service.MeetingApiService;
import com.example.mareu.utils.MeetingToolbox;
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
    private static final int DEFAULT_MEETING_DURATION_IN_MIN = 45;  // TODO : A supprimer
    private List<String> participantsList = new ArrayList<>();
    private ArrayAdapter participantsListAdapter;
    private TextInputLayout nameLyt, dateLyt, startTimeLyt, endTimeLyt, roomLyt;
    private AutoCompleteTextView roomTextView;
    private TextInputLayout addParticipantLyt;
    private ImageButton addParticipantButton;
    private ListView participantsListView;
    private Button returnButton, createMeetingButton;
    private Date mDate, mTime;  // TODO : A supprimer
    private SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    private SimpleDateFormat dfDateLong = new SimpleDateFormat("dd MMMM yyyy", Locale.FRANCE);
    private SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm", Locale.FRANCE);
    // private Calendar now;    // TODO : A supprimer
    private boolean mailOK, fieldsOK, slotOK;
    private String nameText, dateText, dateTextLong, startText, endText, roomText;
    private String nameErrorText, dateErrorText, timeErrorText, endErrorText, roomErrorText;
    private boolean meetingCreated; // TODO : A supprimer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init UI --------------------------------------------------------------------------------
        initView();
        initListeners();
        // now = Calendar.getInstance();    // TODO : A supprimer
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
            try {
                // dateSetting();   // TODO : A supprimer
                MeetingToolbox.dateSetting(dateLyt, this);
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("Error " + e.getMessage());
            }
        }
        if (v == startTimeLyt.getEditText()) { startTimeLyt.setError("");
            try {
                // timeSetting(true, false);    // TODO : A supprimer
                MeetingToolbox.timeSetting(true, false, startTimeLyt, endTimeLyt, this);
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("Error " + e.getMessage());
            }
        }
        if (v == endTimeLyt.getEditText()) { endTimeLyt.setError("");
            try {
                // timeSetting(false, true);
                MeetingToolbox.timeSetting(false, true, startTimeLyt, endTimeLyt, this);
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("Error " + e.getMessage());
            }
        }
        if (v == roomLyt.getEditText()) { roomLyt.setError(""); }
        if (v == addParticipantLyt.getEditText()) { addParticipantLyt.setError(""); }
        if (v == addParticipantButton) { addParticipant(); }
        if (v == returnButton) { finish(); }
        if (v == createMeetingButton) {
            try {
                performCkecks();
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("Error " + e.getMessage());
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == nameLyt.getEditText()) { nameLyt.setError(""); }
        return false;
    }
    // Fin Init listeners -------------------------------------------------------------------------

    /* // // TODO : A supprimer -------------------------------------------------------------------
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
    */ // Fin A supprimer -------------------------------------------------------------------------

    /* // // TODO : A supprimer -------------------------------------------------------------------
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
    */ // Fin A supprimer -------------------------------------------------------------------------

    public void addParticipant() {
        String mailAddress = addParticipantLyt.getEditText().getText().toString();
        mailOK = MeetingToolbox.checkMail(mailAddress);

        if (mailOK) {
            participantsList.add(addParticipantLyt.getEditText().getText().toString().toLowerCase(Locale.ROOT));
            participantsListAdapter.notifyDataSetChanged();
            addParticipantLyt.getEditText().getText().clear();
        } else { addParticipantLyt.setError(getString(R.string.error_email)); }
    }

    public void performCkecks() throws ParseException {
        nameText = nameLyt.getEditText().getText().toString();
        nameErrorText = getString(R.string.error_meeting_name);
        dateTextLong = dateLyt.getEditText().getText().toString();
        dateErrorText = getString(R.string.error_meeting_date);
        startText = startTimeLyt.getEditText().getText().toString();
        endText = endTimeLyt.getEditText().getText().toString();
        timeErrorText = getString(R.string.error_meeting_time);
        endErrorText = getString(R.string.error_meeting_endtime);
        roomText = roomTextView.getText().toString();
        roomErrorText = getString(R.string.error_meeting_room);

        fieldsOK = MeetingToolbox.checkFields(nameLyt, nameText, nameErrorText, dateLyt, dateTextLong, dateErrorText,
                startTimeLyt, startText, endTimeLyt, endText, timeErrorText, endErrorText, roomLyt, roomText, roomErrorText);
        if (fieldsOK) {
            String selectedDate = dfDate.format(dfDateLong.parse(dateTextLong));
            String selectedStart = startTimeLyt.getEditText().getText().toString().replace("h", ":");
            String selectedEnd = endTimeLyt.getEditText().getText().toString().replace("h", ":");
            Room selectedRoom = mMeetingApiService.getRoomByName(roomLyt.getEditText().getText().toString().split(" ")[0]);
            String selectedErrorText = getString(R.string.error_slot);

            slotOK = MeetingToolbox.checkSlot(selectedDate, selectedStart, selectedEnd, selectedRoom, roomLyt, selectedErrorText);
            if (slotOK) {
                dateText = dfDate.format(dfDateLong.parse(dateTextLong));   // Mise au format de la date pour création meeting
                if (participantsList.isEmpty()) {
                    showAlertDialog();
                } else {
                    createMeeting();
                }
            }
        }
    }

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_alertDialog))
                .setMessage(getString(R.string.alertDialog_empty_participants_list))
                .setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            createMeeting();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            System.out.println("Error " + e.getMessage());
                        }
                    }
                })
                .setNegativeButton("NON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .create()
                .show();
    }

    public void createMeeting() throws ParseException {
        Meeting meetingToAdd = new Meeting(System.currentTimeMillis(),
                nameText,
                dateText,
                startText.replace("h", ":"),
                endText.replace("h", ":"),
                mMeetingApiService.getRoomByName(roomText.split(" ")[0]),
                participantsList);

        /* // Option C1 : Envoi de l'event après un délai nécessaire au réabonnement de ListMeetingActivity à l'EventBus
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() { EventBus.getDefault().post(new CreateMeetingEvent(meetingToAdd)); }
        }, 500);
        */ // Fin ---------------------------------------------------------------------------------

        // // Option C2 : Création dans cette activité --------------------------------------------
        mMeetingApiService.createMeeting(meetingToAdd);
        Toast.makeText(this, "La réunion \" " + nameText + "\" a été créée", Toast.LENGTH_SHORT).show();
        // // Fin ---------------------------------------------------------------------------------

        finish();
    }
}