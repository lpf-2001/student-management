package com.hrl.studentmanagement.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hrl.studentmanagement.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailedActivity extends AppCompatActivity {

    @BindView(R.id.xq_id)
    TextView xqId;
    @BindView(R.id.xq_name)
    TextView xqName;
    @BindView(R.id.xq_age)
    TextView xqAge;
    @BindView(R.id.xq_sex)
    TextView xqSex;
    @BindView(R.id.xq_aihao)
    TextView xqAihao;
    @BindView(R.id.xq_phone)
    TextView xqPhone;
    @BindView(R.id.xq_date)
    TextView xqDate;
    @BindView(R.id.btn_gobank)
    Button btnGobank;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //自定义状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.Light_Blue));
        }
        setContentView(R.layout.activity_detailed);
        ButterKnife.bind(this);
        tvTitle.setText("信息详情");//设置标题
        setToolBar();//标题栏返回箭头
        fuzhi();//给控件添加信息
    }

    private void fuzhi() {//给控件添加信息
        Intent intent = getIntent();
        xqId.setText(intent.getStringExtra("id"));
        xqName.setText(intent.getStringExtra("name"));
        xqAge.setText(intent.getStringExtra("age"));
        xqSex.setText(intent.getStringExtra("sex"));
        xqAihao.setText(intent.getStringExtra("aihao"));
        xqPhone.setText(intent.getStringExtra("phone"));
        xqDate.setText(intent.getStringExtra("date"));
    }

    @OnClick(R.id.btn_gobank)
    public void onViewClicked() {
        finish();
    }
    private void setToolBar() {//返回箭头不显示
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    //Toolbar的事件---返回
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
