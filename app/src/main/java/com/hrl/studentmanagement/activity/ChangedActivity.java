package com.hrl.studentmanagement.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hrl.studentmanagement.R;
import com.hrl.studentmanagement.Sql.Sql;

import java.util.Calendar;

import butterknife.ButterKnife;

public class ChangedActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {


    private TextView cgTvDate,cgTvId,tvTitle;
    private Button cgBtnSave;
    private RadioButton cgRbWman,cgRbMan;
    private EditText cgEtPhone,cgEtName,cgEtAge;
    private CheckBox cgCbFitness,cgCbDance,cgCbSing;
    private Toolbar toolbar;

    private String name;
    private String sex;
    private String dates;
    private String sing;
    private String dance;
    private String fitness;
    private String age;
    private String phone;
    private String date;
    Sql dbHelper;

    public void init(){
        tvTitle = (TextView)findViewById(R.id.tv_title);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        cgTvDate = (TextView)findViewById(R.id.cg_tv_date);
        cgTvId = (TextView)findViewById(R.id.cg_tv_id);
        cgBtnSave = (Button)findViewById(R.id.cg_btn_save);//
        cgBtnSave.setOnClickListener(this);
        cgRbWman = (RadioButton) findViewById(R.id.cg_rb_wman);//
        cgRbWman.setOnClickListener(this);
        cgRbMan = (RadioButton) findViewById(R.id.cg_rb_man);//
        cgRbMan.setOnClickListener(this);
        cgEtPhone = (EditText) findViewById(R.id.cg_et_phone);
        cgEtName = (EditText) findViewById(R.id.cg_et_name);
        cgEtAge = (EditText) findViewById(R.id.cg_et_age);
        cgCbFitness = (CheckBox) findViewById(R.id.cg_cb_fitness);//
        cgCbFitness.setOnClickListener(this);
        cgCbDance = (CheckBox) findViewById(R.id.cg_cb_dance);//
        cgCbDance.setOnClickListener(this);
        cgCbSing = (CheckBox) findViewById(R.id.cg_cb_sing);//
        cgCbSing.setOnClickListener(this);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //??????????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.Light_Blue));
        }
        setContentView(R.layout.activity_changed);
        init();
        ButterKnife.bind(this);
        dbHelper = new Sql(this);
        tvTitle.setText("????????????");//????????????
        setToolBar();//?????????????????????
        fuzhi();//?????????????????????
    }
    private void fuzhi() {//?????????????????????
        Intent intent = getIntent();
        cgTvId.setText(intent.getStringExtra("id"));
        cgEtName.setText(intent.getStringExtra("name"));
        cgEtAge.setText(intent.getStringExtra("age"));
        String sex1 = intent.getStringExtra("sex");
        cgEtPhone.setText(intent.getStringExtra("phone"));
        cgTvDate.setText(intent.getStringExtra("date"));
        if (sex1.equals("???")) {
            cgRbMan.setChecked(true);
        }
        if (sex1.equals("???")) {
            cgRbWman.setChecked(true);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cg_rb_man:
                if (cgRbMan.isChecked()) {
                    cgRbWman.setChecked(false);
                    sex = cgRbMan.getText().toString();
                }
                break;
            case R.id.cg_rb_wman:
                if (cgRbWman.isChecked()) {
                    cgRbMan.setChecked(false);
                    sex = cgRbWman.getText().toString();
                }
                break;
            case R.id.cg_cb_sing:
                if (cgCbSing.isChecked()) {
                    sing = cgCbSing.getText().toString();
                }
                break;
            case R.id.cg_cb_dance:
                if (cgCbDance.isChecked()) {
                    dance = cgCbDance.getText().toString();
                }
                break;
            case R.id.cg_cb_fitness:
                if (cgCbFitness.isChecked()) {
                    fitness = cgCbFitness.getText().toString();
                }
                break;
            case R.id.cg_tv_date:
                rili();//??????
                break;
            case R.id.cg_btn_save:
                xiugai();//????????????????????????????????????????????????
                break;
        }
    }

    private void rili() {//??????
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void xiugai() {//?????????????????????????????????
        name = cgEtName.getText().toString().trim();
        age = cgEtAge.getText().toString().trim();
        phone = cgEtPhone.getText().toString().trim();
        date = cgTvDate.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(ChangedActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(age)) {
            Toast.makeText(ChangedActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
        } else if (!cgRbWman.isChecked() && !cgRbMan.isChecked()) {
            Toast.makeText(ChangedActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(ChangedActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
//        } else if (phone.length() < 11) {
//            Toast.makeText(ChangedActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(date)) {
            Toast.makeText(ChangedActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
        } else {
            //????????????
            String id = cgTvId.getText().toString().trim();
            SQLiteDatabase db;
            db = dbHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("age", age);
            values.put("sex", sex);
            values.put("aihao", sing + dance + fitness);
            values.put("phone", phone);
            values.put("date", date);
            int a = db.update("studentinfo", values, "_id = ?", new String[]{id});
            if (a >= 1) {
                Toast.makeText(ChangedActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
            }
            db.close();
            startActivity(new Intent(ChangedActivity.this, MainActivity.class));
            finish();
        }
    }



    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        dates = String.format("%d-%d-%d", year, month + 1, dayOfMonth);
        cgTvDate.setText(dates);//????????????
    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    //Toolbar?????????---??????
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
