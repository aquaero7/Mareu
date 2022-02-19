package com.example.mareu.utils;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import android.view.View;
import com.example.mareu.R;
import org.hamcrest.Matcher;

public class DeleteViewAction implements ViewAction {


    @Override
    public Matcher<View> getConstraints() {
        return null;
    }

    @Override
    public String getDescription() {
        // return null;
        return "Click on specific button";
    }

    @Override
    public void perform(UiController uiController, View view) {
        View button = view.findViewById(R.id.deleteButton);
        button.performClick();
    }

}
