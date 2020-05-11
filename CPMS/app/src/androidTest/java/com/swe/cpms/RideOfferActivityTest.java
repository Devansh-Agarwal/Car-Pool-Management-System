package com.swe.cpms;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class RideOfferActivityTest {

    @Rule
    public ActivityTestRule<RideOfferActivity> rideOfferActivityTestRule = new ActivityTestRule<RideOfferActivity>(RideOfferActivity.class);
    private  RideOfferActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = rideOfferActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    @Test
    public void onCreate() {
        View view1 = mActivity.findViewById(R.id.dSource);
        View view2 = mActivity.findViewById(R.id.dDestination);
        View view3 = mActivity.findViewById(R.id.dDate);
        View view4 = mActivity.findViewById(R.id.dTime);
        View view5 = mActivity.findViewById(R.id.dSeats);
        View view6 = mActivity.findViewById(R.id.offer);
        View view7 = mActivity.findViewById(R.id.dMap);
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
        View view1 = mActivity.findViewById(R.id.dSource);
        View view2 = mActivity.findViewById(R.id.dDestination);
        View view3 = mActivity.findViewById(R.id.dDate);
        View view4 = mActivity.findViewById(R.id.dTime);
        View view5 = mActivity.findViewById(R.id.dSeats);
        View view6 = mActivity.findViewById(R.id.offer);
        View view7 = mActivity.findViewById(R.id.dMap);
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
        View view1 = mActivity.findViewById(R.id.dSource);
        View view2 = mActivity.findViewById(R.id.dDestination);
        View view3 = mActivity.findViewById(R.id.dDate);
        View view4 = mActivity.findViewById(R.id.dTime);
        View view5 = mActivity.findViewById(R.id.dSeats);
        View view6 = mActivity.findViewById(R.id.offer);
        View view7 = mActivity.findViewById(R.id.dMap);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
        assertNotNull(view6);
        assertNotNull(view7);
    }
}
