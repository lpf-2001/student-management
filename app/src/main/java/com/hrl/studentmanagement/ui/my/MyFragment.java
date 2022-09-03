package com.hrl.studentmanagement.ui.my;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.hrl.studentmanagement.R;
import com.hrl.studentmanagement.Sql.Sql;
import com.hrl.studentmanagement.activity.ChangedActivity;
import com.hrl.studentmanagement.activity.LoginActivity;

public class MyFragment extends Fragment implements View.OnClickListener {

    private TextView tv_title,tvUserNmae;
    private LinearLayout lin_infochang,lin_pwdchang;
    private Button loginout;
    private  String info = null;
    private String password;
    private String phone;
    Sql dbHelper;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my, container, false);
        lin_infochang = (LinearLayout)root.findViewById(R.id.chang_info);
        lin_pwdchang = (LinearLayout)root.findViewById(R.id.chang_pwd);
        loginout = (Button)root.findViewById(R.id.btn_loginout);
        tvUserNmae = (TextView)root.findViewById(R.id.tv_username);
        tv_title = (TextView)root.findViewById(R.id.tv_title_le);
        tv_title.setText("我的");
        lin_infochang.setOnClickListener(this);
        lin_pwdchang.setOnClickListener(this);
        loginout.setOnClickListener(this);
        dbHelper = new Sql(getActivity());
        getuserName();
        return root;
    }

    private void getuserName() {
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        phone=sharedPreferences.getString("phone","");
        password = sharedPreferences.getString("password","");
        Log.e("phone",phone);
        Log.e("password",password);
        String  username= null;
        try {
            SQLiteDatabase db;
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select username from admininfo where phone=? and password=?",new String[]{phone,password});
            if (cursor.moveToNext()){
                username = cursor.getString(0);
                Log.e("用户名",username);
                tvUserNmae.setText(username+"管理员");
            }else {
                tvUserNmae.setText("请设置用户名");
            }
            db.close();
        } catch (Exception e){
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chang_info:
                showDialogInfo();
                break;
            case R.id.chang_pwd:
                break;
            case R.id.btn_loginout:
                loginOut();
                break;
        }
    }

    private void loginOut() {//退出登录
        SharedPreferences sp = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        android.app.AlertDialog.Builder dDialog = new android.app.AlertDialog.Builder(getActivity());
        dDialog.setTitle("提示信息");
        dDialog.setMessage("确定要退出吗？");
        dDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor.clear();
                editor.commit();//清空数据
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        dDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dDialog.show();
    }

    public void showDialogInfo(){//展示自定义对话框

       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
       View view = inflater.inflate(R.layout.dialog, null);

       EditText et_dl_info = (EditText) view.findViewById(R.id.et_dl_info);
       Button btn_dl_ok = (Button) view.findViewById(R.id.btn_dl_ok);
       final Dialog dialog = builder.create();
       dialog.show();
       dialog.getWindow().setContentView(view);
       Window window = dialog.getWindow();
       WindowManager.LayoutParams params = window.getAttributes();
       params.width = WindowManager.LayoutParams.WRAP_CONTENT;
       params.height = WindowManager.LayoutParams.WRAP_CONTENT;
       params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
       params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
       params.dimAmount = 0.5f;
       window.setAttributes(params);
       btn_dl_ok.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               info = et_dl_info.getText().toString().trim();
               if (TextUtils.isEmpty(info)){
                   Toast.makeText(getActivity(),"用户名不能为空！", Toast.LENGTH_SHORT).show();
               }else {
                   changInfo();
                   dialog.dismiss();
                   getuserName();
               }
           }
       });
   }
   public void changInfo(){
       try {
           SQLiteDatabase db;
           db = dbHelper.getReadableDatabase();
           ContentValues values = new ContentValues();
           values.put("username", info);
           int a = db.update("admininfo", values, "phone= ? and password = ?", new String[]{phone,password});
           if (a >= 1) {
               Toast.makeText(getActivity(), "信息更新成功", Toast.LENGTH_SHORT).show();
           }db.close();
       }catch (Exception e){
           Toast.makeText(getActivity(), "更新失败！", Toast.LENGTH_SHORT).show();
       }
   }
}