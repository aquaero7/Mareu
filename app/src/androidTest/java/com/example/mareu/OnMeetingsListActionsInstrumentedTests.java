package com.example.mareu;

import static org.hamcrest.Matchers.anything;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.example.mareu.utils.InstrumentedTestsToolbox.clearTestMeetingsList;
import static com.example.mareu.utils.InstrumentedTestsToolbox.setTestMeetingsList_1;
import static com.example.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static com.example.mareu.utils.RecyclerViewPosition.atPosition;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import android.widget.DatePicker;

import com.example.mareu.di.DI;
import com.example.mareu.service.MeetingApiService;
import com.example.mareu.ui.ListMeetingsActivity;

// @RunWith(AndroidJUnit4.class)
public class OnMeetingsListActionsInstrumentedTests {

    private final MeetingApiService service = DI.getMeetingApiService();
    private static final int MEETINGS_LIST_SIZE = 4;


    @Rule
    public ActivityScenarioRule<ListMeetingsActivity> mActivityRule = new ActivityScenarioRule<ListMeetingsActivity>(ListMeetingsActivity.class);

    @Before
    public void setUp() {

    }


    @Test
    public void meetingsListShouldBeDisplayedEmptyOnStart() {
        // Reset filters
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(2).perform(click());

        onView(ViewMatchers.withId(R.id.recyclerView)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(0));
    }

    @Test
    public void listFilteredByDateShouldDisplayOnlySelectedMeetings() {
        setTestMeetingsList_1();

        // Set filter on a date without meeting
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022, 2, 17));
        onView(withId(android.R.id.button1)).perform(click());

        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(0));

        // Set filter on a date when 2 meetings are scheduled
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022, 2, 24));
        onView(withId(android.R.id.button1)).perform(click());

        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(2));

        clearTestMeetingsList();
    }

    @Test
    public void listFilteredByPlaceShouldDisplayOnlySelectedMeetings() {
        setTestMeetingsList_1();

        // Set filter on room 'Azur' where 2 meetings are scheduled
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onData(anything()).atPosition(2).perform(click());

        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(2));

        clearTestMeetingsList();
    }

    @Test
    public void filtersResetShouldDisplayAllMeetings() {
        setTestMeetingsList_1();

        // Reset filters
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        // Scroll to the end of the list
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollToPosition(MEETINGS_LIST_SIZE - 1));

        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(MEETINGS_LIST_SIZE));

        clearTestMeetingsList();
    }

    @Test
    public void listShouldDisplayMeetingsWithAllInformationRequested() {
        setTestMeetingsList_1();

        String meetingInfoLineA1 = "Réunion A - 16h00 - Azur";
        String meetingInfoLineB1 = "Réunion B - 11h00 - Corail";
        String meetingInfoLineC1 = "Réunion C - 15h00 - Azur";
        String meetingInfoLineD1 = "Réunion D - 10h00 - Corail";
        String meetingInfoLine2 = "francis@lamzone.com, patrick@lamzone.com, ";


        // Reset filters
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        // Scroll to the meeting before last meeting in the list
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollToPosition(MEETINGS_LIST_SIZE - 2));

        // Check if we math both lines
        onView(allOf(withText(meetingInfoLineA1), withParent(withChild(withText(meetingInfoLine2))))).check(matches(isDisplayed()));
        onView(allOf(withText(meetingInfoLineB1), withParent(withChild(withText(meetingInfoLine2))))).check(matches(isDisplayed()));
        onView(allOf(withText(meetingInfoLineC1), withParent(withChild(withText(meetingInfoLine2))))).check(matches(isDisplayed()));
        onView(allOf(withText(meetingInfoLineD1), withParent(withChild(withText(meetingInfoLine2))))).check(matches(isDisplayed()));

        /* Other way using 'atPosition' method in utils
        onView(allOf(isDisplayed(), withId(R.id.recyclerView)))
                .check(matches(atPosition(0, hasDescendant(withText(containsString(meetingInfoLineD1))))))
                .check(matches(atPosition(0, hasDescendant(withText(containsString(meetingInfoLine2))))))
                .check(matches(atPosition(1, hasDescendant(withText(containsString(meetingInfoLineC1))))))
                .check(matches(atPosition(1, hasDescendant(withText(containsString(meetingInfoLine2))))))
                .check(matches(atPosition(2, hasDescendant(withText(containsString(meetingInfoLineB1))))))
                .check(matches(atPosition(2, hasDescendant(withText(containsString(meetingInfoLine2))))))
                .check(matches(atPosition(3, hasDescendant(withText(containsString(meetingInfoLineA1))))))
                .check(matches(atPosition(3, hasDescendant(withText(containsString(meetingInfoLine2))))));
        */

        clearTestMeetingsList();
    }

}
