package jinhoo.com.githubprofile;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityDataTest {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void mainActivityDataTest() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.navbar_profile), withContentDescription("Profile"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.main_navbar),
                    0),
                0),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction textView = onView(
        allOf(withId(R.id.profile_name_tv), withText("Jihoo Jeung"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.profile_cv_6),
                    0),
                1),
            isDisplayed()));
    textView.check(matches(withText("Jihoo Jeung")));

    ViewInteraction textView2 = onView(
        allOf(withId(R.id.profile_bio_tv),
            withText("Hi! I am a computer science student in University of Illinois."),
            childAtPosition(
                childAtPosition(
                    withId(R.id.profile_cv_3),
                    0),
                1),
            isDisplayed()));
    textView2
        .check(matches(withText("Hi! I am a computer science student in University of Illinois.")));

    ViewInteraction textView3 = onView(
        allOf(withId(R.id.profile_repo_tv), withText("5"),
            childAtPosition(
                allOf(withId(R.id.profile_repo_cv),
                    childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                        1)),
                0),
            isDisplayed()));
    textView3.check(matches(withText("5")));

    ViewInteraction textView4 = onView(
        allOf(withId(R.id.profile_repo_tv), withText("5"),
            childAtPosition(
                allOf(withId(R.id.profile_repo_cv),
                    childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                        1)),
                0),
            isDisplayed()));
    textView4.check(matches(withText("5")));

    ViewInteraction textView5 = onView(
        allOf(withId(R.id.profile_follower_tv), withText("1"),
            childAtPosition(
                allOf(withId(R.id.profile_follower_cv),
                    childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                        3)),
                0),
            isDisplayed()));
    textView5.check(matches(withText("1")));

    ViewInteraction textView6 = onView(
        allOf(withId(R.id.profile_following_tv), withText("3"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                    5),
                0),
            isDisplayed()));
    textView6.check(matches(withText("3")));

    ViewInteraction textView7 = onView(
        allOf(withId(R.id.profile_username_tv), withText("@JinhooJeung"),
            childAtPosition(
                allOf(withId(R.id.profile_rl_2),
                    childAtPosition(
                        withId(R.id.profile_rl_1),
                        1)),
                0),
            isDisplayed()));
    textView7.check(matches(withText("@JinhooJeung")));

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
