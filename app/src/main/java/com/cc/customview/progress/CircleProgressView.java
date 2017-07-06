package com.cc.customview.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cc.customview.R;


/**
 * Created by CC on 2017/6/10.
 */

public class CircleProgressView extends View {
    private Paint circlePaint; // 最里层实心圆画笔
    private int circleColor; // 实心圆颜色

    private Paint progressPaint; // 中间显示进度圆环画笔
    private float progressWidth; // 进度圆环宽度
    private int progressColor; // 进度圆环颜色

    private Paint sectorPaint; // 最外层圆环画笔
    private float sectorWidth; // 外层圆环宽度
    private int sectorColor; // 外层圆环颜色

    private Paint proTextPaint;
    private float proTextSize;
    private int proTextColor;

    private Paint tipTextPaint;
    private float tipTextSize;
    private int tipTextColor;
    private String tipText;

    private int currProgress; // 当前进度
    private int maxProgress; // 最大进度

    private boolean isShow;
    private boolean isAbove;
    private boolean isScroll;

    public CircleProgressView(Context context) {
        super(context, null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaint();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressView, 0, 0);
        circleColor = typedArray.getColor(R.styleable.CircleProgressView_circleColor, 0x993F51B5);
        progressWidth = typedArray.getDimension(R.styleable.CircleProgressView_progressWidth, 300);
        progressColor = typedArray.getColor(R.styleable.CircleProgressView_progressColor, 0x3F51B5);
        sectorWidth = typedArray.getDimension(R.styleable.CircleProgressView_sectorWidth, 10);
        sectorColor = typedArray.getColor(R.styleable.CircleProgressView_sectorColor, 0xFF4081);
        proTextSize = typedArray.getDimension(R.styleable.CircleProgressView_proTextSize, 60);
        proTextColor = typedArray.getColor(R.styleable.CircleProgressView_proTextColor, 0xFFFFFF);
        tipTextSize = typedArray.getDimension(R.styleable.CircleProgressView_tipTextSize, 30);
        tipTextColor = typedArray.getColor(R.styleable.CircleProgressView_tipTextColor, 0xFFFFFF);
        tipText = typedArray.getString(R.styleable.CircleProgressView_tipText);
        maxProgress = typedArray.getInteger(R.styleable.CircleProgressView_max, 100);
        isShow = typedArray.getBoolean(R.styleable.CircleProgressView_showStoke, true);
        isAbove = typedArray.getBoolean(R.styleable.CircleProgressView_isAbove, false);
        isScroll = typedArray.getBoolean(R.styleable.CircleProgressView_isScroll, false);
        typedArray.recycle();
    }

    private void initPaint() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);

        sectorPaint = new Paint();
        sectorPaint.setAntiAlias(true);
        sectorPaint.setStyle(Paint.Style.STROKE);

        proTextPaint = new Paint();
        proTextPaint.setAntiAlias(true);
        proTextPaint.setStyle(Paint.Style.FILL);

        tipTextPaint = new Paint();
        tipTextPaint.setAntiAlias(true);
        tipTextPaint.setStyle(Paint.Style.FILL);
    }

    private void initVar() {
        circlePaint.setColor(circleColor);

        progressPaint.setColor(progressColor);
        progressPaint.setStrokeWidth(progressWidth);

        sectorPaint.setColor(sectorColor);
        sectorPaint.setStrokeWidth(sectorWidth);

        proTextPaint.setColor(proTextColor);
        proTextPaint.setTextSize(proTextSize);

        tipTextPaint.setColor(tipTextColor);
        tipTextPaint.setTextSize(tipTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initVar();
        float circleRadius = getWidth() / 2;
        Log.d("onDrawCanvas", isShow + "/" + isScroll + "/" + sectorWidth + "/" + progressWidth + "/" + maxProgress);
        if (currProgress >= 0) {
            if (isShow) {
                RectF sector = new RectF(sectorWidth / 2,
                        sectorWidth / 2,
                        2 * circleRadius - sectorWidth / 2,
                        2 * circleRadius - sectorWidth / 2);
                canvas.drawArc(sector, 0, 360, false, sectorPaint);
            }

            RectF progressRectF = new RectF(sectorWidth + progressWidth / 2,
                    sectorWidth + progressWidth / 2,
                    2 * circleRadius - sectorWidth - progressWidth / 2,
                    2 * circleRadius - sectorWidth - progressWidth / 2);
            if (isAbove) {
                // 最里层圆环
                canvas.drawCircle(circleRadius, circleRadius, circleRadius - sectorWidth, circlePaint);
            } else {
                canvas.drawCircle(circleRadius, circleRadius, circleRadius - progressWidth - sectorWidth, circlePaint);
            }

            if (!isScroll) {
                temp = currProgress;
            }
            canvas.drawArc(progressRectF, -90, (float) temp / maxProgress * 360, false, progressPaint);
            String proText = temp + "%";

            Rect rect = new Rect();
            proTextPaint.getTextBounds(proText, 0, proText.length(), rect);
            int height = rect.height();
            canvas.drawText(proText, circleRadius - proTextPaint.measureText(proText) / 2,
                    circleRadius + height / 2, proTextPaint);
//
            if (!TextUtils.isEmpty(tipText)) {
                Rect tipRect = new Rect();
                tipTextPaint.getTextBounds(tipText, 0, tipText.length(), tipRect);
                int tipHeight = tipRect.height();
                canvas.drawText(tipText, circleRadius - tipTextPaint.measureText(tipText) / 2,
                        3 * circleRadius / 2 + tipHeight / 2, tipTextPaint);
            }
        }
    }

    private int temp;

    public void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress must greater than 0");
        }
        temp = 0;
        currProgress = progress;
        if (isScroll) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (temp < currProgress) {
                        temp++;
                        try {
                            Thread.sleep(20);
                            postInvalidate();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        } else {
            invalidate();
        }
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        invalidate();
    }

    public float getProgressWidth() {
        return progressWidth;
    }

    public void setProgressWidth(float progressWidth) {
        this.progressWidth = progressWidth;
        invalidate();
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        invalidate();
    }

    public float getSectorWidth() {
        return sectorWidth;
    }

    public void setSectorWidth(float sectorWidth) {
        this.sectorWidth = sectorWidth;
        invalidate();
    }

    public int getSectorColor() {
        return sectorColor;
    }

    public void setSectorColor(int sectorColor) {
        this.sectorColor = sectorColor;
        invalidate();
    }

    public float getProTextSize() {
        return proTextSize;
    }

    public void setProTextSize(float proTextSize) {
        this.proTextSize = proTextSize;
        invalidate();
    }

    public int getProTextColor() {
        return proTextColor;
    }

    public void setProTextColor(int proTextColor) {
        this.proTextColor = proTextColor;
        invalidate();
    }

    public float getTipTextSize() {
        return tipTextSize;
    }

    public void setTipTextSize(float tipTextSize) {
        this.tipTextSize = tipTextSize;
    }

    public int getTipTextColor() {
        return tipTextColor;
    }

    public void setTipTextColor(int tipTextColor) {
        this.tipTextColor = tipTextColor;
        invalidate();
    }

    public String getTipText() {
        return tipText;
    }

    public void setTipText(String tipText) {
        this.tipText = tipText;
        invalidate();
    }

    public synchronized int getCurrProgress() {
        return currProgress;
    }

    public synchronized void setCurrProgress(int currProgress) {
        this.currProgress = currProgress;
        invalidate();
    }

    public synchronized int getMaxProgress() {
        return maxProgress;
    }

    public synchronized void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        invalidate();
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
        invalidate();
    }

    public boolean isAbove() {
        return isAbove;
    }

    public void setAbove(boolean above) {
        isAbove = above;
        invalidate();
    }

    public boolean isScroll() {
        return isScroll;
    }

    public void setScroll(boolean scroll) {
        isScroll = scroll;
        invalidate();
    }
}
