package com.onfido.android.app.sample;

import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isRoot;

/**
 * Created by jonathantaylor on 19/01/2017.
 */

public class TestUtilities {

    public static final int GRANT_BUTTON_INDEX = 1;
    public static final int DENY_BUTTON_INDEX = 0;

    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }

    public static boolean clickPermissionsIfNeeded(int BUTTON_INDEX) throws UiObjectNotFoundException {
        boolean porf = false;
//        onView(isRoot()).perform(waitFor(5000));
        if (Build.VERSION.SDK_INT >= 23) {
            UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            UiObject permissionsButton = device.findObject(new UiSelector().text("Allow")
                    .clickable(true)
                    .checkable(false)
                    .focusable(true)
                    .index(BUTTON_INDEX));
            if (permissionsButton.exists()) {
                try {
                    permissionsButton.click();
                    porf = true;
                } catch (UiObjectNotFoundException e) {
                    porf = false;
                    System.out.println("There is no permissions dialog to interact with");
                }
            }
        }
        return porf;
    }

    public static boolean dialogExists(String dialogString) throws UiObjectNotFoundException {
        boolean porf = true;
        if (Build.VERSION.SDK_INT >= 23) {
            UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            UiObject dialogInstance = device.findObject(new UiSelector().text(dialogString)
                    .enabled(true));
            if (dialogInstance.exists()) {
                try {
                    System.out.println(dialogInstance.getText());
                } catch (UiObjectNotFoundException e) {
                    porf = false;
                    System.out.println("There is no dialog to interact with");
                }
            }
        }
        return porf;
    }
}
