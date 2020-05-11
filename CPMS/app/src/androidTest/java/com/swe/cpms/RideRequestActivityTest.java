package com.swe.cpms;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class RideRequestActivityTest {

    @Rule
    public ActivityTestRule<RideRequestActivity> rideRequestActivityActivityTestRule = new ActivityTestRule<RideRequestActivity>(RideRequestActivity.class);
    private  RideRequestActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = rideRequestActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    @Test
    public void onCreate() {
        View view1 = mActivity.findViewById(R.id.rSource);
        View view2 = mActivity.findViewById(R.id.rDestination);
        View view3 = mActivity.findViewById(R.id.rDate);
        View view4 = mActivity.findViewById(R.id.rTime);
        View view5 = mActivity.findViewById(R.id.rSeats);
        View view6 = mActivity.findViewById(R.id.find);
        View view7 = mActivity.findViewById(R.id.rMap);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
        assertNotNull(view6);
        assertNotNull(view7);
    }

    @Test
    public void onResume() {
        View view1 = mActivity.findViewById(R.id.rSource);
        View view2 = mActivity.findViewById(R.id.rDestination);
        View view3 = mActivity.findViewById(R.id.rDate);
        View view4 = mActivity.findViewById(R.id.rTime);
        View view5 = mActivity.findViewById(R.id.rSeats);
        View view6 = mActivity.findViewById(R.id.find);
        View view7 = mActivity.findViewById(R.id.rMap);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
        assertNotNull(view6);
        assertNotNull(view7);
    }

    @Test
    public void onPause() {
        View view1 = mActivity.findViewById(R.id.rSource);
        View view2 = mActivity.findViewById(R.id.rDestination);
        View view3 = mActivity.findViewById(R.id.rDate);
        View view4 = mActivity.findViewById(R.id.rTime);
        View view5 = mActivity.findViewById(R.id.rSeats);
        View view6 = mActivity.findViewById(R.id.find);
        View view7 = mActivity.findViewById(R.id.rMap);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
        assertNotNull(view6);
        assertNotNull(view7);
    }
}
