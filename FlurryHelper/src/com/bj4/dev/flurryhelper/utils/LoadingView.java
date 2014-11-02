
package com.bj4.dev.flurryhelper.utils;

import java.lang.ref.WeakReference;

import com.bj4.dev.flurryhelper.R;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class LoadingView extends View {
    private final Paint mPaint = new Paint();

    private final Paint mInnerPaint = new Paint();

    private WeakReference<Context> mContext;

    private final Path mCirclePath = new Path();

    private RectF mRectF;

    private int mRadius;

    private static final int SWIPE_DURATION = 10000;

    private static final int SWIPE_ANGLE = 40;

    private final ValueAnimator mVa = ValueAnimator.ofInt(0, 100);

    private int mCurrentAngle = 0;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = new WeakReference<Context>(context);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth((int)context.getResources().getDimension(
                R.dimen.outer_paint_stroke_width));
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setStyle(Style.STROKE);
        mInnerPaint.setColor(Color.GRAY);
        mInnerPaint.setStrokeWidth((int)context.getResources().getDimension(
                R.dimen.inner_paint_stroke_width));
        setBackgroundColor(0x40000000);
        ViewTreeObserver observer = this.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                ViewTreeObserver observer = LoadingView.this.getViewTreeObserver();
                if (observer.isAlive())
                    observer.removeOnGlobalLayoutListener(this);
                final int centerX = getMeasuredWidth() / 2;
                final int centerY = getMeasuredHeight() / 2;
                mRadius = (int)mContext.get().getResources().getDimension(R.dimen.circle_radius);
                mRectF = new RectF(centerX - mRadius, centerY - mRadius, centerX + mRadius, centerY
                        + mRadius);
                mVa.start();
                invalidate();
            }
        });
        mVa.setDuration(SWIPE_DURATION);
        mVa.setRepeatCount(ValueAnimator.INFINITE);
        mVa.setRepeatMode(ValueAnimator.RESTART);
        mVa.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                if (mRectF != null) {
                    mCurrentAngle += 2;
                    mCurrentAngle %= 360;
                    mCirclePath.reset();
                    mCirclePath.arcTo(mRectF, mCurrentAngle, SWIPE_ANGLE);
                    invalidate();
                }
            }
        });
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mVa.isStarted() == false)
            mVa.start();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVa.cancel();
    }

    public void setPaintColor(int color) {
        mPaint.setColor(color);
    }

    public void setPaint(Paint src) {
        mPaint.set(src);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRectF != null) {
            canvas.drawCircle((mRectF.left + mRectF.right) / 2, (mRectF.top + mRectF.bottom) / 2,
                    mRadius, mInnerPaint);
            canvas.drawPath(mCirclePath, mPaint);
        }
    }
}