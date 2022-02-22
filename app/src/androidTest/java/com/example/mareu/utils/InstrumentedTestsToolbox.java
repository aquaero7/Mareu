package com.example.mareu.utils;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;

import com.example.mareu.R;

import org.hamcrest.Matchers;
import org.hamcrest.core.AllOf;

public class InstrumentedTestsToolbox {

    public static void fillAllFields() {
        onView(withId(R.id.name_meeting_textView)).perform(replaceText("Réunion U"));
        onView(withId(R.id.date_meeting_layout)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022, 2, 17));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.start_time_meeting_layout)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(10, 0));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.end_time_meeting_layout)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(11, 0));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.room_meeting_textView)).perform(click());
        onData(anything()).atPosition(2).inRoot(RootMatchers.isPlatformPopup()).perform(click());   // Azur
        addParticipantEmail("francis@lamzone.com");
        addParticipantEmail("patrick@lamzone.com");
    }

    public static void setFilterOnTestDate(int day, int month, int year) {
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(click());
    }

    public static void setFilterOnTestRoom() {
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onData(anything()).atPosition(2).perform(click());  // Azur
    }

    public static void resetFilters(){
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(2).perform(click());
    }

    public static void addParticipantEmail(String eMail) {
        onView(withId(R.id.add_participant_textView)).perform(replaceText(eMail));
        onView(withId(R.id.button_add_participant)).perform(click());
    }

    public static void setEndBeforeStart() {
        onView(withId(R.id.start_time_meeting_layout)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(10, 0));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.end_time_meeting_layout)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(9, 0));
        onView(withId(android.R.id.button1)).perform(click());
    }

    public static void createMeetingWithoutParticipant() {
        onView(withId(R.id.end_time_meeting_layout)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(11, 0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.name_meeting_textView)).perform(replaceText("Réunion X"));

        onView(withId(R.id.date_meeting_layout)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022, 2, 17));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.room_meeting_textView)).perform(click());
        onData(anything()).atPosition(2).inRoot(RootMatchers.isPlatformPopup()).perform(click());

        onView(withId(R.id.button_create_meeting)).perform(click());
    }

    public static void deleteMeetingAtPosition(int position) throws InterruptedException {
        onView(AllOf.allOf(isDisplayed(), withId(R.id.recyclerView)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, new DeleteViewAction()));
        // Check if confirmation dialog is displayed...
        Thread.sleep(500);
        onView(withText("Confirmation")).check(matches(isDisplayed()));
        // and confirm deletion action
        onView(withId(android.R.id.button1)).perform(click());
    }

}
