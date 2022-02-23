package com.example.mareu.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mareu.R;
import com.example.mareu.model.ReservationSlot;
import com.example.mareu.model.Room;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public class MeetingToolbox {

    private static final int DEFAULT_MEETING_DURATION_IN_MIN = 45;
    private static final SimpleDateFormat dfDateLong = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.FRANCE);
    private static final SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm", Locale.FRANCE);
    private static Date mDate, mTime;
    private static Calendar now;


    public static boolean checkSlot(String mDate, String mStart, String mEnd, @NonNull Room mRoom, TextInputLayout mLayout, String mErrorText) {

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


    public static boolean checkFields(TextInputLayout mNameLayout, @NonNull String mNameText, String mNameErrorText,
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
            mStartLayout.setError(mEndErrorText);
            mEndLayout.setError(mEndErrorText);
            fieldsOK = false;
        }
        return fieldsOK;
    }


    public static boolean checkMail(String mMailAddress) {

        boolean mailOK = true;

        String pattern = "[[\\S]&&[^@\\.]][[\\S]&&[^@]]*@[[\\S]&&[^@]]+\\.[[\\S]&&[^@\\.]]+";
        boolean matches = Pattern.matches(pattern, mMailAddress);
        if (!matches) mailOK = false;

        return mailOK;
    }


    public static void dateSetting(@NonNull TextInputLayout mDateLyt, Context mContext) {
        // Set DatePickerDialog (date listener)
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calD = Calendar.getInstance();
                calD.set(Calendar.YEAR, year);
                calD.set(Calendar.MONTH, month);
                calD.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mDate = calD.getTime();
                Objects.requireNonNull(mDateLyt.getEditText()).setText(dfDateLong.format(mDate));
                mDateLyt.setError("");
            }
        };

        // Create DatePickerDialog
        now = Calendar.getInstance();
        int selectedYear = now.get(Calendar.YEAR);
        int selectedMonth = now.get(Calendar.MONTH);
        int selectedDayOfMonth = now.get(Calendar.DAY_OF_MONTH);

        // Le picker affiche la date déja définie si le champ n'est pas vide, sinon la date du jour
        if (!Objects.requireNonNull(mDateLyt.getEditText()).getText().toString().isEmpty()) {
            Date oldDate = null;
            try {
                oldDate = dfDateLong.parse(mDateLyt.getEditText().getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("ParseException in MeetingToolbox.dateSetting : " + e.getMessage());
                Toast.makeText(mContext, R.string.exception_format_date, Toast.LENGTH_SHORT).show();
            }
            Calendar oldCalDate = Calendar.getInstance();
            assert oldDate != null;
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


    public static void timeSetting(Boolean mStart, Boolean mEnd, TextInputLayout mStartTimeLyt, TextInputLayout mEndTimeLyt, Context mContext) {
        // Set TimePickerDialog (time listener)
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calH = Calendar.getInstance();
                calH.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calH.set(Calendar.MINUTE, minute);
                mTime = calH.getTime();
                if (mStart) {
                    Objects.requireNonNull(mStartTimeLyt.getEditText()).setText(dfTime.format(mTime).replace(":", "h"));
                    mStartTimeLyt.setError("");
                    mEndTimeLyt.setError("");

                    // Preset end time calculated with default duration
                    if (Objects.requireNonNull(mEndTimeLyt.getEditText()).getText().toString().isEmpty()) {
                        Calendar calH2 = Calendar.getInstance();
                        calH2.setTime(mTime);
                        calH2.add(Calendar.MINUTE,  DEFAULT_MEETING_DURATION_IN_MIN);
                        Date mTime2 = calH2.getTime();
                        mEndTimeLyt.getEditText().setText(dfTime.format(mTime2).replace(":", "h"));
                    }
                }
                if (mEnd) {
                    Objects.requireNonNull(mEndTimeLyt.getEditText()).setText(dfTime.format(mTime).replace(":", "h"));
                    mEndTimeLyt.setError("");
                    if (!Objects.requireNonNull(mStartTimeLyt.getEditText()).getText().toString().isEmpty()) { mStartTimeLyt.setError(""); }
                }
            }
        };

        // Create TimePickerDialog
        now = Calendar.getInstance();
        int selectedHourOfDay = now.get(Calendar.HOUR_OF_DAY);
        int selectedMinute = now.get(Calendar.MINUTE);
        // Le picker affiche l'heure déja définie si le champ n'est pas vide, sinon l'heure courante
        if (mStart && !Objects.requireNonNull(mStartTimeLyt.getEditText()).getText().toString().isEmpty()) {
            Date oldTime = null;
            try {
                oldTime = dfTime.parse(mStartTimeLyt.getEditText().getText().toString().replace("h", ":"));
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("ParseException in MeetingToolbox.timeSetting : " + e.getMessage());
                Toast.makeText(mContext, R.string.exception_format_start, Toast.LENGTH_SHORT).show();
            }
            Calendar oldCalTime = Calendar.getInstance();
            assert oldTime != null;
            oldCalTime.setTime(oldTime);
            selectedHourOfDay = oldCalTime.get(Calendar.HOUR_OF_DAY);
            selectedMinute = oldCalTime.get(Calendar.MINUTE);
        } else if (mEnd && !Objects.requireNonNull(mEndTimeLyt.getEditText()).getText().toString().isEmpty()) {
            Date oldTime = null;
            try {
                oldTime = dfTime.parse(mEndTimeLyt.getEditText().getText().toString().replace("h", ":"));
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("ParseException in MeetingToolbox.timeSetting : " + e.getMessage());
                Toast.makeText(mContext, R.string.exception_format_end, Toast.LENGTH_SHORT).show();
            }
            Calendar oldCalTime = Calendar.getInstance();
            assert oldTime != null;
            oldCalTime.setTime(oldTime);
            selectedHourOfDay = oldCalTime.get(Calendar.HOUR_OF_DAY);
            selectedMinute = oldCalTime.get(Calendar.MINUTE);
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                timeSetListener, selectedHourOfDay, selectedMinute, true);

        // Show DatePickerDialog
        timePickerDialog.show();
    }

    public static void clearErrorFields(TextInputLayout nameLyt, TextInputLayout dateLyt,
                                        TextInputLayout startTimeLyt, TextInputLayout endTimeLyt,
                                        TextInputLayout roomLyt, TextInputLayout addParticipantLyt) {
        nameLyt.setError("");
        dateLyt.setError("");
        startTimeLyt.setError("");
        endTimeLyt.setError("");
        roomLyt.setError("");
        addParticipantLyt.setError("");
    }

}
