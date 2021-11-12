package com.example.chatx5.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatx5.R;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT=2000;
    Animation top_animation,bottom_animation;
    ImageView app_img;
    TextView developer_name,app_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        top_animation= AnimationUtils.loadAnimation(this,R.anim.upperanimation);
        bottom_animation= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        app_img=findViewById(R.id.sp_image);
        developer_name=findViewById(R.id.sp_app_name);
        app_name=findViewById(R.id.sp_app_name);
        developer_name=findViewById(R.id.sp_developer_name);

        app_img.setAnimation(top_animation);
        app_name.setAnimation(bottom_animation);
        developer_name.setAnimation(bottom_animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(MainActivity.this, Log_in.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}


