package com.hrl.studentmanagement.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.hrl.studentmanagement.R;
import com.hrl.studentmanagement.ui.logandreg.LoginFragment;


public class LoginActivity extends AppCompatActivity  {
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private LinearLayout login_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //自定义状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.Light_Blue));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_main = (LinearLayout)findViewById(R.id.login_main);
        setDefaultFragment();

    }
    private void setDefaultFragment(){//默认打开登录页面
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.login_main,new LoginFragment());
        transaction.commit();
    }

}