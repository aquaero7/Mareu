package com.example.mareu;

import static org.hamcrest.Matchers.anything;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.example.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.mareu.di.DI;
import com.example.mareu.service.MeetingApiService;
import com.example.mareu.ui.ListMeetingsActivity;
import com.example.mareu.utils.ChildViewMatcher;
import com.example.mareu.utils.TextInputLayoutErrorMatcher;

public class OnMeetingActionsInstrumentedTests {

    private final MeetingApiService service = DI.getMeetingApiService();
    private static final int DUMMY_MEETINGS_LIST_SIZE = 20;
    private static final int MEETINGS_LIST_ON_DATE_TEST_SIZE = 0;
    private static final int MEETINGS_LIST_ON_ROOM_TEST_SIZE = 2;

    @Rule
    public ActivityScenarioRule<ListMeetingsActivity> mActivityRule = new ActivityScenarioRule<ListMeetingsActivity>(ListMeetingsActivity.class);

    @Before
    public void setUp() {
    }

    /*
    @After
    public void reset() {
        onView(withId(R.id.button_return)).perform(click());
    }
    */


    @Test
    public void mandatoryInputFieldsAreCheckedWhenCreateMeeting() {
        // Check if meetings list is displayed
        onView(ViewMatchers.withId(R.id.recyclerView)).check(matches(isDisplayed()));
        // Go to meetings creation view
        onView(withId(R.id.addMeetingButton)).perform(click());
        // Check if meeting creation view is displayed
        onView(withId(R.id.header_add_meeting)).check(matches(isDisplayed()));

        // Try to create a meeting with all fields empty
        onView(withId(R.id.button_create_meeting)).perform(click());

        // Then check if error texts are displayed on Name/Date/Start/End/Room fields
        onView(withId(R.id.name_meeting_layout)).check(matches(TextInputLayoutErrorMatcher
                .hasTextInputLayoutErrorText("Saisir le nom de la réunion !")));
        onView(withId(R.id.date_meeting_layout)).check(matches(TextInputLayoutErrorMatcher
                .hasTextInputLayoutErrorText("Choisir une date !")));
        onView(withId(R.id.start_time_meeting_layout)).check(matches(TextInputLayoutErrorMatcher
                .hasTextInputLayoutErrorText("Choisir une heure !")));
        onView(withId(R.id.end_time_meeting_layout)).check(matches(TextInputLayoutErrorMatcher
                .hasTextInputLayoutErrorText("Choisir une heure !")));
        onView(withId(R.id.room_meeting_layout)).check(matches(TextInputLayoutErrorMatcher
                .hasTextInputLayoutErrorText("Choisir une salle !")));

        // Set end time earlier than start time
        onView(withId(R.id.start_time_meeting_layout)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(10, 0));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.end_time_meeting_layout)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(9, 0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.button_create_meeting)).perform(click());

        // Then check if error text is displayed
        onView(withId(R.id.end_time_meeting_layout)).check(matches(TextInputLayoutErrorMatcher
                .hasTextInputLayoutErrorText("Heure de fin incorrecte !")));

        // Create meeting without participant
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

        // Then check if dialog is displayed in order to confirm meeting creation without participant
        onView(withText("Confirmation")).check(matches(isDisplayed()));
        // and close dialog with cancel action
        onView(withId(android.R.id.button2)).perform(click());


        // Return to list view
        onView(withId(R.id.button_return)).perform(click());
    }

    @Test
    public void participantInputTextIsCheckedBeforeAddAction() {
        String errorMessage = "L'adresse eMail est incorrecte !";
        // Check if meetings list is displayed
        onView(ViewMatchers.withId(R.id.recyclerView)).check(matches(isDisplayed()));
        // Go to meetings creation view
        onView(withId(R.id.addMeetingButton)).perform(click());
        // Check if meeting creation view is displayed
        onView(withId(R.id.header_add_meeting)).check(matches(isDisplayed()));

        // Type mail address with errors and check if error message is displayed
        addParticipantEmail("@francis@lamzone.com");
        onView(withId(R.id.add_participant_layout)).check(matches(TextInputLayoutErrorMatcher.hasTextInputLayoutErrorText(errorMessage)));
        addParticipantEmail(".francis@lamzone.com");
        onView(withId(R.id.add_participant_layout)).check(matches(TextInputLayoutErrorMatcher.hasTextInputLayoutErrorText(errorMessage)));
        addParticipantEmail(" francis@lamzone.com");
        onView(withId(R.id.add_participant_layout)).check(matches(TextInputLayoutErrorMatcher.hasTextInputLayoutErrorText(errorMessage)));
        addParticipantEmail("francis@lam@zone.com");
        onView(withId(R.id.add_participant_layout)).check(matches(TextInputLayoutErrorMatcher.hasTextInputLayoutErrorText(errorMessage)));
        // ...  TODO





        // Type correct mail address and check if participant is added
        addParticipantEmail("francis@lamzone.com");
        onData(anything()).inAdapterView(withId(R.id.participants_listView)).atPosition(0).check(matches(withText("francis@lamzone.com")));
        addParticipantEmail("francis.theboss@fr.lamzone.com");
        onData(anything()).inAdapterView(withId(R.id.participants_listView)).atPosition(1).check(matches(withText("francis.theboss@fr.lamzone.com")));


        // Return to list view
        onView(withId(R.id.button_return)).perform(click());
    }
    
    @Test
    public void createMeetingActionShouldAddSlot() {





    }

    @Test
    public void deleteMeetingActionShouldRemoveSlot() {

    }

    @Test
    public void createMeetingActionShouldAddItem() {
        // Check if meetings list is displayed
        onView(ViewMatchers.withId(R.id.recyclerView)).check(matches(isDisplayed()));
        setFilterOnTestDate();  // 17/02/2022
        // Check if no meeting is scheduled on this date
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(MEETINGS_LIST_ON_DATE_TEST_SIZE));
        setFilterOnTestRoom();  // Azur
        // Check if 2 meetings are scheduled in this room
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(MEETINGS_LIST_ON_ROOM_TEST_SIZE));
        // Reset filters
        resetFilters();
        // Check if whole meeting list contains 20 items
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(DUMMY_MEETINGS_LIST_SIZE));

        // Go to meetings creation view
        onView(withId(R.id.addMeetingButton)).perform(click());
        // Check if meeting creation view is displayed
        onView(withId(R.id.header_add_meeting)).check(matches(isDisplayed()));
        // Fill all fields for meeting creation
        fillAllFields();
        // Schedule meeting
        onView(withId(R.id.button_create_meeting)).perform(click());

        // Check that we are back to meetings list
        onView(ViewMatchers.withId(R.id.recyclerView)).check(matches(isDisplayed()));
        // Check if list of meetings scheduled on test date contains one more item
        setFilterOnTestDate();
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(MEETINGS_LIST_ON_DATE_TEST_SIZE + 1));
        // Check if list of meetings scheduled in test room contains one more item
        setFilterOnTestRoom();
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(MEETINGS_LIST_ON_ROOM_TEST_SIZE + 1));
        // Check if whole meeting list contains one more items
        resetFilters();
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(DUMMY_MEETINGS_LIST_SIZE + 1));
    }

    @Test
    public void deleteMeetingActionShouldRemoveItem() {

    }


    /** Methods **/

    public void fillAllFields() {
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

    public void setFilterOnTestDate() {
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022, 2, 17));
        onView(withId(android.R.id.button1)).perform(click());
    }

    public void setFilterOnTestRoom() {
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onData(anything()).atPosition(2).perform(click());  // Azur
    }

    public void resetFilters(){
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(2).perform(click());
    }

    public void addParticipantEmail(String eMail) {
        onView(withId(R.id.add_participant_textView)).perform(replaceText(eMail));
        onView(withId(R.id.button_add_participant)).perform(click());
    }
}
