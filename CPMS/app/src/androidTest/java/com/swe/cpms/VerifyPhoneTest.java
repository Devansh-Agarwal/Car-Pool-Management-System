package com.swe.cpms;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class VerifyPhoneTest {

    @Rule
    public ActivityTestRule<VerifyPhone> verifyPhoneActivityTestRule = new ActivityTestRule<VerifyPhone>(VerifyPhone.class);
    private  VerifyPhone mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = verifyPhoneActivityTestRule.getActivity();
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
        View view6 = mActivity.findViewById(R.id.progressbar);
        View view7 = mActivity.findViewById(R.id.editTextCode);
        View view8 = mActivity.findViewById(R.id.buttonSignIn);
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
        View view1 = mActivity.findViewById(R.id.relativeLayout);
        View view2 = mActivity.findViewById(R.id.v_shape);
        View view3 = mActivity.findViewById(R.id.imageView);
        View view4 = mActivity.findViewById(R.id.container);
        View view5 = mActivity.findViewById(R.id.textView);
        View view6 = mActivity.findViewById(R.id.progressbar);
        View view7 = mActivity.findViewById(R.id.editTextCode);
        View view8 = mActivity.findViewById(R.id.buttonSignIn);
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
        View view1 = mActivity.findViewById(R.id.relativeLayout);
        View view2 = mActivity.findViewById(R.id.v_shape);
        View view3 = mActivity.findViewById(R.id.imageView);
        View view4 = mActivity.findViewById(R.id.container);
        View view5 = mActivity.findViewById(R.id.textView);
        View view6 = mActivity.findViewById(R.id.progressbar);
        View view7 = mActivity.findViewById(R.id.editTextCode);
        View view8 = mActivity.findViewById(R.id.buttonSignIn);
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
