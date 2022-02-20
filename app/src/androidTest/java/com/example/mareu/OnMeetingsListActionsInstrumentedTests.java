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
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.example.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static com.example.mareu.utils.RecyclerViewPosition.atPosition;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import android.widget.DatePicker;

import com.example.mareu.di.DI;
import com.example.mareu.service.MeetingApiService;
import com.example.mareu.ui.ListMeetingsActivity;

public class OnMeetingsListActionsInstrumentedTests {

    private final MeetingApiService service = DI.getMeetingApiService();
    private static final int DUMMY_MEETINGS_LIST_SIZE = 20;


    @Rule
    public ActivityScenarioRule<ListMeetingsActivity> mActivityRule = new ActivityScenarioRule<ListMeetingsActivity>(ListMeetingsActivity.class);

    @Before
    public void setUp() {
    }


    @Test
    public void meetingsListShouldBeDisplayedAndNotBeEmpty() {
        // Reset filters
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(2).perform(click());

        onView(ViewMatchers.withId(R.id.recyclerView)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.recyclerView)).check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void listFilteredByDateShouldDisplayOnlySelectedMeetings() {
        // Set filter on a date without meeting
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022, 2, 17));
        onView(withId(android.R.id.button1)).perform(click());

        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(0));

        // Set filter on a date when 5 meetings are scheduled
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022, 2, 23));
        onView(withId(android.R.id.button1)).perform(click());

        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(5));
    }

    @Test
    public void listFilteredByPlaceShouldDisplayOnlySelectedMeetings() {
        // Set filter on room 'Azur' where 2 meetings are scheduled
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onData(anything()).atPosition(2).perform(click());

        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(2));
    }

    @Test
    public void filtersResetShouldDisplayAllMeetings() {
        // Reset filters
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        // Scroll to the end of the list
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollToPosition(DUMMY_MEETINGS_LIST_SIZE - 1));

        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(DUMMY_MEETINGS_LIST_SIZE));
    }

    @Test
    public void listShouldDisplayMeetingsWithAllInformationRequested() {
        String meetingInfoLine1 = "Réunion T - 13h30 - Cosmos";
        String meetingInfoLine2 = "francis@lamzone.com, patrick@lamzone.com, ";

        // Reset filters
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        // Scroll to the meeting before last meeting in the list
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollToPosition(DUMMY_MEETINGS_LIST_SIZE - 2));

        // Check if we math both lines
        onView(allOf(withText(meetingInfoLine1), withParent(withChild(withText(meetingInfoLine2))))).check(matches(isDisplayed()));

        /* Other way using 'atPosition' method in utils
        onView(allOf(isDisplayed(), withId(R.id.recyclerView)))
                .check(matches(atPosition(DUMMY_MEETINGS_LIST_SIZE - 2, hasDescendant(withText(containsString(meetingInfoLine1))))))
                .check(matches(atPosition(DUMMY_MEETINGS_LIST_SIZE - 2, hasDescendant(withText(containsString(meetingInfoLine2))))));
        */
    }

}
