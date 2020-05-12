package com.swe.cpms;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private  MainActivity mActivity = null;
    Instrumentation.ActivityMonitor monitorHelp = InstrumentationRegistry.getInstrumentation().addMonitor(HelpActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitorRider = InstrumentationRegistry.getInstrumentation().addMonitor(RideRequestActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitorDriver = InstrumentationRegistry.getInstrumentation().addMonitor(RideOfferActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitorMyRides = InstrumentationRegistry.getInstrumentation().addMonitor(MyRides.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitorVP = InstrumentationRegistry.getInstrumentation().addMonitor(ViewProfile.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitorSOS = InstrumentationRegistry.getInstrumentation().addMonitor(SosActivity.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        mActivity = mainActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    @Test
    public void testLaunchOfHelpActivity()
    {
        assertNotNull(mActivity.findViewById(R.id.view_help_centre));
        onView(withId(R.id.view_help_centre)).perform(click());
        Activity tempActivity = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorHelp, 5000);
        assertNotNull(tempActivity);
        tempActivity.finish();
    }

    @Test
    public void testLaunchOfRideRequestActivity()
    {
        assertNotNull(mActivity.findViewById(R.id.Rider));
        onView(withId(R.id.Rider)).perform(click());
        Activity tempActivity = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorRider, 5000);
        assertNotNull(tempActivity);
        tempActivity.finish();
    }

    @Test
    public void testLaunchOfRideOfferActivity()
    {
        assertNotNull(mActivity.findViewById(R.id.Driver));
        onView(withId(R.id.Driver)).perform(click());
        Activity tempActivity = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorDriver, 5000);
        assertNotNull(tempActivity);
        tempActivity.finish();
    }

    @Test
    public void testLaunchOfMyRidesActivity()
    {
        assertNotNull(mActivity.findViewById(R.id.my_rides));
        onView(withId(R.id.my_rides)).perform(click());
        Activity tempActivity = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorMyRides, 5000);
        assertNotNull(tempActivity);
        tempActivity.finish();
    }

    @Test
    public void testLaunchOfViewProfileActivity()
    {
        assertNotNull(mActivity.findViewById(R.id.view_profile));
        onView(withId(R.id.view_profile)).perform(click());
        Activity tempActivity = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorVP, 5000);
        assertNotNull(tempActivity);
        tempActivity.finish();
    }

    @Test
    public void testLaunchOfSOSActivity()
    {
        assertNotNull(mActivity.findViewById(R.id.view_sos));
        onView(withId(R.id.view_sos)).perform(click());
        Activity tempActivity = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorSOS, 5000);
        assertNotNull(tempActivity);
        tempActivity.finish();
    }


    @Test
    public void onCreate() {
        View view1 = mActivity.findViewById(R.id.Rider);
        View view2 = mActivity.findViewById(R.id.Driver);
        View view3 = mActivity.findViewById(R.id.my_rides);
        View view4 = mActivity.findViewById(R.id.view_profile);
        View view5 = mActivity.findViewById(R.id.view_upcoming);
        View view6 = mActivity.findViewById(R.id.view_help_centre);
        View view7 = mActivity.findViewById(R.id.view_sos);
        View view8 = mActivity.findViewById(R.id.log_out);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
        assertNotNull(view6);
        assertNotNull(view7);
        assertNotNull(view8);
    }

    @Test
    public void onResume() {
        View view1 = mActivity.findViewById(R.id.Rider);
        View view2 = mActivity.findViewById(R.id.Driver);
        View view3 = mActivity.findViewById(R.id.my_rides);
        View view4 = mActivity.findViewById(R.id.view_profile);
        View view5 = mActivity.findViewById(R.id.view_upcoming);
        View view6 = mActivity.findViewById(R.id.view_help_centre);
        View view7 = mActivity.findViewById(R.id.view_sos);
        View view8 = mActivity.findViewById(R.id.log_out);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
        assertNotNull(view6);
        assertNotNull(view7);
        assertNotNull(view8);
    }

    @Test
    public void onPause() {
        View view1 = mActivity.findViewById(R.id.Rider);
        View view2 = mActivity.findViewById(R.id.Driver);
        View view3 = mActivity.findViewById(R.id.my_rides);
        View view4 = mActivity.findViewById(R.id.view_profile);
        View view5 = mActivity.findViewById(R.id.view_upcoming);
        View view6 = mActivity.findViewById(R.id.view_help_centre);
        View view7 = mActivity.findViewById(R.id.view_sos);
        View view8 = mActivity.findViewById(R.id.log_out);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
        assertNotNull(view6);
        assertNotNull(view7);
        assertNotNull(view8);
    }
}
