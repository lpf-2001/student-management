package com.hrl.studentmanagement.ui.logandreg;

import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.regex.*;
import com.hrl.studentmanagement.MD5Util.MD5;
import com.hrl.studentmanagement.R;
import com.hrl.studentmanagement.Sql.Sql;
import com.hrl.studentmanagement.activity.MainActivity;

import java.util.regex.Pattern;


public class RegisterFragment extends Fragment implements View.OnClickListener {

    private TextView tvTitle;
    private Button btn_register,btn_delete;
    private EditText register_phone,register_pwd,register_pwd2;

    Sql dbHelper;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        tvTitle = (TextView)root.findViewById(R.id.tv_title);
        btn_register = (Button)root.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        btn_delete = (Button) root.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);
        register_phone = (EditText)root.findViewById(R.id.et_register_phone);
        register_pwd = (EditText)root.findViewById(R.id.et_register_pwd);
        register_pwd2 = (EditText)root.findViewById(R.id.et_register_pwd2);
        dbHelper = new Sql(getActivity());
        btn_register.setOnClickListener(this);
        tvTitle.setText("管理员注册");
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register://注册
                registerVerify();
                break;
            case R.id.btn_delete://取消注册
                callBackLogin();
                break;
        }

    }

    private void callBackLogin() {
        FragmentTransaction transaction;FragmentManager fragmentManager;
        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.login_main,new LoginFragment());
        transaction.commit();
    }
    private void callBackWelcome(String phone,String password) {
        String userphone = MD5.getMD5(phone);
        String userpwd = MD5.getMD5(password);
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
    private void registerVerify() {
        String phone= register_phone.getText().toString().trim();
        String password= register_pwd.getText().toString().trim();
        String password2= register_pwd2.getText().toString().trim();
        //加密操作
        String pattern = "[a-zA-Z][a-z0-9_A-Z]*[a-z0-9_A-Z]*";
        boolean isMatch = Pattern.matches(pattern, phone);
        String pattern2 = "[a-z0-9_]*";
        boolean isMatch2 = Pattern.matches(pattern2, password);
        String userphone = MD5.getMD5(phone);
        String userpwd = MD5.getMD5(password);
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(getActivity(), "手机号不能为空！", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(getActivity(), "密码不能为空！", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password2)){
            Toast.makeText(getActivity(), "确认密码不能为空！", Toast.LENGTH_SHORT).show();
        }else if(!((phone.length()>=5)&&(phone.length()<=10)&&isMatch)){
            Toast.makeText(getActivity(), "用户名不合要求！", Toast.LENGTH_SHORT).show();
        }else if(!((password.length()>=6)&&(password.length()<=12)&&isMatch2)){//
            Toast.makeText(getActivity(), "密码不合要求！", Toast.LENGTH_SHORT).show();
        }else if (password.equals(password2)){
            try {
                SQLiteDatabase db;
                db = dbHelper.getReadableDatabase();
                ContentValues values = new ContentValues();
                ContentValues values2 = new ContentValues();
                values.put("phone", userphone);
                values.put("password", userpwd);

                values2.put("name", phone);
                values2.put("age", 0);
                values2.put("sex", 0);
                values2.put("aihao",0);
                values2.put("phone", "0");
                values2.put("date", 0);
                Cursor cursor = db.rawQuery("select * from studentinfo where name=?",new String[]{phone});
                String phone_Q = null;
                if (cursor.moveToNext()){
                    phone_Q = cursor.getString(1);
                    System.out.println(phone_Q);
                    System.out.println("existence");
                }
                if (phone_Q ==null){
                long b = db.insert("studentinfo", null, values2);
                long a = db.insert("admininfo", null, values);
                if (a >= 1){
                    Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                    callBackLogin();
                    Log.e("加密",userphone+"/"+userpwd);
                }
                callBackWelcome(userphone,userpwd);
                db.close();
                }else{
                    Toast.makeText(getActivity(), "用户已存在", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Toast.makeText(getActivity(), "注册失败！", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
        }

    }
}
