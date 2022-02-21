package com.example.mareu.ui;

import static com.example.mareu.utils.MeetingToolbox.checkFields;
import static com.example.mareu.utils.MeetingToolbox.checkMail;
import static com.example.mareu.utils.MeetingToolbox.checkSlot;
import static com.example.mareu.utils.MeetingToolbox.clearErrorFields;
import static com.example.mareu.utils.MeetingToolbox.dateSetting;
import static com.example.mareu.utils.MeetingToolbox.timeSetting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mareu.R;
import com.example.mareu.databinding.ActivityAddMeetingBinding;
import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.service.MeetingApiService;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddMeetingActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, View.OnFocusChangeListener {

    private ArrayAdapter participantsListAdapter;
    private final MeetingApiService mMeetingApiService = DI.getMeetingApiService();
    private final List<String> participantsList = new ArrayList<>();
    private final SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    private final SimpleDateFormat dfDateLong = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.FRANCE);
    private TextInputLayout nameLyt, dateLyt, startTimeLyt, endTimeLyt, roomLyt;
    private AutoCompleteTextView roomTextView;
    private TextInputLayout addParticipantLyt;
    private ImageButton addParticipantButton;
    private Button returnButton, createMeetingButton;
    private String selectedDate = null, selectedStart, selectedEnd, nameText;
    private Room selectedRoom;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init UI --------------------------------------------------------------------------------
        initView();
        initListeners();
        // Fin init UI ----------------------------------------------------------------------------
    }

    // Méthodes -----------------------------------------------------------------------------------

    // Init View & binding ------------------------------------------------------------------------
    private void initView() {
        // Init view
        ActivityAddMeetingBinding mActivityAddMeetingBinding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        View view = mActivityAddMeetingBinding.getRoot();
        setContentView(view);

        // Binding
        ListView participantsListView = mActivityAddMeetingBinding.participantsListView;
        nameLyt = mActivityAddMeetingBinding.nameMeetingLayout;
        dateLyt = mActivityAddMeetingBinding.dateMeetingLayout;
        startTimeLyt = mActivityAddMeetingBinding.startTimeMeetingLayout;
        endTimeLyt = mActivityAddMeetingBinding.endTimeMeetingLayout;
        roomLyt = mActivityAddMeetingBinding.roomMeetingLayout;
        roomTextView = mActivityAddMeetingBinding.roomMeetingTextView;
        addParticipantLyt = mActivityAddMeetingBinding.addParticipantLayout;
        addParticipantButton = mActivityAddMeetingBinding.buttonAddParticipant;
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
        nameLyt.getEditText().setOnFocusChangeListener(this);
        Objects.requireNonNull(dateLyt.getEditText()).setOnClickListener(this);
        Objects.requireNonNull(startTimeLyt.getEditText()).setOnClickListener(this);
        Objects.requireNonNull(endTimeLyt.getEditText()).setOnClickListener(this);
        Objects.requireNonNull(roomLyt.getEditText()).setOnClickListener(this);
        Objects.requireNonNull(addParticipantLyt.getEditText()).setOnClickListener(this);
        addParticipantLyt.getEditText().setOnFocusChangeListener(this);
        addParticipantButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        createMeetingButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == dateLyt.getEditText()) { dateSetting(dateLyt, this); }
        if (v == startTimeLyt.getEditText()) { timeSetting(true, false, startTimeLyt, endTimeLyt, this); }
        if (v == endTimeLyt.getEditText()) { timeSetting(false, true, startTimeLyt, endTimeLyt, this); }
        if (v == roomLyt.getEditText()) { roomLyt.setError(""); }
        if (v == addParticipantLyt.getEditText()) { addParticipantLyt.setError(""); }
        if (v == addParticipantButton) { addParticipant(); }
        if (v == returnButton) { finish(); }
        if (v == createMeetingButton) { performCkecks(); }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == nameLyt.getEditText()) { nameLyt.setError(""); }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == nameLyt.getEditText() && !hasFocus) { hideVirtualKeyboard(nameLyt); }
        if (v == addParticipantLyt.getEditText() && !hasFocus) { hideVirtualKeyboard(addParticipantLyt); }
    }
    // Fin Init listeners -------------------------------------------------------------------------

    public void addParticipant() {
        String mailAddress = Objects.requireNonNull(addParticipantLyt.getEditText()).getText().toString();
        boolean mailOK = checkMail(mailAddress);

        addParticipantLyt.setError("");

        if (mailOK) {
            hideVirtualKeyboard(addParticipantButton);
            participantsList.add(addParticipantLyt.getEditText().getText().toString().toLowerCase(Locale.ROOT));
            participantsListAdapter.notifyDataSetChanged();
            addParticipantLyt.getEditText().getText().clear();
        } else {
            addParticipantLyt.setError(getString(R.string.error_email));
        }
    }

    public void performCkecks() {
        nameText = Objects.requireNonNull(nameLyt.getEditText()).getText().toString();
        String dateTextLong = Objects.requireNonNull(dateLyt.getEditText()).getText().toString();
        String startText = Objects.requireNonNull(startTimeLyt.getEditText()).getText().toString();
        String endText = Objects.requireNonNull(endTimeLyt.getEditText()).getText().toString();
        String roomText = roomTextView.getText().toString();

        String nameErrorText = getString(R.string.error_meeting_name);
        String dateErrorText = getString(R.string.error_meeting_date);
        String timeErrorText = getString(R.string.error_meeting_time);
        String endErrorText = getString(R.string.error_meeting_endTime);
        String roomErrorText = getString(R.string.error_meeting_room);
        String selectedErrorText = getString(R.string.error_slot);

        clearErrorFields(nameLyt, dateLyt, startTimeLyt, endTimeLyt, roomLyt, addParticipantLyt);

        boolean fieldsOK = checkFields(nameLyt, nameText, nameErrorText, dateLyt, dateTextLong, dateErrorText,
                startTimeLyt, startText, endTimeLyt, endText, timeErrorText, endErrorText, roomLyt, roomText, roomErrorText);
        if (fieldsOK) {
            try {
                selectedDate = dfDate.format(Objects.requireNonNull(dfDateLong.parse(dateTextLong)));   // Mise au format de la date pour création meeting
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("ParseException in " + getClass() + " / " + this + " : " + e.getMessage());
                Toast.makeText(this, getString(R.string.exception_format_date), Toast.LENGTH_SHORT).show();
            }
            selectedStart = startText.replace("h", ":");
            selectedEnd = endText.replace("h", ":");
            selectedRoom = mMeetingApiService.getRoomByName(roomText.split(" ")[0]);

            boolean slotOK = checkSlot(selectedDate, selectedStart, selectedEnd, selectedRoom, roomLyt, selectedErrorText);
            if (slotOK) {
                if (participantsList.isEmpty()) {
                    showAlertDialog();
                } else {
                    scheduleMeeting();
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
                        scheduleMeeting();
                    }
                })
                .setNegativeButton("NON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .create()
                .show();
    }

    public void hideVirtualKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    public void scheduleMeeting() {
        Meeting meetingToSchedule = new Meeting(System.currentTimeMillis(),
                nameText, selectedDate, selectedStart, selectedEnd, selectedRoom, participantsList);

        /* // Option C1 : Envoi de l'event après un délai nécessaire au réabonnement de ListMeetingActivity à l'EventBus
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new CreateMeetingEvent(meetingToSchedule));
            }
        }, 500);
        */ // Fin ---------------------------------------------------------------------------------

        // // Option C2 : Création dans cette activité --------------------------------------------
        mMeetingApiService.createMeeting(meetingToSchedule);
        Toast.makeText(this, "La réunion \"" + nameText + "\" a été créée !", Toast.LENGTH_SHORT).show();
        // // Fin ---------------------------------------------------------------------------------

        finish();
    }

}