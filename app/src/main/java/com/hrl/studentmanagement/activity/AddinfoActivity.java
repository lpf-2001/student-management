package com.hrl.studentmanagement.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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

import com.hrl.studentmanagement.MD5Util.MD5;
import com.hrl.studentmanagement.R;
import com.hrl.studentmanagement.Sql.Sql;
import java.util.Calendar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddinfoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_age)
    EditText etAge;
    @BindView(R.id.rb_man)
    RadioButton rbMan;
    @BindView(R.id.rb_wman)
    RadioButton rbWman;
    @BindView(R.id.cb_sing)
    CheckBox cbSing;
    @BindView(R.id.cb_dance)
    CheckBox cbDance;
    @BindView(R.id.cb_fitness)
    CheckBox cbFitness;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.btn_remove)
    Button btnRemove;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String name, sex, dates, sing, dance, fitness, age, phone, date;
    Sql dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //自定义状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.Light_Blue));
        }
        setContentView(R.layout.activity_addinfo);
        ButterKnife.bind(this);
        dbHelper = new Sql(this);
        tvTitle.setText("添加学生信息");//设置标题
        setToolBar();//标题栏返回箭头
    }
    @OnClick({R.id.rb_man, R.id.rb_wman, R.id.cb_sing, R.id.cb_dance, R.id.cb_fitness, R.id.btn_remove, R.id.btn_save, R.id.tv_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_man:
                if (rbMan.isChecked()) {
                    rbWman.setChecked(false);
                    sex = rbMan.getText().toString();
                }
                break;
            case R.id.rb_wman:
                if (rbWman.isChecked()) {
                    rbMan.setChecked(false);
                    sex = rbWman.getText().toString();
                }
                break;
            case R.id.cb_sing:
                if (cbSing.isChecked()) {
                    sing = cbSing.getText().toString();
                }
                break;
            case R.id.cb_dance:
                if (cbDance.isChecked()) {
                    dance = cbDance.getText().toString();
                }
                break;
            case R.id.cb_fitness:
                if (cbFitness.isChecked()) {
                    fitness = cbFitness.getText().toString();
                }
                break;
            case R.id.tv_date:
                riLi();//日历框
                break;
            case R.id.btn_remove:
                yiChu();//重置功能
                break;
            case R.id.btn_save:
                save();//判断输入值空值与合法性并插入数据
                break;
        }
    }
    private void riLi() {//日历框
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
    private void save() {//判断输入值空值与合法性插入数据
        name = etName.getText().toString().trim();
        age = etAge.getText().toString().trim();
        phone = etPhone.getText().toString().trim();
        date = tvDate.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(AddinfoActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(age)) {
            Toast.makeText(AddinfoActivity.this, "年龄不能为空", Toast.LENGTH_SHORT).show();
//        } else if(age.equals("101")||age.equals("0")){
//            Toast.makeText(AddinfoActivity.this, "年龄格式不正确", Toast.LENGTH_SHORT).show();
        }else if (!rbWman.isChecked() && !rbMan.isChecked()) {
            Toast.makeText(AddinfoActivity.this, "性别不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(AddinfoActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
//        } else if (phone.length() < 11) {
//            Toast.makeText(AddinfoActivity.this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(date)) {
            Toast.makeText(AddinfoActivity.this, "日期不能为空", Toast.LENGTH_SHORT).show();
        } else {//插入数据
            SQLiteDatabase db;
            db = dbHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("age", age);
            values.put("sex", sex);
            values.put("aihao", sing + "," + dance + "," + fitness);
            values.put("phone", phone);
            values.put("date", date);
            String phone_Q = null;
            ContentValues values2 = new ContentValues();
            String userphone = MD5.getMD5(name);
            String userpwd = MD5.getMD5("123456");
            values2.put("phone", userphone);
            values2.put("password", userpwd);
            Cursor cursor = db.rawQuery("select * from studentinfo where name=?",new String[]{name});
            if (cursor.moveToNext()){
                phone_Q = cursor.getString(1);
                System.out.println(phone_Q);
                System.out.println("existence");
            }
            if (phone_Q ==null){
            long a = db.insert("studentinfo", null, values);
            long b = db.insert("admininfo", null, values2);
            if (a >= 1){
                Toast.makeText(AddinfoActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
            }
            db.close();
            startActivity(new Intent(AddinfoActivity.this, MainActivity.class));
            //finish();
            }else{
                Toast.makeText(AddinfoActivity.this, "已存在该用户", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void yiChu() {//重置功能
        etName.setText(null);
        etAge.setText(null);
        etPhone.setText(null);
        tvDate.setText(null);
        rbMan.setChecked(false);
        rbWman.setChecked(false);
        cbSing.setChecked(false);
        cbDance.setChecked(false);
        cbFitness.setChecked(false);
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        dates = String.format("%d-%d-%d", year, month + 1, dayOfMonth);
        tvDate.setText(dates);//设置时间
    }
    private void setToolBar() {//返回箭头不显示
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setTitle("");
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
