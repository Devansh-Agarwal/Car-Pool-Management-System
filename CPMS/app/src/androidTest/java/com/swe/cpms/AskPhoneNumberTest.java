package com.swe.cpms;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class AskPhoneNumberTest {

    @Rule
    public ActivityTestRule<AskPhoneNumber> askPhoneNumberActivityTestRule = new ActivityTestRule<AskPhoneNumber>(AskPhoneNumber.class);
    private  AskPhoneNumber mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = askPhoneNumberActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    @Test
    public void onCreate() {
        View view1 = mActivity.findViewById(R.id.relativeLayout);
        View view2 = mActivity.findViewById(R.id.v_shape);
        View view3 = mActivity.findViewById(R.id.imageView);
        View view4 = mActivity.findViewById(R.id.container);
        View view5 = mActivity.findViewById(R.id.textView);
        View view6 = mActivity.findViewById(R.id.editTextMobile);
        View view7 = mActivity.findViewById(R.id.buttonContinue);
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
        View view1 = mActivity.findViewById(R.id.relativeLayout);
        View view2 = mActivity.findViewById(R.id.v_shape);
        View view3 = mActivity.findViewById(R.id.imageView);
        View view4 = mActivity.findViewById(R.id.container);
        View view5 = mActivity.findViewById(R.id.textView);
        View view6 = mActivity.findViewById(R.id.editTextMobile);
        View view7 = mActivity.findViewById(R.id.buttonContinue);
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
        View view1 = mActivity.findViewById(R.id.relativeLayout);
        View view2 = mActivity.findViewById(R.id.v_shape);
        View view3 = mActivity.findViewById(R.id.imageView);
        View view4 = mActivity.findViewById(R.id.container);
        View view5 = mActivity.findViewById(R.id.textView);
        View view6 = mActivity.findViewById(R.id.editTextMobile);
        View view7 = mActivity.findViewById(R.id.buttonContinue);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
        assertNotNull(view6);
        assertNotNull(view7);
    }
}
