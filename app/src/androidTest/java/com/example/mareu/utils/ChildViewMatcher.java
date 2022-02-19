package com.example.mareu.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ChildViewMatcher {

    /** Example to call this method :
     *
     * onView(allOf(childAtPosition(childAtPosition(withId(R.id.filter_button), 0), 0), isDisplayed())).perform(click());
     *
     */

    public static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                // return false;
                ViewParent parent = item.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent) && item.equals(((ViewGroup) parent).getChildAt(position));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent");
                parentMatcher.describeTo(description);
            }
        };
    }

}
