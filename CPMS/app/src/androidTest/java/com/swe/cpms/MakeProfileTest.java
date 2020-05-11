package com.swe.cpms;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class MakeProfileTest {

    @Rule
    public ActivityTestRule<MakeProfile> makeProfileActivityTestRule = new ActivityTestRule<MakeProfile>(MakeProfile.class);
    private  MakeProfile mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = makeProfileActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    @Test
    public void onCreate() {
        View view1 = mActivity.findViewById(R.id.view_Flag_check);
        View view2 = mActivity.findViewById(R.id.name);
        View view3 = mActivity.findViewById(R.id.email);
        View view4 = mActivity.findViewById(R.id.age);
        View view5 = mActivity.findViewById(R.id.driving_license);
        View view6 = mActivity.findViewById(R.id.vehicle_no);
        View view7 = mActivity.findViewById(R.id.radioGroup);
        View view8 = mActivity.findViewById(R.id.radioMale);
        View view9 = mActivity.findViewById(R.id.radioFemale);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
        assertNotNull(view6);
        assertNotNull(view7);
        assertNotNull(view8);
        assertNotNull(view9);
    }

    @Test
    public void onResume() {
        View view1 = mActivity.findViewById(R.id.view_Flag_check);
        View view2 = mActivity.findViewById(R.id.name);
        View view3 = mActivity.findViewById(R.id.email);
        View view4 = mActivity.findViewById(R.id.age);
        View view5 = mActivity.findViewById(R.id.driving_license);
        View view6 = mActivity.findViewById(R.id.vehicle_no);
        View view7 = mActivity.findViewById(R.id.radioGroup);
        View view8 = mActivity.findViewById(R.id.radioMale);
        View view9 = mActivity.findViewById(R.id.radioFemale);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
        assertNotNull(view6);
        assertNotNull(view7);
        assertNotNull(view8);
        assertNotNull(view9);
    }

    @Test
    public void onPause() {
        View view1 = mActivity.findViewById(R.id.view_Flag_check);
        View view2 = mActivity.findViewById(R.id.name);
        View view3 = mActivity.findViewById(R.id.email);
        View view4 = mActivity.findViewById(R.id.age);
        View view5 = mActivity.findViewById(R.id.driving_license);
        View view6 = mActivity.findViewById(R.id.vehicle_no);
        View view7 = mActivity.findViewById(R.id.radioGroup);
        View view8 = mActivity.findViewById(R.id.radioMale);
        View view9 = mActivity.findViewById(R.id.radioFemale);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
        assertNotNull(view6);
        assertNotNull(view7);
        assertNotNull(view8);
        assertNotNull(view9);
    }
}
