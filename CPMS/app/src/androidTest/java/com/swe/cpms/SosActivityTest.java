package com.swe.cpms;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class SosActivityTest {

    @Rule
    public ActivityTestRule<SosActivity> SosActivityActivityTestRule = new ActivityTestRule<SosActivity>(SosActivity.class);
    private  SosActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = SosActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    @Test
    public void onCreate() {
        View view1 = mActivity.findViewById(R.id.number_1);
        View view2 = mActivity.findViewById(R.id.number_2);
        View view3 = mActivity.findViewById(R.id.submit_number);
        View view4 = mActivity.findViewById(R.id.cancel_number);
        View view5 = mActivity.findViewById(R.id.load_number);
        View view6 = mActivity.findViewById(R.id.remove_number);
        View view7 = mActivity.findViewById(R.id.send_sos);
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
        View view1 = mActivity.findViewById(R.id.number_1);
        View view2 = mActivity.findViewById(R.id.number_2);
        View view3 = mActivity.findViewById(R.id.submit_number);
        View view4 = mActivity.findViewById(R.id.cancel_number);
        View view5 = mActivity.findViewById(R.id.load_number);
        View view6 = mActivity.findViewById(R.id.remove_number);
        View view7 = mActivity.findViewById(R.id.send_sos);
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
        View view1 = mActivity.findViewById(R.id.number_1);
        View view2 = mActivity.findViewById(R.id.number_2);
        View view3 = mActivity.findViewById(R.id.submit_number);
        View view4 = mActivity.findViewById(R.id.cancel_number);
        View view5 = mActivity.findViewById(R.id.load_number);
        View view6 = mActivity.findViewById(R.id.remove_number);
        View view7 = mActivity.findViewById(R.id.send_sos);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
        assertNotNull(view6);
        assertNotNull(view7);
    }
}
