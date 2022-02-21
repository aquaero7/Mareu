package com.example.mareu;

import static org.hamcrest.Matchers.anything;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matchers;
import org.hamcrest.core.AllOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.example.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static com.example.mareu.utils.TextInputLayoutErrorMatcher.hasTextInputLayoutErrorText;

import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.mareu.di.DI;
import com.example.mareu.service.MeetingApiService;
import com.example.mareu.ui.ListMeetingsActivity;
import com.example.mareu.utils.DeleteViewAction;


// @RunWith(AndroidJUnit4.class)
public class OnMeetingActionsInstrumentedTests {

    private final MeetingApiService service = DI.getMeetingApiService();
    private final MeetingApiService newService = DI.getNewMeetingApiService();
    private int meetingsListSize;
    private int meetingsListSizeOnCreationActionTestDate;
    private int meetingsListSizeOnDeletionActionTestDate;
    private int meetingsListSizeOnRoomTest;

    @Rule
    public ActivityScenarioRule<ListMeetingsActivity> mActivityRule = new ActivityScenarioRule<>(ListMeetingsActivity.class);
    private View decorView;

    @Before
    public void setUp() {
        mActivityRule.getScenario().onActivity(new ActivityScenario.ActivityAction<ListMeetingsActivity>() {
            @Override
            public void perform(ListMeetingsActivity activity) {
                decorView = activity.getWindow().getDecorView();
            }
        });

        meetingsListSize = service.getMeetings().size();
        meetingsListSizeOnCreationActionTestDate = service.getMeetingsByDate("17/02/2022").size();
        meetingsListSizeOnDeletionActionTestDate = service.getMeetingsByDate("23/02/2022").size();
        meetingsListSizeOnRoomTest = service.getMeetingsByPlace(service.getRoomByName("Azur")).size();
    }


    @Test
    public void mandatoryInputFieldsAreCheckedWhenCreateMeeting() throws InterruptedException {
        // Check if meetings list is displayed
        onView(ViewMatchers.withId(R.id.recyclerView)).check(matches(isDisplayed()));
        // Go to meetings creation view
        onView(withId(R.id.addMeetingButton)).perform(click());
        // Check if meeting creation view is displayed
        onView(withId(R.id.header_add_meeting)).check(matches(isDisplayed()));

        // Try to create a meeting with all fields empty
        onView(withId(R.id.button_create_meeting)).perform(click());

        // Then check if error texts are displayed on Name/Date/Start/End/Room fields
        onView(withId(R.id.name_meeting_layout)).check(matches(hasTextInputLayoutErrorText("Saisir le nom de la réunion !")));
        onView(withId(R.id.date_meeting_layout)).check(matches(hasTextInputLayoutErrorText("Choisir une date !")));
        onView(withId(R.id.start_time_meeting_layout)).check(matches(hasTextInputLayoutErrorText("Choisir une heure !")));
        onView(withId(R.id.end_time_meeting_layout)).check(matches(hasTextInputLayoutErrorText("Choisir une heure !")));
        onView(withId(R.id.room_meeting_layout)).check(matches(hasTextInputLayoutErrorText("Choisir une salle !")));

        // Set end time earlier than start time
        onView(withId(R.id.start_time_meeting_layout)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(10, 0));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.end_time_meeting_layout)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(9, 0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.button_create_meeting)).perform(click());

        // Then check if error text is displayed
        onView(withId(R.id.end_time_meeting_layout)).check(matches(hasTextInputLayoutErrorText("Heure de fin incorrecte !")));

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
        Thread.sleep(500);
        onView(withText("Confirmation")).check(matches(isDisplayed()));
        // and cancel action
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
        onView(withId(R.id.add_participant_layout)).check(matches(hasTextInputLayoutErrorText(errorMessage)));
        addParticipantEmail(".francis@lamzone.com");
        onView(withId(R.id.add_participant_layout)).check(matches(hasTextInputLayoutErrorText(errorMessage)));
        addParticipantEmail("francis@lamzone.com@");
        onView(withId(R.id.add_participant_layout)).check(matches(hasTextInputLayoutErrorText(errorMessage)));
        addParticipantEmail("francis@lamzone.co@m");
        onView(withId(R.id.add_participant_layout)).check(matches(hasTextInputLayoutErrorText(errorMessage)));
        addParticipantEmail("francis@lamzone.c@om");
        onView(withId(R.id.add_participant_layout)).check(matches(hasTextInputLayoutErrorText(errorMessage)));
        addParticipantEmail("francis@lamzone.com.");
        onView(withId(R.id.add_participant_layout)).check(matches(hasTextInputLayoutErrorText(errorMessage)));
        addParticipantEmail("francisatlamzone.com");
        onView(withId(R.id.add_participant_layout)).check(matches(hasTextInputLayoutErrorText(errorMessage)));
        addParticipantEmail("francis@lam@zone.com");
        onView(withId(R.id.add_participant_layout)).check(matches(hasTextInputLayoutErrorText(errorMessage)));
        addParticipantEmail("francis@lam zone.com");
        onView(withId(R.id.add_participant_layout)).check(matches(hasTextInputLayoutErrorText(errorMessage)));

        // Type correct mail address and check if participant is added
        addParticipantEmail("francis@lamzone.com");
        onData(anything()).inAdapterView(withId(R.id.participants_listView)).atPosition(0).check(matches(withText("francis@lamzone.com")));
        addParticipantEmail("francis.theboss@fr.lamzone.com");
        onData(anything()).inAdapterView(withId(R.id.participants_listView)).atPosition(1).check(matches(withText("francis.theboss@fr.lamzone.com")));


        // Return to list view
        onView(withId(R.id.button_return)).perform(click());
    }

    @Test
    public void createMeetingActionShouldAddItem() {
        // Check if meetings list is displayed
        onView(ViewMatchers.withId(R.id.recyclerView)).check(matches(isDisplayed()));
        setFilterOnTestDate(17, 2, 2022);
        // Check the number of meetings scheduled on this date
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(meetingsListSizeOnCreationActionTestDate));
        setFilterOnTestRoom();  // Azur
        // Check the number of meetings scheduled in this room
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(meetingsListSizeOnRoomTest));
        // Reset filters
        resetFilters();
        // Check the size of the whole meetings list
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(meetingsListSize));

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
        // Check if the list of meetings scheduled on test date contains one more item
        setFilterOnTestDate(17, 2, 2022);
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(meetingsListSizeOnCreationActionTestDate + 1));
        // Check if the list of meetings scheduled in test room contains one more item
        setFilterOnTestRoom();
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(meetingsListSizeOnRoomTest + 1));
        // Check if the whole meetings list contains one more item
        resetFilters();
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(meetingsListSize + 1));
    }

    @Test
    public void deleteMeetingActionShouldRemoveItem() throws InterruptedException {
        // Check if meetings list is displayed
        onView(ViewMatchers.withId(R.id.recyclerView)).check(matches(isDisplayed()));
        setFilterOnTestDate(23, 2, 2022);
        // Check the number of meetings scheduled on this date
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(meetingsListSizeOnDeletionActionTestDate));
        setFilterOnTestRoom();  // Azur
        // Check the number of meetings scheduled in this room
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(meetingsListSizeOnRoomTest));
        // Reset filters
        resetFilters();
        // Check the size of the whole meetings list
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(meetingsListSize));

        // Delete Meeting 'Réunion B' scheduled in room 'Azur' from 15h00 to 15h45
        // Click on delete action button
        onView(AllOf.allOf(isDisplayed(), withId(R.id.recyclerView)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, new DeleteViewAction()));
        // Check if confirmation dialog is displayed
        Thread.sleep(500);
        onView(withText("Confirmation")).check(matches(isDisplayed()));
        // and confirm deletion action
        onView(withId(android.R.id.button1)).perform(click());

        // Check if the whole meetings list contains one less item
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(meetingsListSize - 1));
        // Check if the list of meetings scheduled on test date contains one less item
        setFilterOnTestDate(23, 2, 2022);
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(meetingsListSizeOnDeletionActionTestDate - 1));
        // Check if the list of meetings scheduled in test room contains one less item
        setFilterOnTestRoom();
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(meetingsListSizeOnRoomTest - 1));
    }

    @Test
    public void createAndDeleteMeetingActionShouldUpdateSlot() throws InterruptedException {
        // Create a meeting on an available slot
        createMeetingActionShouldAddItem();

        // Try to create another meeting on the same slot in the same room
        // Go to meetings creation view
        onView(withId(R.id.addMeetingButton)).perform(click());
        // Check if meeting creation view is displayed
        onView(withId(R.id.header_add_meeting)).check(matches(isDisplayed()));
        // Fill all fields for meeting creation
        fillAllFields();
        // Schedule meeting
        onView(withId(R.id.button_create_meeting)).perform(click());

        // Check if error text on room field is displayed
        onView(withId(R.id.room_meeting_layout)).check(matches(hasTextInputLayoutErrorText("La salle n'est pas disponible sur ce créneau !")));

        // Return to list view
        onView(withId(R.id.button_return)).perform(click());
        // Set filter on test date
        setFilterOnTestDate(17, 2, 2022);

        // Delete the meeting we have created
        onView(AllOf.allOf(isDisplayed(), withId(R.id.recyclerView)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        Thread.sleep(500);
        onView(withText("Confirmation")).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());

        // Try to create the same meeting again and check if it is now possible
        createMeetingActionShouldAddItem();


        // Cleanup : Delete the meeting we have created
        setFilterOnTestDate(17, 2, 2022);
        onView(AllOf.allOf(isDisplayed(), withId(R.id.recyclerView)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        Thread.sleep(500);
        onView(withText("Confirmation")).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
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

    public void setFilterOnTestDate(int day, int month, int year) {
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
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
