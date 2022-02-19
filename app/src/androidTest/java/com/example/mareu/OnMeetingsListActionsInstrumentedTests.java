package com.example.mareu;

import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.*;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.example.mareu.utils.ChildViewMatcher.childAtPosition;
import static com.example.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;

import com.example.mareu.di.DI;
import com.example.mareu.service.MeetingApiService;
import com.example.mareu.ui.ListMeetingsActivity;
import com.example.mareu.ui.MyMeetingRecyclerViewAdapter;

import java.util.List;

public class OnMeetingsListActionsInstrumentedTests {

    private final MeetingApiService service = DI.getMeetingApiService();
    private static final int DUMMY_MEETINGS_LIST_SIZE = 20;


    @Rule
    public ActivityScenarioRule<ListMeetingsActivity> mActivityRule = new ActivityScenarioRule<ListMeetingsActivity>(ListMeetingsActivity.class);

    @Before
    public void setUp() {
    }


    @Test
    public void meetingsListShouldNotBeEmpty() {
        // Reset filters
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(2).perform(click());

        onView(ViewMatchers.withId(R.id.recyclerView)).check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void listFilteredByDateShouldDisplayOnlySelectedMeetings() {
        // Set filter on date without meeting
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022, 02, 17));
        onView(withId(android.R.id.button1)).perform(click());

        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(0));

        // Set filter on date when 5 meetings are scheduled
        onView(withId(R.id.filter_button)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022, 02, 23));
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
        // Scroll to the end of list
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollToPosition(DUMMY_MEETINGS_LIST_SIZE - 1));

        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(DUMMY_MEETINGS_LIST_SIZE));
    }

}
