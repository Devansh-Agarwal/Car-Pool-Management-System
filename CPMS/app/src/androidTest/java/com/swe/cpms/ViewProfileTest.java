package com.swe.cpms;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class ViewProfileTest {

    @Rule
    public ActivityTestRule<ViewProfile> viewProfileActivityTestRule = new ActivityTestRule<ViewProfile>(ViewProfile.class);
    private  ViewProfile mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = viewProfileActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    @Test
    public void onCreate() {
        View view1 = mActivity.findViewById(R.id.view_name);
        View view2 = mActivity.findViewById(R.id.view_email);
        View view3 = mActivity.findViewById(R.id.view_Age);
        View view4 = mActivity.findViewById(R.id.view_gender);
        View view5 = mActivity.findViewById(R.id.view_dl_no);
        View view6 = mActivity.findViewById(R.id.view_v_no);
        View view7 = mActivity.findViewById(R.id.view_rating);
        View view8 = mActivity.findViewById(R.id.btn_update_profile);
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
        View view1 = mActivity.findViewById(R.id.view_name);
        View view2 = mActivity.findViewById(R.id.view_email);
        View view3 = mActivity.findViewById(R.id.view_Age);
        View view4 = mActivity.findViewById(R.id.view_gender);
        View view5 = mActivity.findViewById(R.id.view_dl_no);
        View view6 = mActivity.findViewById(R.id.view_v_no);
        View view7 = mActivity.findViewById(R.id.view_rating);
        View view8 = mActivity.findViewById(R.id.btn_update_profile);
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
        View view1 = mActivity.findViewById(R.id.view_name);
        View view2 = mActivity.findViewById(R.id.view_email);
        View view3 = mActivity.findViewById(R.id.view_Age);
        View view4 = mActivity.findViewById(R.id.view_gender);
        View view5 = mActivity.findViewById(R.id.view_dl_no);
        View view6 = mActivity.findViewById(R.id.view_v_no);
        View view7 = mActivity.findViewById(R.id.view_rating);
        View view8 = mActivity.findViewById(R.id.btn_update_profile);
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
