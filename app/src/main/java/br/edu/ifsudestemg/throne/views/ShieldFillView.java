package br.edu.ifsudestemg.throne.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

public class ShieldFillView extends View {

    private Drawable overlayDrawable;
    private float percentage = 1.0f;
    private int fillColor = Color.parseColor("#604682B4"); // azul frio com transparência

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public ShieldFillView(Context context) {
        this(context, null);
    }

    public ShieldFillView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (overlayDrawable == null) return;

        int width = getWidth();
        int height = getHeight();

        overlayDrawable.setBounds(0, 0, width, height);

        int fillHeight = (int) (height * percentage);
        int fillTop = height - fillHeight;

        paint.setColor(fillColor);
        canvas.drawRect(0, fillTop, width, height, paint);

        overlayDrawable.draw(canvas);
    }

    public void setPercentage(float percentage) {
        this.percentage = Math.max(0.0f, Math.min(1.0f, percentage));
        invalidate();
    }

    public void setFillColor(int color) {
        this.fillColor = color;
        invalidate();
    }

    public void setOverlay(int drawableResId) {
        this.overlayDrawable = AppCompatResources.getDrawable(getContext(), drawableResId);
        invalidate();
    }
}