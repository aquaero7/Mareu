package com.example.mareu;

import static org.hamcrest.Matchers.anything;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.example.mareu.utils.InstrumentedTestsToolbox.addParticipantEmail;
import static com.example.mareu.utils.InstrumentedTestsToolbox.createMeetingWithoutParticipant;
import static com.example.mareu.utils.InstrumentedTestsToolbox.deleteMeetingAtPosition;
import static com.example.mareu.utils.InstrumentedTestsToolbox.fillAllFields;
import static com.example.mareu.utils.InstrumentedTestsToolbox.resetFilters;
import static com.example.mareu.utils.InstrumentedTestsToolbox.setEndBeforeStart;
import static com.example.mareu.utils.InstrumentedTestsToolbox.setFilterOnTestDate;
import static com.example.mareu.utils.InstrumentedTestsToolbox.setFilterOnTestRoom;
import static com.example.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static com.example.mareu.utils.TextInputLayoutErrorMatcher.hasTextInputLayoutErrorText;

import android.view.View;

import com.example.mareu.di.DI;
import com.example.mareu.service.MeetingApiService;
import com.example.mareu.ui.ListMeetingsActivity;

import java.util.Arrays;
import java.util.List;


// @RunWith(AndroidJUnit4.class)
public class OnMeetingActionsInstrumentedTests {

    private final List<String> WRONG_EMAIL_TEST_LIST = Arrays.asList("@francis@lamzone.com",
            ".francis@lamzone.com", "francis@lamzone.com@", "francis@lamzone.co@m", "francis@lamzone.c@om",
            "francis@lamzone.com.", "francisatlamzone.com", "francis@lam@zone.com", "francis@lam zone.com");

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
        onView(withId(R.id.name_meeting_layout))
                .check(matches(hasTextInputLayoutErrorText("Saisir le nom de la réunion !")));
        onView(withId(R.id.date_meeting_layout))
                .check(matches(hasTextInputLayoutErrorText("Choisir une date !")));
        onView(withId(R.id.start_time_meeting_layout))
                .check(matches(hasTextInputLayoutErrorText("Choisir une heure !")));
        onView(withId(R.id.end_time_meeting_layout))
                .check(matches(hasTextInputLayoutErrorText("Choisir une heure !")));
        onView(withId(R.id.room_meeting_layout))
                .check(matches(hasTextInputLayoutErrorText("Choisir une salle !")));

        // Set end time earlier than start time
        setEndBeforeStart();
        onView(withId(R.id.button_create_meeting)).perform(click());

        // Then check if error text is displayed
        onView(withId(R.id.start_time_meeting_layout))
                .check(matches(hasTextInputLayoutErrorText("Horaires incohérents !")));
        onView(withId(R.id.end_time_meeting_layout))
                .check(matches(hasTextInputLayoutErrorText("Horaires incohérents !")));

        // Create meeting without participant
        createMeetingWithoutParticipant();

        // Then check if dialog is displayed in order to confirm meeting creation without participant
        Thread.sleep(500);
        onView(withText("Confirmation")).check(matches(isDisplayed()));
        // and cancel action
        onView(withId(android.R.id.button2)).perform(click());


        // Cleanup : Return to list view
        onView(withId(R.id.button_return)).perform(click());
    }

    @Test
    public void participantInputTextIsCheckedBeforeAddAction() {
        // Check if meetings list is displayed
        onView(ViewMatchers.withId(R.id.recyclerView)).check(matches(isDisplayed()));
        // Go to meetings creation view
        onView(withId(R.id.addMeetingButton)).perform(click());
        // Check if meeting creation view is displayed
        onView(withId(R.id.header_add_meeting)).check(matches(isDisplayed()));

        // Type mail addresses with errors and check if error message is displayed
        for (String wrongEmail : WRONG_EMAIL_TEST_LIST) {
            addParticipantEmail(wrongEmail);
            onView(withId(R.id.add_participant_layout))
                    .check(matches(hasTextInputLayoutErrorText("Adresse eMail incorrecte !")));
        }

        // Then, type correct mail address and check if participant is added
        addParticipantEmail("francis@lamzone.com");
        onData(anything()).inAdapterView(withId(R.id.participants_listView)).atPosition(0)
                .check(matches(withText("francis@lamzone.com")));
        addParticipantEmail("francis.theboss@fr.lamzone.com");
        onData(anything()).inAdapterView(withId(R.id.participants_listView)).atPosition(1)
                .check(matches(withText("francis.theboss@fr.lamzone.com")));


        // Cleanup : Return to list view
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

        // Delete Meeting 'Réunion B' scheduled in room 'Azur' from 15h00 to 15h45...

        // Click on delete action button
        deleteMeetingAtPosition(2);

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

        // Try to create another meeting on the same slot in the same room...

        // Go to meetings creation view
        onView(withId(R.id.addMeetingButton)).perform(click());
        // Check if meeting creation view is displayed
        onView(withId(R.id.header_add_meeting)).check(matches(isDisplayed()));
        // Fill all fields for meeting creation
        fillAllFields();
        // Schedule meeting
        onView(withId(R.id.button_create_meeting)).perform(click());

        // Check if error text on room field is displayed
        onView(withId(R.id.room_meeting_layout))
                .check(matches(hasTextInputLayoutErrorText("Salle non disponible sur ce créneau !")));

        // Return to list view
        onView(withId(R.id.button_return)).perform(click());
        // Set filter on test date
        setFilterOnTestDate(17, 2, 2022);

        // Delete the meeting we just created
        deleteMeetingAtPosition(0);

        // Try to create the same meeting again and check if it is now possible
        createMeetingActionShouldAddItem();


        // Cleanup : Delete the meeting we just created
        setFilterOnTestDate(17, 2, 2022);
        deleteMeetingAtPosition(0);
    }

}
