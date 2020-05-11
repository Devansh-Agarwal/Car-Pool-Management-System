package com.swe.cpms;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private  MainActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mainActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
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