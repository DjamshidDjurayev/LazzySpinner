package com.dzhuraev.dddlazzy_spinner.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by root on 6/18/16.
 */
public class LazzySpinner extends TextView {
    /**
     * This is LazzySpinner based on a TextView with HintText and HintTextColor
     * LazzySpinner has a view as {@link Spinner} that displays one child at a time and lets the user pick among them.
     * The items in the Spinner come from the {@link Adapter} associated with
     * this view.
     */

    private Context mContext;
    private ListPopupWindow mPopup;
    private ListAdapter mAdapter;
    private AdapterView.OnItemClickListener mClickListener;
    private OnClickListener mOnClickListener;
    private int windowWidth;
    private int windowHeight;
    private WindowManager windowManager;
    private DisplayMetrics displayMetrics;
    private Activity mActivity;

    public static int HALF_SIZE = 1;
    public static int FULL_SIZE = 2;
    private boolean windowWidthSet = false;
    private boolean windowHeightSet = false;

    public LazzySpinner(Context context) {
        super(context);
        this.mContext = context;
        setPrefs();
    }


    public LazzySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setPrefs();
    }

    public LazzySpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setPrefs();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LazzySpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        setPrefs();
    }

    public void setSpinnerHintText(@Nullable CharSequence hint) {
        setHint(hint);
    }

    public void setSpinnerHintColor(@Nullable int color) {
        setHintTextColor(color);
    }

    public void withAdapter(ListAdapter listAdapter) {
        this.mAdapter = listAdapter;
        this.mPopup.setAdapter(mAdapter);
    }

    private int measureContentWidth() {

        ViewGroup mMeasureParent = null;
        int maxWidth = 0;
        View itemView = null;
        int itemType = 0;

        if (mAdapter != null) {

            final ListAdapter adapter = mAdapter;
            final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), View.MeasureSpec.UNSPECIFIED);
            final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), View.MeasureSpec.UNSPECIFIED);
            final int count = adapter.getCount();

            for (int i = 0; i < count; i++) {
                final int positionType = adapter.getItemViewType(i);
                if (positionType != itemType) {
                    itemType = positionType;
                    itemView = null;
                }

                if (mMeasureParent == null) {
                    mMeasureParent = new FrameLayout(mContext);
                    mMeasureParent.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                }

                itemView = adapter.getView(i, itemView, mMeasureParent);
                itemView.measure(widthMeasureSpec, heightMeasureSpec);

                final int itemWidth = itemView.getMeasuredWidth();

                if (itemWidth > maxWidth) {
                    maxWidth = itemWidth;
                }
            }
        }
        return maxWidth / 2;
    }

    public void show() {
        getSettings();
        mPopup.show();
    }

    private void getSettings() {
        mPopup.setModal(true);
        mPopup.setAnchorView(this);
        mPopup.setInputMethodMode(ListPopupWindow.INPUT_METHOD_NOT_NEEDED);

        if (!windowWidthSet) { // by default full size
            mPopup.setContentWidth(windowWidth);
            mPopup.setWidth(windowWidth);
        }
    }

    public void dismiss() {
        mPopup.dismiss();
    }

    public void withItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mClickListener = listener;
        mPopup.setOnItemClickListener(listener);
    }

    public void withClickListener(OnClickListener listener) {
        this.mOnClickListener = listener;
        this.setOnClickListener(mOnClickListener);
    }

    //
    public void setDialogWidth(int dialogWidth) {
        windowWidthSet = true;
        int mWidth;

        if (dialogWidth == HALF_SIZE) {
            mWidth = windowWidth / 2;
        } else if (dialogWidth == FULL_SIZE) {
            mWidth = windowWidth;
        } else {
            mWidth = dialogWidth;
        }
        mPopup.setContentWidth(mWidth);
        mPopup.setWidth(mWidth);
    }

    public void setDialogHeight(int dialogHeight) {
        windowHeightSet = true;
        int mHeight;

        if (dialogHeight == HALF_SIZE) {
            mHeight = windowHeight / 2;
        } else if (dialogHeight == FULL_SIZE) {
            mHeight = windowHeight;
        } else {
            mHeight = dialogHeight;
        }
        mPopup.setHeight(mHeight);
    }

    private void getWindowDimens() {
        if (mActivity != null) {
            this.windowManager = mActivity.getWindowManager();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        this.windowHeight = displayMetrics.heightPixels;
        this.windowWidth = displayMetrics.widthPixels;
    }

    public void withActivity(Activity activity) {
        this.mActivity = activity;
        getWindowDimens();
    }

    private void setPrefs() {
        mPopup = new ListPopupWindow(mContext);
        displayMetrics = new DisplayMetrics();
    }
}
