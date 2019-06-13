package jinhoo.com.githubprofile;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EspressoMainActivityTest {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void mainActivityTest() {
    ViewInteraction relativeLayout = Espresso.onView(
        Matchers.allOf(childAtPosition(
            childAtPosition(
                ViewMatchers.withId(R.id.main_fl),
                0),
            0),
            ViewMatchers.isDisplayed()));
    relativeLayout.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    ViewInteraction textView = Espresso.onView(
        Matchers.allOf(ViewMatchers.withText("My Github Account"),
            childAtPosition(
                Matchers.allOf(ViewMatchers.withId(R.id.action_bar),
                    childAtPosition(
                        ViewMatchers.withId(R.id.action_bar_container),
                        0)),
                0),
            ViewMatchers.isDisplayed()));
    textView.check(ViewAssertions.matches(ViewMatchers.withText("My Github Account")));

    ViewInteraction frameLayout = Espresso.onView(
        Matchers.allOf(ViewMatchers.withId(R.id.profile_cv_1),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                    0),
                0),
            ViewMatchers.isDisplayed()));
    frameLayout.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    ViewInteraction frameLayout2 = Espresso.onView(
        Matchers.allOf(ViewMatchers.withId(R.id.profile_cv_2),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                    0),
                1),
            ViewMatchers.isDisplayed()));
    frameLayout2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    ViewInteraction frameLayout3 = Espresso.onView(
        Matchers.allOf(ViewMatchers.withId(R.id.profile_cv_6),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                    0),
                2),
            ViewMatchers.isDisplayed()));
    frameLayout3.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    ViewInteraction frameLayout4 = Espresso.onView(
        Matchers.allOf(ViewMatchers.withId(R.id.profile_cv_3),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                    0),
                3),
            ViewMatchers.isDisplayed()));
    frameLayout4.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    ViewInteraction frameLayout5 = Espresso.onView(
        Matchers.allOf(ViewMatchers.withId(R.id.profile_cv_4),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                    0),
                4),
            ViewMatchers.isDisplayed()));
    frameLayout5.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    ViewInteraction frameLayout6 = Espresso.onView(
        Matchers.allOf(ViewMatchers.withId(R.id.profile_cv_5),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                    0),
                5),
            ViewMatchers.isDisplayed()));
    frameLayout6.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    ViewInteraction frameLayout7 = Espresso.onView(
        Matchers.allOf(ViewMatchers.withId(R.id.navbar_profile), ViewMatchers.withContentDescription("Profile"),
            childAtPosition(
                childAtPosition(
                    ViewMatchers.withId(R.id.main_navbar),
                    0),
                0),
            ViewMatchers.isDisplayed()));
    frameLayout7.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

  }

  private static Matcher<View> childAtPosition(
      final Matcher<View> parentMatcher, final int position) {

    return new TypeSafeMatcher<View>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("Child at position " + position + " in parent ");
        parentMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(View view) {
        ViewParent parent = view.getParent();
        return parent instanceof ViewGroup && parentMatcher.matches(parent)
            && view.equals(((ViewGroup) parent).getChildAt(position));
      }
    };
  }
}
