package com.yunbao.beauty.ui.views.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yunbao.beauty.R;

/**
 * Created by cxf on 2018/6/22.
 */

public class TextSeekBarNew extends FrameLayout implements SeekBar.OnSeekBarChangeListener {

    private SeekBar mSeekBar;
//    private TextView mTextView;
    private TextView mProgressVal;
    private Context mContext;
    private String mText;
    private int mProgress;
    private int mMaxProgress;
    private int mMinProgress;
    private OnSeekChangeListener mOnSeekChangeListener;
//    private LinearLayout.LayoutParams layoutParams;

    public TextSeekBarNew(Context context) {
        this(context, null);
    }

    public TextSeekBarNew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextSeekBarNew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.getResources().obtainAttributes(attrs, R.styleable.TextSeekBarNew);
//        mText = ta.getString(R.styleable.TextSeekBar_text2);
        mProgress = ta.getInteger(R.styleable.TextSeekBarNew_progressVal_new, 0);
        mMaxProgress = ta.getInteger(R.styleable.TextSeekBarNew_maxProgress_new, 100);
        mMinProgress = ta.getInteger(R.styleable.TextSeekBarNew_minProgress_new, 0);
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_seek_group_new, this, false);
        mSeekBar = (SeekBar) v.findViewById(R.id.seekBar);
//        mTextView = (TextView) v.findViewById(R.id.text);
        mProgressVal = (TextView) v.findViewById(R.id.progressVal);
        mSeekBar.setMax(mMaxProgress);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mSeekBar.setMin(mMinProgress);
        }
        mSeekBar.setProgress(mProgress);
        mSeekBar.setOnSeekBarChangeListener(this);
        mProgressVal.setText(String.valueOf(mProgress));
        addView(v);
        caculateLeft(mProgressVal, mSeekBar);
    }

    public void setMaxProgress(int mMaxProgress) {
        this.mMaxProgress = mMaxProgress;
        mSeekBar.setMax(mMaxProgress);
//        invalidate();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mProgress = progress;
        mProgressVal.setText(String.valueOf(progress));
        caculateLeft(mProgressVal, mSeekBar);
        if (mOnSeekChangeListener != null && fromUser) {
            mOnSeekChangeListener.onProgressChanged(TextSeekBarNew.this, progress);
        }
    }


    private void caculateLeft(TextView mProgressVal, SeekBar mSeekBar){
//        float textWidth = mProgressVal.getWidth();
//        float left = mSeekBar.getLeft();
//        float max =Math.abs(mSeekBar.getMax());
//        float thumb = DensityUtils.dp2px(mContext,20);
//        float average = (((float) seekBar.getWidth())-2*thumb)/max;
//        float currentProgress = mProgress;
//        float pox = left - textWidth/2 +thumb + average * currentProgress;
//        mProgressVal.setX(pox);

        float mProgressValWidth = mProgressVal.getWidth();
        float measuredWidth = mSeekBar.getMeasuredWidth();
        float mSeekBarLeft = mSeekBar.getLeft();
        float dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.text_seek_bar_margin);
        float realWidth = measuredWidth - dimensionPixelSize * 2;
//        int realWidth = measuredWidth - DensityUtils.dp2px(mContext, 40);
        float average = mProgress / (mMaxProgress * 1.0f);
        float left = mSeekBarLeft + average * realWidth - mProgressValWidth / 2 + dimensionPixelSize;
        mProgressVal.setX(left);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        mProgress = progress;
        if (mSeekBar != null) {
            mSeekBar.setProgress(progress);
        }
    }

    public float getFloatProgress() {
        return mProgress / 100f;
    }

    public void setOnSeekChangeListener(OnSeekChangeListener listener) {
        mOnSeekChangeListener = listener;
    }


    public interface OnSeekChangeListener {
        void onProgressChanged(View view, int progress);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (mSeekBar != null) {
            mSeekBar.setEnabled(enabled);
        }
        if (enabled) {
            setAlpha(1f);
        } else {
            setAlpha(0.5f);
        }
    }

}
