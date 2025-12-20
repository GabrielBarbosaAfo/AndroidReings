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

public class PercentageIconView extends View {

    private Drawable emptyIcon;
    private Drawable fullIcon;
    private float percentage = 0f;

    public PercentageIconView(Context context) {
        this(context, null);
    }

    public PercentageIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIcons(int emptyResId, int fullResId) {
        emptyIcon = AppCompatResources.getDrawable(getContext(), emptyResId);
        fullIcon = AppCompatResources.getDrawable(getContext(), fullResId);
        invalidate();
    }

    public void setPercentage(float percentage) {
        this.percentage = Math.max(0f, Math.min(1f, percentage));
        invalidate();
    }

    public float getPercentage() {
        return percentage;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (emptyIcon == null)
            return;

        emptyIcon.setBounds(0, 0, getWidth(), getHeight());
        emptyIcon.draw(canvas);

        if (fullIcon != null && percentage > 0) {

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

        if (percentage <= 0.10f) {
            drawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else if (percentage >= 0.90f) {
            drawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else {
            drawable.clearColorFilter();
        }
    }
}
