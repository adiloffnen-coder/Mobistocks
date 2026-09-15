package com.ocp.consultationstocks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircularProgressView extends View {

    private Paint paint;
    private RectF rectF;

    private int progress = 0;

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

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
        paint.setStrokeCap(Paint.Cap.ROUND);

        rectF = new RectF();
    }

    public void setProgress(int value) {

        progress = Math.max(0, Math.min(100, value));

        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        float stroke = 10f;

        float left = stroke;
        float top = stroke;
        float right = getWidth() - stroke;
        float bottom = getHeight() - stroke;

        rectF.set(left, top, right, bottom);

        // Cercle gris foncé
        paint.setColor(0xFF3A3D40);
        paint.setStrokeWidth(stroke);

        canvas.drawArc(
                rectF,
                0,
                360,
                false,
                paint
        );

        // Progression verte
        if (progress > 0) {

            paint.setColor(0xFF20D366);

            float angle =
                    360f * progress / 100f;

            canvas.drawArc(
                    rectF,
                    -90,
                    angle,
                    false,
                    paint
            );
        }
    }
}
