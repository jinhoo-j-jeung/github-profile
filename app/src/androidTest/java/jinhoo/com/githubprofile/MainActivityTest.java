package jinhoo.com.githubprofile;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static jinhoo.com.githubprofile.EspressoViewProfileActivityTest.childAtPosition;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.test.espresso.Espresso;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import jinhoo.com.githubprofile.fragments.RepositoryFragment;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for Profile Fragment
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

  @Rule
  public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
  private MainActivity mActivity = null;

  private final Instrumentation.ActivityMonitor monitor1 = getInstrumentation()
      .addMonitor(MainActivityTest.class.getName(), null, false);

  @Before
  public void setUp() {
    mActivity = mActivityTestRule.getActivity();
  }

  @Test
  public void layoutTest() {
    BottomNavigationView navigationView = mActivity.findViewById(R.id.main_navbar);
    FrameLayout frameLayout = mActivity.findViewById(R.id.main_fl);

    assertNotNull(navigationView);
    assertNotNull(frameLayout);
  }

  @Test 
  public void DataTest1() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.navbar_profile),
            withContentDescription("Profile"),
            childAtPosition(childAtPosition(withId(R.id.main_navbar), 0), 0),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    SharedPreferences mSharedPreferences = mActivity.getSharedPreferences("User Github Information", Context.MODE_PRIVATE);
    assertNotNull(mSharedPreferences);

    assertEquals("Jihoo Jeung", mSharedPreferences.getString("name", ""));
    assertEquals("https://avatars2.githubusercontent.com/u/31013910?v=4", mSharedPreferences.getString("avatar_url", ""));
    assertEquals("Hi! I am a computer science student in University of Illinois.", mSharedPreferences.getString("bio", ""));
    assertEquals("https://www.linkedin.com/in/jinhoo-jeung-258767150/", mSharedPreferences.getString("website", ""));
    assertEquals("5", mSharedPreferences.getString("repo_count", ""));
  }

  @Test
  public void RepoFragmentTest() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.navbar_repo), withContentDescription("Repo"),
            childAtPosition(childAtPosition(withId(R.id.main_navbar), 0), 1),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction relativeLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.repo_rv), childAtPosition(withId(R.id.repo_rl), 0)), 0),
            isDisplayed()));
    relativeLayout.check(matches(isDisplayed()));
  }
}
