package com.example.mareu.utils;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mareu.R;
import com.example.mareu.model.ReservationSlot;
import com.example.mareu.model.Room;
import com.example.mareu.ui.AddMeetingActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MeetingToolbox extends Application {

    public static final int DEFAULT_MEETING_DURATION_IN_MIN = 45;
    private static SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    private static SimpleDateFormat dfDateLong = new SimpleDateFormat("dd MMMM yyyy", Locale.FRANCE);
    private static SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm", Locale.FRANCE);
    private static Date mDate, mTime;

    private static Calendar now = Calendar.getInstance();


    public static boolean checkSlot(String mDate, String mStart, String mEnd, Room mRoom, TextInputLayout mLayout, String mErrorText) {

        boolean slotOK = true;
        for (ReservationSlot slot : mRoom.getReservationSlots()) {
            if (mDate.equals(slot.getDate())) {
                if ((mEnd.compareTo(slot.getStart()) >= 0) && (mStart.compareTo(slot.getEnd()) <= 0)) {
                    slotOK = false;
                    mLayout.setError(mErrorText);
                }
            }
        }
        return slotOK;
    }


    public static boolean checkFields(TextInputLayout mNameLayout, String mNameText, String mNameErrorText,
                                      TextInputLayout mDateLayout, String mDateText, String mDateErrorText,
                                      TextInputLayout mStartLayout, String mStartText, TextInputLayout mEndLayout, String mEndText,
                                      String mTimeErrorText, String mEndErrorText,
                                      TextInputLayout mRoomLayout, String mRoomText, String mRoomErrorText) {

        boolean fieldsOK = true;

        if (mNameText.isEmpty()) {
            mNameLayout.setError(mNameErrorText);
            fieldsOK = false;
        }
        if (mDateText.isEmpty()) {
            mDateLayout.setError(mDateErrorText);
            fieldsOK = false;
        }
        if (mStartText.isEmpty()) {
            mStartLayout.setError(mTimeErrorText);
            fieldsOK = false;
        }
        if (mEndText.isEmpty()) {
            mEndLayout.setError(mTimeErrorText);
            fieldsOK = false;
        }
        if (mRoomText.isEmpty()) {
            mRoomLayout.setError(mRoomErrorText);
            fieldsOK = false;
        }
        if (!mStartText.isEmpty() && !mEndText.isEmpty() && (mEndText).compareTo(mStartText) <= 0) {
            mEndLayout.setError(mEndErrorText);
            fieldsOK = false;
        }
        return fieldsOK;
    }


    public static boolean checkMail(String mMailAddress) {

        boolean mailOK = true;

        int occurAt = 0;
        for (int i = 0; i < mMailAddress.length(); i++) {
            if (mMailAddress.charAt(i) == '@') {
                occurAt ++;
            }
        }

        if (mMailAddress.indexOf("@") < 1 || occurAt != 1
                || mMailAddress.charAt(mMailAddress.length()-3) == '@'
                || mMailAddress.charAt(mMailAddress.length()-2) == '@'
                || mMailAddress.charAt(mMailAddress.length()-1) == '@'
                || mMailAddress.indexOf(".") < 1 || mMailAddress.charAt(mMailAddress.length()-1) == '.') {
            mailOK = false;
        }
        return mailOK;
    }


    public static void dateSetting(TextInputLayout mDateLyt, Context mContext) throws ParseException {
        // Set DatePickerDialog (date listener)
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calD = Calendar.getInstance();
                calD.set(Calendar.YEAR, year);
                calD.set(Calendar.MONTH, month);
                calD.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mDate = calD.getTime();
                mDateLyt.getEditText().setText(dfDateLong.format(mDate));
            }
        };

        // Create DatePickerDialog
        int selectedYear = now.get(Calendar.YEAR);
        int selectedMonth = now.get(Calendar.MONTH);
        int selectedDayOfMonth = now.get(Calendar.DAY_OF_MONTH);

        // Le picker affiche la date déja définie si le champ n'est pas vide, sinon la date du jour
        if (!mDateLyt.getEditText().getText().toString().isEmpty()) {
            Date oldDate = dfDateLong.parse(mDateLyt.getEditText().getText().toString());
            Calendar oldCalDate = Calendar.getInstance();
            oldCalDate.setTime(oldDate);
            selectedYear = oldCalDate.get(Calendar.YEAR);
            selectedMonth = oldCalDate.get(Calendar.MONTH);
            selectedDayOfMonth = oldCalDate.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);

        // Show DatePickerDialog
        datePickerDialog.show();
    }


    public static void timeSetting(Boolean mStart, Boolean mEnd, TextInputLayout mStartTimeLyt, TextInputLayout mEndTimeLyt, Context mContext) throws ParseException {
        // Set TimePickerDialog (time listener)
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calH = Calendar.getInstance();
                calH.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calH.set(Calendar.MINUTE, minute);
                mTime = calH.getTime();
                if (mStart) { mStartTimeLyt.getEditText().setText(dfTime.format(mTime).replace(":", "h"));
                    // Preset end time calculated with default duration
                    if (mEndTimeLyt.getEditText().getText().toString().isEmpty()) {
                        Calendar calH2 = Calendar.getInstance();
                        calH2.setTime(mTime);
                        calH2.add(Calendar.MINUTE,  DEFAULT_MEETING_DURATION_IN_MIN);
                        Date mTime2 = calH2.getTime();
                        mEndTimeLyt.getEditText().setText(dfTime.format(mTime2).replace(":", "h"));
                    }
                }
                if (mEnd) mEndTimeLyt.getEditText().setText(dfTime.format(mTime).replace(":", "h"));
            }
        };

        // Create TimePickerDialog
        int selectedHourOfDay = now.get(Calendar.HOUR_OF_DAY);
        int selectedMinute = now.get(Calendar.MINUTE);

        // Le picker affiche l'heure déja définie si le champ n'est pas vide, sinon l'heure courante
        if (mStart && !mStartTimeLyt.getEditText().getText().toString().isEmpty()) {
            Date oldTime = dfTime.parse(mStartTimeLyt.getEditText().getText().toString().replace("h", ":"));
            Calendar oldCalTime = Calendar.getInstance();
            oldCalTime.setTime(oldTime);
            selectedHourOfDay = oldCalTime.get(Calendar.HOUR_OF_DAY);
            selectedMinute = oldCalTime.get(Calendar.MINUTE);
        } else if (mEnd && !mEndTimeLyt.getEditText().getText().toString().isEmpty()) {
            Date oldTime = dfTime.parse(mEndTimeLyt.getEditText().getText().toString().replace("h", ":"));
            Calendar oldCalTime = Calendar.getInstance();
            oldCalTime.setTime(oldTime);
            selectedHourOfDay = oldCalTime.get(Calendar.HOUR_OF_DAY);
            selectedMinute = oldCalTime.get(Calendar.MINUTE);
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                timeSetListener, selectedHourOfDay, selectedMinute, true);

        // Show DatePickerDialog
        timePickerDialog.show();
    }


}
