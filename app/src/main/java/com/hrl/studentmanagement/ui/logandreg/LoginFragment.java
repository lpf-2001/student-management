package com.hrl.studentmanagement.ui.logandreg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.regex.*;
import com.hrl.studentmanagement.MD5Util.MD5;
import com.hrl.studentmanagement.R;
import com.hrl.studentmanagement.Sql.Sql;
import com.hrl.studentmanagement.activity.MainActivity;

import butterknife.OnClick;


public class LoginFragment extends Fragment implements View.OnClickListener {


    private TextView tvTitle;
    private EditText  etLonginphone,etLonginPwd;
    private Button btnlogin,btn_go_register;
    private CheckBox rb_pwd,atuo_lg;
    Sql dbHelper;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private boolean state;
    private String phone;
    private  String password;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_login, container, false);



        tvTitle = (TextView)root.findViewById(R.id.tv_title);

        etLonginphone = (EditText)root.findViewById(R.id.et_longin_phone);
        etLonginPwd = (EditText)root.findViewById(R.id.et_longin_pwd);

        btnlogin = (Button)root.findViewById(R.id.btn_login);
        btnlogin.setOnClickListener(this);
        btn_go_register = (Button)root.findViewById(R.id.btn_go_register);
        btn_go_register.setOnClickListener(this);
        rb_pwd = (CheckBox)root.findViewById(R.id.rb_pwd);
        atuo_lg = (CheckBox)root.findViewById(R.id.atuo_lg);
        dbHelper = new Sql(getActivity());
        rememberPwd();
        autoLg();
        tvTitle.setText("管理员登录");
        return root;
    }

    private void autoLg() {//自动登录
        SharedPreferences sp = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        boolean auto = sp.getBoolean("auto", false);
        String pho = sp.getString("pho", "");
        String pwd = sp.getString("pwd", "");
        if (auto){
            loginVerify(pho,pwd);
        }
    }

    private void rememberPwd() {//记住密码判断
        SharedPreferences sp = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        boolean state = sp.getBoolean("state", false);
        String pwd = sp.getString("pwd", "");
        if (state){
             etLonginPwd.setText(pwd);
             rb_pwd.setChecked(true);
         }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_go_register://去注册界面，当前：登录界面
                fragmentManager = getActivity().getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.login_main,new RegisterFragment());
                transaction.commit();
                tvTitle.setText("管理员注册");
                break;
            case R.id.btn_login://登录
                phone= etLonginphone.getText().toString().trim();
                password= etLonginPwd.getText().toString().trim();
                loginVerify(phone,password);
                checkState();
                break;
        }

    }

    public void checkState() {
        SharedPreferences sp = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (rb_pwd.isChecked()){
            editor.putBoolean("state",rb_pwd.isChecked());
            editor.commit();
        }else {
            editor.putBoolean("state",false);
            editor.commit();
        }
        if (atuo_lg.isChecked()){
            editor.putBoolean("auto",atuo_lg.isChecked());
            editor.commit();
        }else {
            editor.putBoolean("auto",false);
            editor.commit();
        }
    }

    public void loginVerify(String phone,String password){
        String pattern = "[a-zA-Z][a-z0-9_A-Z]*[a-z0-9_A-Z]*";
        boolean isMatch = Pattern.matches(pattern, phone);
        //加密操作
        String userphone = MD5.getMD5(phone);
        String userpwd = MD5.getMD5(password);
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(getActivity(), "用户名不能为空！", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(getActivity(), "密码不能为空！", Toast.LENGTH_SHORT).show();
        }else if(!((phone.length()>=5)&&(phone.length()<=10)&&isMatch)){
            Toast.makeText(getActivity(), "用户名不合要求！", Toast.LENGTH_SHORT).show();
        }else {
            SQLiteDatabase db;
            db = dbHelper.getReadableDatabase();
            String phone_Q = null;
            try {
                Cursor cursor = db.rawQuery("select * from admininfo where phone=? and password=?",new String[]{userphone,userpwd});
                if (cursor.moveToNext()){
                    phone_Q = cursor.getString(3);
                }
            } catch (Exception e){
                Toast.makeText(getActivity(), "有问题！"+phone, Toast.LENGTH_SHORT).show();
            }
            db.close();
            if (phone_Q ==null){
                Toast.makeText(getActivity(), "没有该用户！", Toast.LENGTH_SHORT).show();
            }else {
                startActivity(new Intent(getActivity(), MainActivity.class));
                SharedPreferences sp = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("phone",userphone);
                editor.putString("password",userpwd);
                editor.putString("pho",phone);
                editor.putString("pwd",password);
                editor.commit();
                getActivity().finish();

            }
        }
    }


}
