package org.mariusconstantin.dashlanetest.activities.mainactivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasValue;
import static org.junit.Assert.assertNotNull;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mariusconstantin.dashlanetest.MockDataProdiver;
import org.mariusconstantin.dashlanetest.R;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;


/**
 * Created by MConstantin on 1/4/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    private String mTextListItemTitle;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityIntentsTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() {
        mTextListItemTitle = MockDataProdiver.MOCKED_DATA.get(0).getTitle();
    }

    /**
     * A custom {@link Matcher} which matches an item in a {@link RecyclerView} by its text.
     * <p/>
     * <p/>
     * View constraints:
     * <ul>
     * <li>View must be a child of a {@link RecyclerView}
     * <ul>
     *
     * @param itemText the text to match
     * @return Matcher that matches text in the given view
     */
    private Matcher<View> withItemText(final String itemText) {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA RV with text " + itemText);
            }
        };
    }

    public Matcher<? super View> hasImage(final int resourceId) {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("has image a resource with id as bitmap: ");
                description.appendValue(resourceId);
            }

            @Override
            public boolean matchesSafely(ImageView view) {
                return bitmapEquals(getBitmap(view.getDrawable()), getBitmap(view.getResources().getDrawable(R.mipmap.ic_launcher)));
            }
        };
    }

    private Bitmap getBitmap(Drawable drawable) {
        return ((BitmapDrawable) drawable).getBitmap();
    }

    private boolean bitmapEquals(Bitmap bitmap1, Bitmap bitmap2) {
        ByteBuffer buffer1 = ByteBuffer.allocate(bitmap1.getHeight() * bitmap1.getRowBytes());
        bitmap1.copyPixelsToBuffer(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(bitmap2.getHeight() * bitmap2.getRowBytes());
        bitmap2.copyPixelsToBuffer(buffer2);

        return Arrays.equals(buffer1.array(), buffer2.array());
    }

    @Test
    public void checkListFragmentState() {
        onView(ViewMatchers.withId(R.id.fragment_container)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void checkListItemClick() {
        onView(withItemText(mTextListItemTitle)).perform(click());
        onView(withId(android.R.id.text1)).check(matches(isDisplayed()));
        onView(withId(android.R.id.text1)).check(matches(withText(mTextListItemTitle)));
        onView(withId(R.id.image)).check(matches(isDisplayed()));
        onView(withId(R.id.image)).check(matches(withContentDescription(R.string.web_site_logo_desc)));
        onView(withId(R.id.image)).check(matches(hasImage(R.mipmap.ic_launcher)));
    }
}
