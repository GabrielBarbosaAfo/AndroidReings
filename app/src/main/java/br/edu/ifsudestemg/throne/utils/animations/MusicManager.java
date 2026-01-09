package br.edu.ifsudestemg.throne.utils.animations;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicManager {

    private static MediaPlayer player;
    private static int currentResId = -1;
    private static int pausedPosition = 0;

    public static void play(Context context, int resId, boolean loop) {

        if (player != null && currentResId == resId) {
            return;
        }

        stopMusic();

        player = MediaPlayer.create(context.getApplicationContext(), resId);
        player.setLooping(loop);
        player.start();

        currentResId = resId;
        pausedPosition = 0;
    }

    public static void pause() {
        if (player != null && player.isPlaying()) {
            pausedPosition = player.getCurrentPosition();
            player.pause();
        }
    }

    public static void resume() {
        if (player != null && !player.isPlaying()) {
            player.seekTo(pausedPosition);
            player.start();
        }
    }

    public static void stopMusic() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
            currentResId = -1;
            pausedPosition = 0;
        }
    }

    public static void switchTrack(Context context, int resId, boolean loop) {
        stopMusic();
        play(context, resId, loop);
    }
}

