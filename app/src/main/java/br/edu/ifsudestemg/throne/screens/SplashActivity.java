package br.edu.ifsudestemg.throne.screens;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private VideoView videoView;
    private View fadeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        videoView = findViewById(R.id.splash_video);
        fadeView = findViewById(R.id.fade_view);

        playIntro();
    }

    private void playIntro() {
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro);
        videoView.setVideoURI(videoUri);

        videoView.setOnPreparedListener(mp -> mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING));

        videoView.setOnCompletionListener(mp -> startFadeOut());

        videoView.setOnErrorListener((mp, what, extra) -> {
            goToMain();
            return true;
        });

        videoView.start();
    }

    private void startFadeOut() {
        AlphaAnimation anim = new AlphaAnimation(0f, 1f);
        anim.setDuration(800);
        anim.setFillAfter(true);

        fadeView.startAnimation(anim);

        fadeView.postDelayed(this::goToMain, 100);
    }

    private void goToMain() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
