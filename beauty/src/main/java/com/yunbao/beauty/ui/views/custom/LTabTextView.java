package com.yunbao.beauty.ui.views.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DrawableUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yunbao.beauty.R;

public class LTabTextView extends TextView {
        // 宽高
        private int mWidth, mHeight;
        private float _density = 1;
        // 文本画笔
        private Paint tPaint = null;
        // 字体颜色
        private int textColor = Color.RED;
        // 字体大小
        private float textSize = 8f;
        // 画笔宽度
        private int strokeWidth = 2;

        private float mPointX = 0;
        private float mPointY = 0;
        private float mPointTextWidth = 0;
        private float mPointTextHeight = 0;
        private CharSequence mPointText = null;
        private float circleY = 0;
        private float circleX = 0;
        private Paint circlePaint = null;
        // 圆圈颜色
        private int circleColor = Color.RED;
        // 圆圈画笔宽度
        private int circlestrokeWidth = 2;
        //数字在x轴的位移
        private float offsetX = 0;
        //数字在y轴的位移
        private float offsetY = 0;
        //数字的最大宽度
        private float maxWidth = 0;

        public LTabTextView(Context context) {
            super(context);
            init(context);
        }

        public LTabTextView(Context context, AttributeSet attrs,
                            int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context);
        }

        public LTabTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        private void init(Context context) {
//            _density = context.getResources().getDisplayMetrics().density;
//            tPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            tPaint.setColor(textColor);
//            tPaint.setStrokeWidth(strokeWidth);
//            tPaint.setTextSize(textSize * _density);
//            tPaint.setStyle(Paint.Style.FILL);
            setPadding(0,0,5,0);
        }

//        @Override
//        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//            super.onSizeChanged(w, h, oldw, oldh);
//            mWidth = w;
//            mHeight = h;
//            if (this.getText() != null) {
//                if (mPointText!=null&&!mPointText.equals("")) {
//                    //计算出数字的可能的最大宽度，这里默认最大能显示数字是99，对于99用99+表示
//                    maxWidth = tPaint.measureText("99+");
//                    //当前数字的真正宽度
//                    mPointTextWidth = tPaint.measureText(String.valueOf(mPointText));
//                    //计算的是数字的x坐标
//                    //因为已经限定数字的最大宽度是"99+"字符串的宽度
////              mWidth-maxWidth/2就是圆心的位置，
////              如果我们要将数字画在园的中心，就要
////              获取当前数字的宽度的一半
////              mWidth-maxWidth/2-mPointTextWidth/2，
////              然后再减去自己设置的x轴位移量
//                    mPointX = mWidth-maxWidth/2-mPointTextWidth/2-offsetX;
//                    Paint.FontMetrics metrics = tPaint.getFontMetrics();
//                    //因为数字的外面要包一个圆，所以为了让圆能显示完全，就让数字的高也和宽相等，减去位移量，获取数字的高度
//                    float textH = maxWidth+offsetY;
//                    //如果看过http://www.jianshu.com/p/a3d15421a718我这篇文章，应该知道这是获取baseline，
//                    //获取到这个位置就能将文字在指定位置的y轴中心显示
//                    mPointTextHeight = (textH-(metrics.descent-metrics.ascent))/2-metrics.ascent;
//                    //为了能让圆圈显示完整，所以+1*_density
//                    mPointY = mPointTextHeight+1*_density;
//                    //圆的x轴
//                    circleX = mWidth-(maxWidth)/2-offsetX;
//                    //圆的y轴
//                    circleY = textH/2+1*_density;
//                }
//            }
//        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
//            if (mPointText!=null&&!mPointText.equals("")) {
//                canvas.drawCircle(circleX, circleY, maxWidth/2+1*_density, circlePaint);
//                if (Integer.parseInt(mPointText.toString())>99) {
//                    canvas.drawText("99+", mPointX, mPointY, tPaint);
//                }else
//                    canvas.drawText(mPointText.toString(), mPointX, mPointY, tPaint);
//            }
            canvas.save();
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            Drawable drawable = getContext().getDrawable(R.mipmap.icon_tab_topright_pro);
            if (drawable == null) return;
            int drawableWidth = drawable.getIntrinsicWidth() ;
            int drawableHeight = drawable.getIntrinsicHeight();
            drawable.setBounds(0,0,drawableWidth,drawableHeight);
            float width = getPaint().measureText(getText().toString());
            float trueWidth = width < measuredWidth ? width : measuredWidth;
            float topMargin = (getHeight() +getLayout().getLineTop(0)-getLayout().getLineBottom(0))/2;
            float left = (float)measuredWidth/2 + trueWidth/2;
            float top =getPaddingTop() + getCompoundPaddingTop() + topMargin - drawableHeight/2;
            canvas.translate(left,top);
            drawable.draw(canvas);
            canvas.restore();
        }
}
