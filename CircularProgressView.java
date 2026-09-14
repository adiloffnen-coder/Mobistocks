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

    private final RectF rect = new RectF();

    public CircularProgressView(Context context) {
        super(context);
        init();
    }

    public CircularProgressView(Context context, AttributeSet attrs) {
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
        backgroundPaint.setStrokeWidth(10);
        backgroundPaint.setColor(0xFFE1E5E3);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(10);
        progressPaint.setColor(0xFF168A4A);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(0xFF168A4A);
        textPaint.setTextSize(30);
        textPaint.setTypeface(
                android.graphics.Typeface.DEFAULT_BOLD
        );
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        float radius =
                Math.min(getWidth(), getHeight()) / 2f - 12;

        rect.set(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius
        );

        // Cercle de fond
        canvas.drawArc(
                rect,
                -90,
                360,
                false,
                backgroundPaint
        );

        // Progression verte
        canvas.drawArc(
                rect,
                -90,
                progress * 3.6f,
                false,
                progressPaint
        );

        // Pourcentage au centre
        String text =
                ((int) progress) + " %";

        Paint.FontMetrics metrics =
                textPaint.getFontMetrics();

        float baseline =
                centerY
                        - (metrics.ascent + metrics.descent)
                        / 2f;

        canvas.drawText(
                text,
                centerX,
                baseline,
                textPaint
        );
    }

    public void setProgress(float progress) {

        this.progress =
                Math.max(0, Math.min(progress, 100));

        invalidate();
    }

    public float getProgress() {
        return progress;
    }
}
