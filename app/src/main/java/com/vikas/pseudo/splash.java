package com.vikas.pseudo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        ImageView imageView = findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.pop_out_animation);
        imageView.startAnimation(animation);
        TextView textView = findViewById(R.id.textView);

        AnimationSet animationSet = new AnimationSet(true);

        Animation translateAnimation = new TranslateAnimation(0, 0, -300, 0); // Adjust the starting position (-500) based on your requirement
        Animation alphaAnimation = new AlphaAnimation(0, 1);

        translateAnimation.setDuration(2000); // Adjust the duration (in milliseconds) as needed
        alphaAnimation.setDuration(2000);

        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);

        textView.startAnimation(animationSet);



        new Handler().postDelayed(() -> {
            Intent intent= new Intent(splash.this ,MainActivity.class);
            startActivity(intent);
            finish();
        },2500);
    }
}