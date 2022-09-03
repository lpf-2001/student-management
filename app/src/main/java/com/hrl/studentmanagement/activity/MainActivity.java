package com.hrl.studentmanagement.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hrl.studentmanagement.R;
import com.hrl.studentmanagement.ui.home.HomeFragment;
import com.hrl.studentmanagement.ui.my.MyFragment;
import com.hrl.studentmanagement.ui.search.SearchFragment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity {

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private final BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentManager = getSupportFragmentManager();  //使用fragmentmanager和transaction来实现切换效果
            transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.content,new HomeFragment());  //对应的java class
                    transaction.commit();
                    return true;
                case R.id.navigation_search:
                    transaction.replace(R.id.content,new SearchFragment());  //对应的java class
                    transaction.commit();
                    return true;
                case R.id.navigation_my:
                    transaction.replace(R.id.content,new MyFragment());  //对应的java class
                    transaction.commit();
                    return true;
            }
            return false;
        }
    };
    private void setDefaultFragment(){
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content,new HomeFragment());
        transaction.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.getSupportActionBar().hide();
        setDefaultFragment();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



    }

    public  void back(){

        if(MainActivity.this.getWindow().getDecorView().getVisibility() == View.VISIBLE){
            AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
            normalDialog.setTitle("提示信息");
            normalDialog.setMessage("确定要退出吗？");
            normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            normalDialog.show();
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            back();
        }
        return false;
    }

}