package com.example.helping_wind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    Animation bottomAnimation, bottom2,topAnimation;

    private TextView title, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        title = findViewById(R.id.title);
        slogan = findViewById(R.id.slogan);

        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        bottom2 = AnimationUtils.loadAnimation(this, R.anim.bottom_2);

        title.setAnimation(topAnimation);
        slogan.setAnimation(bottom2);

        int SPLASH_SCREEN = 4300;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);


    }
}