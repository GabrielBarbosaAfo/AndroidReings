package br.edu.ifsudestemg.throne.views;

import android.content.Context;
import android.graphics.Canvas;
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

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (emptyIcon == null) return;

        emptyIcon.setBounds(0, 0, getWidth(), getHeight());
        emptyIcon.draw(canvas);

        if (fullIcon != null && percentage > 0) {
            int fullHeight = getHeight();
            int clipHeight = (int) (fullHeight * percentage);

            canvas.save();
            canvas.clipRect(0, fullHeight - clipHeight, getWidth(), fullHeight);
            fullIcon.setBounds(0, 0, getWidth(), getHeight());
            fullIcon.draw(canvas);
            canvas.restore();
        }
    }
}