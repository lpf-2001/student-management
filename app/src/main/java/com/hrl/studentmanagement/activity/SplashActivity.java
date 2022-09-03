package com.hrl.studentmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.hrl.studentmanagement.R;

import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        Timer timer=new Timer();//创建定时器
        TimerTask task=new TimerTask(){
            @Override
            public void run() {
                startActivity( new Intent(SplashActivity.this,LoginActivity.class));
                finish();
            }
        };
        timer.schedule(task,2500); //单位为毫秒
    }

}
