// utils/FeedbackUtils.java
package br.edu.ifsudestemg.throne.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import br.edu.ifsudestemg.throne.R;

public class FeedbackUtils {

    public static void playCardAppear(Context context) {
        playSound(context, R.raw.card_appear);
    }

    public static void playClickFeedback(Context context) {
        vibrateShort(context);
        playSound(context, R.raw.button_click);
    }

    private static void vibrateShort(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(50);
            }
        }
    }

    private static void playSound(Context context, int soundResId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, soundResId);
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
            mediaPlayer.start();
        }
    }
}