package com.onfido.android.app.sample;

import android.app.Instrumentation;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isJavascriptEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.onfido.android.app.sample.R.id.bt_capture;
import static com.onfido.android.app.sample.R.id.bt_go;
import static com.onfido.android.app.sample.R.string.bank_instructions;
import static com.onfido.android.app.sample.R.string.label_doc_type_driving_license;
import static com.onfido.android.app.sample.R.string.title_document_selection;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by jonathantaylor on 12/01/2017.
 */

@RunWith(AndroidJUnit4.class)
public class DemoActivityTest {

    private static final int PERMISSIONS_DIALOG_DELAY = 3000;
    private static final int GRANT_BUTTON_INDEX = 1;
    private static final int DENY_BUTTON_INDEX = 0;

    CountingIdlingResource idlingResource = new CountingIdlingResource("DATA LOADER");

    @Rule public final ActivityTestRule<DemoActivity> main = new ActivityTestRule<>(DemoActivity.class);

    @Test
    public void shouldBeAbleToLaunchMainScreen() {
        onView(withText(bank_instructions))
                .check(matches(isDisplayed()))
                .check(matches(withText(containsString("bank"))));

    }

    @Test
    public void firstTimeUserFindCameraAuthorization() {
        onView(withText(bank_instructions)).check(matches(not(isJavascriptEnabled())));
        onView(withId(R.id.next)).perform(click());
        this.clickPermissionsIfNeeded(DENY_BUTTON_INDEX);

    }

    @Test
    public void beginWorkflow() {
        onView(withText(bank_instructions)).check(matches(isDisplayed())).check(matches(withText(containsString("bank"))));
        onView(withId(R.id.next)).check(matches(withText(containsString("next")))).perform(click());
        onView(withId(bt_go)).check(matches(withText(containsString("Start")))).perform(click());
        onView(withText(title_document_selection)).check(matches(withText(containsString("document")))).perform(click());
        onView(withText(label_doc_type_driving_license)).perform(click());
        onView(withId(bt_capture)).perform(click());
        // need to change this to use counting idling resource
        onView(isRoot()).perform(waitFor(5000));
        onView(withId(R.id.tv_message)).check(matches(withText(containsString("capture"))));
        onView(allOf(withId(R.id.bt_go),withText("Next"))).perform(click());
        onView(withId(bt_capture)).perform(click());
        onView(isRoot()).perform(waitFor(5000));
        onView(allOf(withId(R.id.bt_go),withText("Start Check"))).perform(click());
        onView(isRoot()).perform(waitFor(5000));
        onView(allOf(withId(R.id.bt_go),withText("Get Started"))).perform(click());
        onView(withText("SORT CODE")).check(matches(isDisplayed()));



    }

    private void clickPermissionsIfNeeded(int BUTTON_INDEX)  {
        if (Build.VERSION.SDK_INT >= 23) {
            UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            UiObject permissionsButton = device.findObject(new UiSelector()
                    .clickable(true)
                    .checkable(false)
                    .index(BUTTON_INDEX));
            if (permissionsButton.exists()) {
                try {
                    permissionsButton.click();
                } catch (UiObjectNotFoundException e) {
                    System.out.println("There is no permissions dialog to interact with");
                }
            }
        }
    }

    private void dismissRateRequest(int BUTTON_INDEX)  {
        if (Build.VERSION.SDK_INT >= 23) {
            Instrumentation instr = InstrumentationRegistry.getInstrumentation();
            UiDevice device = UiDevice.getInstance(instr);
            UiObject permissionsButton = device.findObject(new UiSelector()
                    .clickable(true)
                    .checkable(false)
                    .index(BUTTON_INDEX));
            if (permissionsButton.exists()) {
                try {
                    permissionsButton.click();
                } catch (UiObjectNotFoundException e) {
                    System.out.println("There is no permissions dialog to interact with");
                }
            }
        }
    }

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

}
