package com.scorpioneal.scaleview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * TODO 现在只支持均分的，不均分暂时不支持
 * TODO 异常数据还没做处理
 * TODO 支持手势操作
 * TODO 适配
 * TODO 表针可替换
 * Created by ScorpioNeal on 15/7/6.
 */
public class ScaleView extends View {

    private static final String TAG = ScaleView.class.getSimpleName();

    private float mMaxValue = 180; //最大值
    private float mMinValue = 0; //最小值

    private float mStartAngle = 135; //开始角度
    private float mEndAngle = 405;  //结束角度
    private float mSweepAngle = 130; //扫过的角度

    private Paint mScaleProgressPaint; //进度条
    private Paint mScaleBgPaint; //背景
    private Paint mScalePaint; //刻度
    private Paint mScaleNumPaint;//刻度数字
    private Paint mDescripPaint;//描述文字

    private float mViewWidth;//in px
    private float mViewHeight;//in px

    private float mScaleLength = 30;//刻度的长度
    private float mScalePadding = 75;//刻度和圆周的距离

    private int count = 7; //7个刻度

    private float mRadius;

    private String mShowText = "测量中...";

    public static final float VIEW_PADDING = 30;

    private int mScaleBackgroundColor = Color.GRAY;
    private int mScaleSecondaryBackgroundColor = Color.CYAN;
    private int mScaleNumberColor = Color.BLACK;
    private int mScaleTextColor = Color.BLACK;
    private float mScaleTextSize = 50;
    private float mScaleNumberSize = 45;

    public ScaleView(Context context) {
        this(context, null);
    }

    public ScaleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mScaleProgressPaint = new Paint();
        mScaleBgPaint = new Paint();
        mScalePaint = new Paint();
        mScaleNumPaint = new Paint();
        mDescripPaint = new Paint();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScaleViewTheme);
        mScaleBackgroundColor = a.getColor(R.styleable.ScaleViewTheme_scaleBackground, Color.GRAY);
        mScaleSecondaryBackgroundColor = a.getColor(R.styleable.ScaleViewTheme_scaleSecondaryBackground, Color.CYAN);
        mScaleNumberColor = a.getColor(R.styleable.ScaleViewTheme_scaleNumberColor, Color.BLACK);
        mScaleTextColor = a.getColor(R.styleable.ScaleViewTheme_scaleTextColor, Color.BLACK);
        mScaleTextSize = a.getDimension(R.styleable.ScaleViewTheme_scaleTextSize, 50);
        mScaleNumberSize = a.getDimension(R.styleable.ScaleViewTheme_scaleNumberSize, 45);
        a.recycle();

        initPaint();
    }

    private void initPaint() {

        mScaleBgPaint.setColor(mScaleBackgroundColor);
        mScaleBgPaint.setStrokeWidth(12);
        mScaleBgPaint.setAntiAlias(true);
        mScaleBgPaint.setStyle(Paint.Style.STROKE);

        mScalePaint.setStrokeWidth(8);
        mScalePaint.setAntiAlias(true);
        mScalePaint.setStyle(Paint.Style.STROKE);

        mScaleProgressPaint.setColor(mScaleSecondaryBackgroundColor);
        mScaleProgressPaint.setStrokeWidth(12);
        mScaleProgressPaint.setAntiAlias(true);
        mScaleProgressPaint.setStyle(Paint.Style.STROKE);

        mScaleNumPaint.setColor(mScaleNumberColor);
        mScaleNumPaint.setStrokeWidth(1);
        mScaleNumPaint.setAntiAlias(true);
        mScaleNumPaint.setTextSize(mScaleNumberSize);

        mDescripPaint.setColor(mScaleTextColor);
        mDescripPaint.setAntiAlias(true);
        mDescripPaint.setTextSize(mScaleTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mViewHeight = getMeasuredHeight() - VIEW_PADDING * 2;

        mViewWidth = getMeasuredWidth() - VIEW_PADDING * 2;

        mRadius = Math.min(mViewHeight, mViewWidth) / 2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mScaleBgPaint.setColor(mScaleBackgroundColor);
        mScaleProgressPaint.setColor(mScaleSecondaryBackgroundColor);
        mScaleNumPaint.setColor(mScaleNumberColor);

        RectF circleRect = new RectF(VIEW_PADDING, VIEW_PADDING, VIEW_PADDING+ mRadius * 2, VIEW_PADDING + mRadius * 2);
        //画背景大圈
        canvas.drawArc(circleRect, mStartAngle, mEndAngle - mStartAngle, false, mScaleBgPaint);
        //画指示进度的圈
        canvas.drawArc(circleRect, mStartAngle, mSweepAngle, false, mScaleProgressPaint);

        //圆心
        float originX = mRadius + VIEW_PADDING;
        float originY = mRadius + VIEW_PADDING;

        //画刻度
        for(int i = 0; i < count; i++) {
            //总共7个刻度
            float angle = mStartAngle + i * (mEndAngle - mStartAngle) / (count - 1);

            if(angle <= mSweepAngle + mStartAngle){
                mScalePaint.setColor(mScaleSecondaryBackgroundColor);
            }else {
                mScalePaint.setColor(mScaleBackgroundColor);
            }

            float angleValue = (float)(angle / 180 * Math.PI);
            float startX = (float)(originX + (mRadius - mScaleLength) * Math.cos(angleValue));
            float startY = (float)(originY + (mRadius - mScaleLength) * Math.sin(angleValue));
            float endX   = (float)(originX + mRadius * Math.cos(angleValue));
            float endY   = (float)(originY + mRadius * Math.sin(angleValue));
            canvas.drawLine(startX, startY, endX, endY, mScalePaint);

            //画刻度的数字
            float textWidth = mScaleNumPaint.measureText((int)angle+"");
            float textHeight = 20; //TODO a better way to measure text height

            float x = (float)(startX - mScalePadding * Math.cos(angleValue)) - textWidth / 2;
            float y = (float)(startY - mScalePadding * Math.sin(angleValue)) + textHeight / 2; //坐标系不同，所以要minus

            float value = mMinValue + (angle - mStartAngle) * (mMaxValue - mMinValue) / (mEndAngle - mStartAngle);
            canvas.drawText((int)value + "", x, y, mScaleNumPaint);
        }

        //画中下方的文字
        float txtWidth = mDescripPaint.measureText(mShowText);
        //大约在3/4的下方
        canvas.drawText(mShowText, VIEW_PADDING + mRadius - txtWidth / 2, VIEW_PADDING + 7 * mRadius / 4, mDescripPaint);

        //画指针 TODO use bitmap to replace line
        float angleValue = (float)((mStartAngle + mSweepAngle) / 180 * Math.PI);
        canvas.drawLine(originX, originY, (float)(originX + (mRadius - 120) * Math.cos(angleValue)), (float)(originY + (mRadius - 120) * Math.sin(angleValue)), mScaleProgressPaint);

    }

    /**
     * 设置显示的文本内容
     * @param message
     */
    public void setShowText(String message){
        mShowText = message;
        invalidate();
    }

    /**
     * 设置显示的取值区间
     * @param fromValue
     * @param toValue
     */
    public void setValueRegion(float fromValue, float toValue){
        mMinValue = fromValue;
        mMaxValue = toValue;
        invalidate();
    }

    /**
     * 设置角度区间
     * @param fromAngle
     * @param toAngle
     */
    public void setAngleRegion(float fromAngle, float toAngle){
        mStartAngle = fromAngle;
        mEndAngle = toAngle;
        invalidate();
    }

    private float lastShownValue = 0f;
    /**
     * 设置显示的值
     * @param value
     */
    public void setShownValue(float value){
        if(value - mMinValue < 0.0000001f) {
            value = mMinValue;
        }
        if(value - mMaxValue > 0.0000001f) {
            value = mMaxValue;
        }
        final float v = value;
        ValueAnimator valueAnimator = new ValueAnimator().ofFloat(lastShownValue, v);
        valueAnimator.setInterpolator(new MyIntepolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float)animation.getAnimatedValue();
                Log.d(TAG, "value : " + value);
                float sweapAngle = (value - mMinValue) * (mEndAngle - mStartAngle) / (mMaxValue - mMinValue);
                mSweepAngle = sweapAngle;
                invalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lastShownValue = v;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(400).start();
    }

    class MyIntepolator extends BounceInterpolator {
        private float bounce(float t){
            return t * t * 8.0f;
        }
        @Override
        public float getInterpolation(float t) {
//            t *= 1.1226f;
            if (t < 0.3535f) return bounce(t);
            else if (t < 0.7408f) return bounce(t - 0.54719f) + 0.7f;
            else if (t < 0.9644f) return bounce(t - 0.8526f) + 0.9f;
            else return bounce(t - 1.0435f) + 0.95f;
        }
    }

    public float getMinValue() {
        return mMinValue;
    }

    public float getMaxValue() {
        return mMaxValue;
    }

    /**
     * 设置刻度颜色
     * @param mScaleBackgroundColor
     */
    public void setScaleBackgroundColor(int mScaleBackgroundColor) {
        this.mScaleBackgroundColor = mScaleBackgroundColor;
        invalidate();
    }

    /**
     * 设置进度条颜色
     * @param mScaleSecondaryBackgroundColor
     */
    public void setScaleSecondaryBackgroundColor(int mScaleSecondaryBackgroundColor) {
        this.mScaleSecondaryBackgroundColor = mScaleSecondaryBackgroundColor;
        invalidate();
    }

    /**
     * 设置最大值和最小值
     * @param minValue
     * @param maxValue
     */
    public void setMinMaxValue(float minValue, float maxValue) {
        this.mMinValue = Math.min(minValue, maxValue);
        this.mMaxValue = Math.max(minValue, maxValue);
        invalidate();
    }

    public void setStartEndAngle(float startAngle, float endAngle) {
        this.mStartAngle = Math.min(startAngle, endAngle);
        this.mEndAngle = Math.max(startAngle, endAngle);
        invalidate();
    }

    public void setCount(int count) {
        this.count = count;
        invalidate();
    }

    public void setScaleNumberColor(int mScaleNumberColor) {
        this.mScaleNumberColor = mScaleNumberColor;
        invalidate();
    }
}
