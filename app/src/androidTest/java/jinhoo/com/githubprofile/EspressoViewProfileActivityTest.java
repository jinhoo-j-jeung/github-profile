package jinhoo.com.githubprofile;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EspressoViewProfileActivityTest {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void viewProfileActivityTest() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.navbar_profile), withContentDescription("Profile"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.main_navbar),
                    0),
                0),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction bottomNavigationItemView2 = onView(
        allOf(withId(R.id.navbar_follower), withContentDescription("Followers"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.main_navbar),
                    0),
                3),
            isDisplayed()));
    bottomNavigationItemView2.perform(click());

    ViewInteraction imageView = onView(
        allOf(childAtPosition(
            childAtPosition(
                withClassName(is("android.support.v7.widget.CardView")),
                0),
            0),
            isDisplayed()));
    imageView.perform(click());

    ViewInteraction bottomNavigationItemView3 = onView(
        allOf(withId(R.id.navbar_profile), withContentDescription("Profile"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.profileview_navbar),
                    0),
                0),
            isDisplayed()));
    bottomNavigationItemView3.perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Min Seok Choi's Github"),
            childAtPosition(
                allOf(withId(R.id.action_bar),
                    childAtPosition(
                        withId(R.id.action_bar_container),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Min Seok Choi's Github")));

    ViewInteraction textView2 = onView(
        allOf(withId(R.id.profile_name_tv), withText("Min Seok Choi"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.profile_cv_6),
                    0),
                1),
            isDisplayed()));
    textView2.check(matches(withText("Min Seok Choi")));

    ViewInteraction textView3 = onView(
        allOf(withId(R.id.profile_bio_tv), withText("let's git it"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.profile_cv_3),
                    0),
                1),
            isDisplayed()));
    textView3.check(matches(withText("let's git it")));

  }

  public static Matcher<View> childAtPosition(
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
