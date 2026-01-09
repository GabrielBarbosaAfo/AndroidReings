package br.edu.ifsudestemg.throne.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.Objects;

public class PercentageIconView extends View {

    private Drawable emptyIcon;
    private Drawable fullIcon;

    private float percentage = 0f;

    private int internalAlpha = 255;

    public PercentageIconView(Context context) {
        this(context, null);
    }

    public PercentageIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIcons(int emptyResId, int fullResId) {
        emptyIcon = Objects.requireNonNull(AppCompatResources
                        .getDrawable(getContext(), emptyResId))
                .mutate();

        fullIcon = Objects.requireNonNull(AppCompatResources
                        .getDrawable(getContext(), fullResId))
                .mutate();

        invalidate();
    }

    public void setPercentage(float percentage) {
        if (percentage > 1.0f) {
            percentage = percentage / 100f;
        }

        this.percentage = Math.max(0f, Math.min(1f, percentage));
        invalidate();
    }

    public float getPercentage() {
        return percentage;
    }

    @Override
    public void setAlpha(float alpha) {
        internalAlpha = (int) (alpha * 255);
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (emptyIcon == null) return;

        emptyIcon.setAlpha(internalAlpha);
        if (fullIcon != null) {
            fullIcon.setAlpha(internalAlpha);
        }

        emptyIcon.setBounds(0, 0, getWidth(), getHeight());
        emptyIcon.draw(canvas);

        if (fullIcon != null && percentage > 0f) {
            applyDangerColorIfNeeded(fullIcon);

            int clipWidth = (int) (getWidth() * percentage);

            canvas.save();
            canvas.clipRect(0, 0, clipWidth, getHeight());
            fullIcon.setBounds(0, 0, getWidth(), getHeight());
            fullIcon.draw(canvas);
            canvas.restore();
        }
    }

    private void applyDangerColorIfNeeded(Drawable drawable) {
        if (percentage <= 0.10f || percentage >= 0.90f) {
            int dangerColor = Color.parseColor("#7A1E1E");
            drawable.setColorFilter(dangerColor, PorterDuff.Mode.SRC_IN);
        } else {
            drawable.clearColorFilter();
        }
    }
}
