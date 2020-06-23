package com.yunbao.main.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.ScreenDimenUtil;

public class TestView extends AppCompatTextView {

    private Paint mPaint;
    private Paint mPaint2;
    private Paint mPaint3;
    private RectF mRectF;
    private Rect mRect;
    private Paint.FontMetricsInt mFontMetricsInt;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAlpha(3);
        mPaint.setTextSize(30);
        mPaint.setColor(0xff008800);

        mPaint2 = new Paint();
        mPaint2.setAlpha(3);
        mPaint2.setColor(0xff888800);

        mPaint3 = new Paint();
        mPaint3.setTextSize(30);
        mPaint3.setColor(0xffff0000);
        mRectF = new RectF();
            mFontMetricsInt = mPaint.getFontMetricsInt();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specModeH = MeasureSpec.getMode(heightMeasureSpec);
        int width = 0;
        int height = 0;

        L.e("mFontMetricsInt.bottom="+mFontMetricsInt.bottom+"mFontMetricsInt.top="+mFontMetricsInt.top);
        L.e("mFontMetricsInt.ascent="+mPaint.ascent()+"mFontMetricsInt.descent="+mPaint.descent());
        L.e("mFontMetricsInt.ascent="+mFontMetricsInt.ascent+"mFontMetricsInt.descent="+mFontMetricsInt.descent);

        if (specMode == MeasureSpec.AT_MOST) {
            width =30*"cdsddswdffgg".length();
            L.e("----width>="+width);
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        if (specModeH == MeasureSpec.AT_MOST) {
            height = mFontMetricsInt.bottom-mFontMetricsInt.top;
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        float baseline = getHeight() / 2 + (Math.abs(mPaint.ascent()) - mPaint.descent()) / 2+ getPaddingTop() / 2 - getPaddingBottom() / 2;

        int baseLine = (mFontMetricsInt.bottom - mFontMetricsInt.top) / 2 - mFontMetricsInt.bottom + getHeight() / 2
                + getPaddingTop() / 2 - getPaddingBottom() / 2;



        L.e("-baseline->="+baseline+"--->baseLine="+baseLine);
        canvas.drawText("cdsddswdffgg", 0, baseLine, mPaint);


//        mRectF.set(0,0,getWidth(),getHeight());
//        canvas.drawRoundRect(mRectF, DpUtil.dp2px(15), DpUtil.dp2px(15), mPaint);
//
//        //if (mProgress*getWidth()<=DpUtil.dp2px(15)){
//            mRectF.set(0,0,mProgress*getWidth(),getHeight());
//            canvas.drawArc(mRectF,180, 360,true, mPaint2);
//            //canvas.clipRect(0,0,mProgress*getWidth(),getHeight(), Region.Op.DIFFERENCE);
////        }else {
////            mRectF.set(0,0,mProgress*getWidth(),getHeight());
////            canvas.drawRoundRect(mRectF, DpUtil.dp2px(15), DpUtil.dp2px(15), mPaint2);
////        }


//        canvas.save();
//        canvas.translate(10, 10);
//        //画笔颜色设置为浅蓝色
//        mPaint.setColor(Color.parseColor("#D4E9FA"));
//        //画笔画一个矩形
//        //canvas.drawRect(new RectF(0, 0, 300, 300), mPaint);
//        //画笔画一个圆形
//        canvas.drawCircle(300, 150, 150, mPaint);
//        //画笔颜色设置为浅红色
//        mPaint.setColor(Color.parseColor("#FF4081"));
//        //画布裁剪一个矩形
//        canvas.clipRect(new RectF(0, 0, 300, 300));//第一个裁剪一个形状相当于A
//        //画布裁剪一个圆形
//        Path mPath = new Path();
//        mPath.addCircle(300, 150, 150, Path.Direction.CCW);
//        /**这里只是改变第二个参数Region.Op.来观察效果*/
//        canvas.clipPath(mPath, Region.Op.XOR);//第二个裁剪一个形状相当于B
//        //裁剪完之后,画一个长宽全覆盖的红色矩形观察效果
//        canvas.drawRect(new RectF(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE), mPaint);
//        canvas.restore();


//        canvas.rotate(-90);
//        canvas.translate(-getHeight(), 0);

//        canvas.rotate(90);
//        canvas.translate(0, -getWidth());
        super.onDraw(canvas);

    }

    private float mProgress;

    public void setProgress(float progress) {
        mProgress = progress;
        L.e("mProgress=" + mProgress);
        invalidate();
    }

}
