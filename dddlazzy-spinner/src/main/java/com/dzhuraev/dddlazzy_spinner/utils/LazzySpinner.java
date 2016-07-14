package com.dzhuraev.dddlazzy_spinner.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.dzhuraev.dddlazzy_spinner.R;

/**
 * Created by root on 6/18/16.
 */
public class LazzySpinner extends TextView {

    /**
     * This is LazzySpinner based on a TextView with HintText and HintTextColor
     * LazzySpinner has a view as {@link Spinner} that displays one child at a time and lets the user pick among them.
     * The items in the Spinner come from the {@link Adapter}
     */

    private Context mContext;
    private ListAdapter mAdapter;
    private AdapterView.OnItemClickListener mOnItemClicker;
    private OnClickListener mOnClickListener;
    private int mWindowWidth;
    private int mWindowHeight;
    private WindowManager mWindowManager;
    private DisplayMetrics mDisplayMetrics;
    private LazzyPopup mPopup;
    private Activity mActivity;
    private boolean windowWidthSet = false;
    private boolean windowHeightSet = false;

    public LazzySpinner(Context context) {
        super(context);
        mContext = context;
        initAttrs();
    }

    public LazzySpinner(Context context, AttributeSet attrs) {
        super(new ContextThemeWrapper(context, R.style.Corner), attrs);
        mContext = context;
        initAttrs();
    }

    public LazzySpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(new ContextThemeWrapper(context, R.style.Corner), attrs, defStyleAttr);
        mContext = context;
        initAttrs();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LazzySpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initAttrs();
    }

    public void setSpinnerHintText(CharSequence hint) {
        setHint(hint);
    }

    public void setSpinnerHintColor(int color) {
        setHintTextColor(color);
    }

    public void withAdapter(ListAdapter listAdapter) {
        mAdapter = listAdapter;
        mPopup.setAdapter(mAdapter);
    }

    public void withActivity(Activity activity) {
        this.mActivity = activity;
        getWindowDimens();
    }

    public void show() {
        this.mPopup.show();
    }

    public void dismiss() {
        mPopup.dismiss();
    }

    public void withItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mOnItemClicker = listener;
    }

    public void withClickListener(OnClickListener listener) {
        this.mOnClickListener = listener;
    }

    public void setDialogWidth(int dialogWidth) {
        windowWidthSet = true;
        int mWidth;

        if (dialogWidth == LazzyDimens.HALF_SIZE) {
            mWidth = mWindowWidth / 2;
        } else if (dialogWidth == LazzyDimens.FULL_SIZE) {
            mWidth = mWindowWidth;
        } else {
            mWidth = dialogWidth;
        }
        mPopup.setContentWidth(mWidth);
        mPopup.setWidth(mWidth);
    }

    public void setDialogHeight(int dialogHeight) {
        windowHeightSet = true;
        int mHeight;

        if (dialogHeight == LazzyDimens.HALF_SIZE) {
            mHeight = mWindowHeight / 2;
        } else if (dialogHeight == LazzyDimens.FULL_SIZE) {
            mHeight = mWindowHeight;
        } else {
            mHeight = dialogHeight;
        }
        mPopup.setHeight(mHeight);
    }

    private void getWindowDimens() {
        if (mActivity != null) {
            mWindowManager = mActivity.getWindowManager();
            mWindowManager.getDefaultDisplay().getMetrics(mDisplayMetrics);
            mWindowHeight = mDisplayMetrics.heightPixels;
            mWindowWidth = mDisplayMetrics.widthPixels;
        }
    }

    private class LazzyPopup extends android.widget.ListPopupWindow {
        public LazzyPopup(
                Context context) {
            super(context);
            setAnchorView(LazzySpinner.this);
            setModal(true);
            setInputMethodMode(android.widget.ListPopupWindow.INPUT_METHOD_NOT_NEEDED);
            setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setSelection(position);
                    LazzySpinner.this.setText(getListView().getAdapter().getItem(position).toString());
                    if (mOnItemClicker != null) {
                        mOnItemClicker.onItemClick(parent, view, position, id);
                    }
                    dismiss();
                }
            });
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
    }

    private interface BaseLazzyPopup {
        void setAdapter(ListAdapter adapter);

        void show();

        void dismiss();

        void setContentWidth(int width);

        void setHeight(int height);
    }

    private void initAttrs() {
        mPopup = new LazzyPopup(mContext);
        this.setOnClickListener(clicker);
        mDisplayMetrics = new DisplayMetrics();
    }

    OnClickListener clicker = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v);
            }
            show();
        }
    };
}
