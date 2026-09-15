package com.ocp.consultationstocks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircularProgressView extends View {

    private Paint backgroundPaint;
    private Paint progressPaint;
    private Paint textPaint;

    private float progress = 0;

    private final int green = 0xFF20B86B;
    private final int darkGray = 0xFF3A3A3A;

    public CircularProgressView(Context context) {
        super(context);
        init();
    }

    public CircularProgressView(
            Context context,
            AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularProgressView(
            Context context,
            AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(18);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        backgroundPaint.setColor(darkGray);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(18);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setColor(green);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(green);
        textPaint.setTextSize(42);
        textPaint.setTypeface(
                android.graphics.Typeface.DEFAULT_BOLD
        );
        textPaint.setTextAlign(Paint.Align.CENTER);

        setLayerType(
                View.LAYER_TYPE_SOFTWARE,
                null
        );
    }

    public void setProgress(float value) {

        if (value < 0) {
            value = 0;
        }

        if (value > 100) {
            value = 100;
        }

        progress = value;

        invalidate();
    }

    public float getProgress() {
        return progress;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        float radius =
                Math.min(
                        getWidth(),
                        getHeight()
                ) / 2f - 20;

        RectF rect =
                new RectF(
                        centerX - radius,
                        centerY - radius,
                        centerX + radius,
                        centerY + radius
                );

        /*
         * Cercle gris foncé
         */
        canvas.drawArc(
                rect,
                -90,
                360,
                false,
                backgroundPaint
        );

        /*
         * Progression verte
         */
        float sweep =
                360f * progress / 100f;

        canvas.drawArc(
                rect,
                -90,
                sweep,
                false,
                progressPaint
        );

        /*
         * Pourcentage au centre
         */
        String text =
                Math.round(progress) + "%";

        Paint.FontMetrics metrics =
                textPaint.getFontMetrics();

        float baseline =
                centerY
                        - (metrics.ascent
                        + metrics.descent) / 2;

        canvas.drawText(
                text,
                centerX,
                baseline,
                textPaint
        );
    }
}
