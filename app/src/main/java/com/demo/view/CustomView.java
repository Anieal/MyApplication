package com.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CustomView extends View {

    private int count = 6;                //数据个数
    private float angle = (float) (Math.PI * 2 / count);
    private float radius;                   //网格最大半径
    private int centerX;                  //中心X
    private int centerY;                  //中心Y
    private String[] titles = {"aaa", "bbb", "ccc", "ddd", "eee", "fff"};
    private double[] data = {100, 60, 60, 60, 100, 50}; //各维度分值
    private Paint mainPaint;                //雷达区画笔
    private Paint valuePaint;               //数据区画笔
    private Paint textPaint;                //文本画笔

    private Path hexagonPath;
    private Path linePath;

    public CustomView(Context context) {
        this(context, null);
    }


    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        count = Math.min(data.length, titles.length);

        mainPaint = new Paint();
        mainPaint.setAntiAlias(true);
        mainPaint.setColor(Color.GRAY);
        mainPaint.setStyle(Paint.Style.STROKE);

        valuePaint = new Paint();
        valuePaint.setAntiAlias(true);
        valuePaint.setColor(Color.BLUE);
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new Paint();
        textPaint.setTextSize(20);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);

        hexagonPath = new Path();
        linePath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = Math.min(h, w) / 2 * 0.7f;
        centerX = w / 2;
        centerY = h / 2;
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPolygon(canvas);
        drawLines(canvas);
        drawText(canvas);
        drawRegion(canvas);
    }

    /**
     * 绘制正多边形
     */
    private void drawPolygon(Canvas canvas) {
        float between = radius / count;
        for (int i = 0; i <= count; i++) {
            float currentR = between * i;
            hexagonPath.reset();
            for (int j = 0; j < count; j++) {
                if (j == 0) {
                    hexagonPath.moveTo(centerX + currentR, centerY);
                } else {
                    float currentX = (float) (currentR * Math.cos(angle * j) + centerX);
                    float currentY = (float) (currentR * Math.sin(angle * j) + centerY);
                    hexagonPath.lineTo(currentX, currentY);
                }
            }
            hexagonPath.close();
            canvas.drawPath(hexagonPath, mainPaint);
        }
    }

    /**
     * 绘制直线
     */
    private void drawLines(Canvas canvas) {
        for (int i = 0; i < count; i++) {
            linePath.reset();
            linePath.moveTo(centerX, centerY);
            float lineX = (float) (radius * Math.cos(angle * i) + centerX);
            float lineY = (float) (radius * Math.sin(angle * i) + centerY);
            linePath.lineTo(lineX, lineY);
            canvas.drawPath(linePath, mainPaint);
        }
    }

    /**
     * 绘制文字
     */
    private void drawText(Canvas canvas) {
        float fontHeight = textPaint.getFontMetrics().descent - textPaint.getFontMetrics().ascent;
        for (int i = 0; i < count; i++) {
            float textX = (float) ((radius + fontHeight / 2) * Math.cos(angle * i) + centerX);
            float textY = (float) ((radius + fontHeight / 2) * Math.sin(angle * i) + centerY);
            if (angle * i == 0) {
                canvas.drawText(titles[i], textX, textY + (fontHeight / 4), textPaint);
            } else if (angle * i < Math.PI && angle * i > 0) {
                float fontWidth = textPaint.measureText(titles[i]);
                canvas.drawText(titles[i], textX - fontWidth / 2, textY + (fontHeight / 2), textPaint);
            } else if (angle * i > Math.PI * 2 / 3 && angle * i < Math.PI * 5 / 4) {
                float fontWidth = textPaint.measureText(titles[i]);
                canvas.drawText(titles[i], textX - fontWidth, textY + (fontHeight / 4), textPaint);
            } else if (angle * i > Math.PI * 5 / 4 && angle * i < Math.PI * 2) {
                float fontWidth = textPaint.measureText(titles[i]);
                canvas.drawText(titles[i], textX - fontWidth / 2, textY, textPaint);
            }
        }
    }

    /**
     * 绘制区域
     */
    private void drawRegion(Canvas canvas) {
        Path path = new Path();
        valuePaint.setAlpha(255);
        for (int i = 0; i < count; i++) {
            float maxValue = 100;
            double percent = data[i] / maxValue;
            float x = (float) (centerX + radius * Math.cos(angle * i) * percent);
            float y = (float) (centerY + radius * Math.sin(angle * i) * percent);
            if (i == 0) {
                path.moveTo(x, centerY);
            } else {
                path.lineTo(x, y);
            }
            //绘制小圆点
            canvas.drawCircle(x, y, 4, valuePaint);
        }
        valuePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, valuePaint);
        valuePaint.setAlpha(127);
        //绘制填充区域
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, valuePaint);
    }

}
