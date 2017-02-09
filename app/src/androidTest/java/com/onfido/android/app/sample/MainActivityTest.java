package com.onfido.android.app.sample;

import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiObjectNotFoundException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.onfido.android.app.sample.R.string.bank_instructions;
import static com.onfido.android.app.sample.R.string.label_doc_type_driving_license;
import static com.onfido.android.app.sample.TestUtilities.waitFor;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;



/**
 * Created by jonathantaylor on 12/01/2017.
 */

@RunWith(AndroidJUnit4.class)
@MediumTest
public class MainActivityTest {

    @Rule public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void intentionallyFailingTest() {
        onView(withText(bank_instructions))
                .check(matches(isDisplayed()))
                .check(matches(withText(containsString("STRING THAT SHOULD NOT BE PRESENT"))));
    }

    @Test
    public void shouldBeAbleToLaunchMainScreen() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));

    }

    @Test
    public void shouldBeAbleToLaunchFromSignUpScreenFlow() throws UiObjectNotFoundException {
        onView(withId(R.id.tv_signup)).perform(click());
        TestUtilities.clickPermissionsIfNeeded(TestUtilities.GRANT_BUTTON_INDEX);
        onView(withId(R.id.tv_title))
                .check(matches(isDisplayed()))
                // if we add localization, we'll want not to verify the text
                .check(matches(withText("Identity verification")));
    }

    @Test
    public void shouldBeAbleToLaunchFromAccountScreenFlow() throws UiObjectNotFoundException {
        onView(withId(R.id.tv_account)).perform(click());
        TestUtilities.clickPermissionsIfNeeded(TestUtilities.GRANT_BUTTON_INDEX);
        onView(withId(R.id.tv_title))
                .check(matches(isDisplayed()))
                // if we add localization, we'll want not to verify the text
                .check(matches(withText("Identity verification")));
    }

    @Test
    public void shouldBeAbleToLaunchCustomScreenFlow() throws UiObjectNotFoundException {
        onView(withId(R.id.tv_custom_flow)).perform(click());
        TestUtilities.clickPermissionsIfNeeded(TestUtilities.GRANT_BUTTON_INDEX);
        onView(withId(R.id.tv_message))
                .check(matches(isDisplayed()))
                // if we add localization, we'll want not to verify the text
                .check(matches(withText(containsString("custom"))));
    }

    @Test
    public void shouldBeAbleToLaunchCustomFaceOnlyScreenFlow() throws UiObjectNotFoundException {
        onView(withId(R.id.tv_custom_flow_options)).perform(click());
        TestUtilities.clickPermissionsIfNeeded(TestUtilities.GRANT_BUTTON_INDEX);
        onView(withId(R.id.tv_message))
                .check(matches(isDisplayed()))
                // if we add localization, we'll want not to verify the text
                .check(matches(withText(containsString("face"))));
    }

    @Test
    public void shouldGetAnErrorIfTheresNoDucumentFoundFromSignUpScreenFlow() throws UiObjectNotFoundException {
        onView(withId(R.id.tv_signup)).perform(click());
        TestUtilities.clickPermissionsIfNeeded(TestUtilities.GRANT_BUTTON_INDEX);

        onView(allOf(withId(R.id.tv_title), withText("Identity verification"), isDisplayed()));

        onView(isRoot()).perform(waitFor(5000));

        ViewInteraction inititateButton = onView(allOf(withId(R.id.bt_go), isDisplayed(), isClickable()));
        inititateButton.perform(click());

        ViewInteraction docTypeListItem = onView(allOf(withText(label_doc_type_driving_license), isDisplayed(), isEnabled()));
        docTypeListItem.perform(click());

        ViewInteraction captureButton = onView(allOf(withId(R.id.bt_capture), isDisplayed(), isEnabled()));
        captureButton.perform(click());

//        onView(withText("Loading.  Please wait."))
//                .check(matches(isDisplayed()));

        onView(isRoot()).perform(waitFor(5000));
        onView(withId(R.id.alertTitle)).check(matches(isDisplayed()));
    }





}
