package com.dzhuraev.dddlazzy_spinner.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by root on 7/3/16.
 */
public class BaseSpinner extends TextView {

    Spinner spinner;

    public BaseSpinner(Context context) {
        super(context);
    }

    public BaseSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
