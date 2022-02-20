package com.example.mareu.utils;

import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class TextInputLayoutErrorMatcher {

    public static Matcher<View> hasTextInputLayoutErrorText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }
                CharSequence error = ((TextInputLayout) view).getError();
                if (error == null) {
                    return false;
                }
                String errorText = error.toString();
                return expectedErrorText.equals(errorText);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }


}
