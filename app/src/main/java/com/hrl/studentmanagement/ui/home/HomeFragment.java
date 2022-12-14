package com.hrl.studentmanagement.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.hrl.studentmanagement.Bean.StudentBean;
import com.hrl.studentmanagement.R;
import com.hrl.studentmanagement.Sql.Sql;
import com.hrl.studentmanagement.activity.AddinfoActivity;
import com.hrl.studentmanagement.activity.ChangedActivity;
import com.hrl.studentmanagement.activity.DetailedActivity;
;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class HomeFragment extends Fragment implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    private TextView tv_title;
    private Button btnDel,btnQxsel,btnQx;
    private Toolbar toolbar;
    private ListView mlist;
    private Sql dbHelper;
    private SQLiteDatabase database;
    private int index;
    List<StudentBean> studentList;
    private MyAapter myAdapter;
    private boolean a = true;
   




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        tv_title = (TextView)root.findViewById(R.id.tv_title_le);
        btnDel = (Button) root.findViewById(R.id.btn_del);
        btnQxsel = (Button) root.findViewById(R.id.btn_qxsel);
        btnQx = (Button) root.findViewById(R.id.btn_qx);
        mlist = (ListView) root.findViewById(R.id.mlist);
        
        tv_title.setText("??????");
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(this);
        btnDel.setOnClickListener(this);
        btnQxsel.setOnClickListener(this);
        btnQx.setOnClickListener(this);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.add));//??????toolbar??????????????????

        studentList = new ArrayList<StudentBean>();
        dbHelper = new Sql(getActivity());
        database = dbHelper.getWritableDatabase();
        myAdapter = new MyAapter();
        mlist.setAdapter(myAdapter);//???????????????
        mlist.setDivider(null);
        mlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//lisview????????????
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;
                return false;
            }
        });
        mlist.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {//???????????????
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0, 0, 0, "??????????????????");
                contextMenu.add(0, 1, 0, "????????????");
                contextMenu.add(0, 2, 0, "??????????????????");
            }
        });
        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {//lisview??????intent??????
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailedActivity.class);
                StudentBean studentBean = studentList.get(position);
                intent.putExtra("id", studentBean.getId() + "");
                intent.putExtra("name", studentBean.getName() + "");
                intent.putExtra("age", studentBean.getAge() + "");
                intent.putExtra("sex", studentBean.getSex() + "");
                intent.putExtra("aihao", studentBean.getAihao() + "");
                intent.putExtra("phone", studentBean.getPhone() + "");
                intent.putExtra("date", studentBean.getDate() + "");
                startActivity(intent);
            }
        });
        chaXun();
        return root;
    }

    //??????????????????
    class MyAapter extends BaseAdapter {

        @Override
        public int getCount() {
            return studentList.size();
        }
        @Override
        public Object getItem(int i) {
            return studentList.get(i);
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            StudentBean st = studentList.get(i);
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.itmeinfo_layout, viewGroup, false);
                viewHolder.tv_id = view.findViewById(R.id.tv_id);
                viewHolder.tv_name = view.findViewById(R.id.tv_name);
                viewHolder.tv_sex = view.findViewById(R.id.tv_sex);
                viewHolder.tv_age = view.findViewById(R.id.tv_age);
                viewHolder.item_cb = view.findViewById(R.id.item_cb);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tv_id.setText(st.getId() + "");
            viewHolder.tv_name.setText(st.getName());
            viewHolder.tv_sex.setText(st.getSex());
            viewHolder.tv_age.setText(st.getAge());

            return view;
        }
        class ViewHolder {
            private TextView tv_id, tv_name, tv_sex, tv_age ,item_cb;

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_del:
                allDel();
                break;
            case R.id.btn_qxsel:
                allSel();
                myAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_qx:
                cBoxSrateFlase();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_add:
                startActivity(new Intent(getActivity(), AddinfoActivity.class));
                break;
            case R.id.toolbar_select:
                cBoxSratetrue();
                break;
        }
        return true;
    }

    //????????????
    private void chaXun() {
        Cursor cursor = database.query("studentinfo", null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String age = cursor.getString(2);
            String sex = cursor.getString(3);
            String aihao = cursor.getString(4);
            String phone = cursor.getString(5);
            String date = cursor.getString(6);
            StudentBean studentBean = new StudentBean(id, name, age, sex, aihao, phone, date);
            studentList.add(studentBean);
        }
        cursor.close();
        database.close();
    }

    //????????????
    private void allDel() {
        Log.e("???????????????studentList??????", String.valueOf(studentList.size()));
        List<TextView> position1 = new ArrayList<>();
        List<Integer> itemId = new ArrayList<>();
        int childCount = mlist.getChildCount();
        for (int i = 0; i < childCount; i++) {
            CheckBox checkBox = mlist.getChildAt(i).findViewById(R.id.item_cb);
            if (checkBox.isChecked()) {
                TextView textView = mlist.getChildAt(i).findViewById(R.id.tv_id);
                position1.add(textView);
                itemId.add((int) myAdapter.getItemId(i));
            }
        }
         if (position1.size() > 0) {
             AlertDialog.Builder normalDialog = new AlertDialog.Builder(getActivity());
             normalDialog.setTitle("??????????????????");
             normalDialog.setMessage("??????????????????????????????");
             normalDialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     int a = 0;
                     for (int i1 = 0; i1 < position1.size(); i1++) {
                         try {
                             database = dbHelper.getReadableDatabase();
                             a = database.delete("studentinfo", "_id=?", new String[]{(String) position1.get(i1).getText()});
                             database.close();
                         }catch (Exception e){
                             Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                         }
                        }
                        if (a >= 1||position1.size()==itemId.size()) {
                            //Log.e("item??????",""+itemId.size());
                            for (int i = itemId.size() - 1; i >= 0; i--) {
                                int id = itemId.get(i);
                                studentList.remove(id);
                                //Log.e("???????????????studentList??????", String.valueOf(studentList.size()));
                                myAdapter.notifyDataSetChanged();
                                //Log.e("studentList???????????????","?????????");
                                //Log.e("??????item??????", String.valueOf(itemId.get(i)));
                                allSelFale();//????????????checkBox????????????
                            }
                            Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                        }
                        position1.clear();
                        itemId.clear();
                    }
                });
                normalDialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                normalDialog.show();
            } else {
                Toast.makeText(getActivity(), "??????????????????", Toast.LENGTH_SHORT).show();
         }
    }

    //????????????????????????
    public boolean onContextItemSelected(MenuItem item) {
        StudentBean studentBean = studentList.get(index);
        Log.e("position", String.valueOf(index));
        database = dbHelper.getReadableDatabase();
        switch (item.getItemId()) {
            case 0://????????????
                AlertDialog.Builder normalDialog = new AlertDialog.Builder(getActivity());
                normalDialog.setTitle("??????????????????");
                normalDialog.setMessage("??????????????????????????????");
                normalDialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Cursor cursor1 = database.rawQuery("select _id from studentinfo", null);
                            cursor1.moveToPosition(index);
                            int id = cursor1.getInt(0);
                            studentList.remove(index);
                            int a = database.delete("studentinfo", "_id=?", new String[]{id + ""});
                            if (a >= 1) {
                                Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                                myAdapter.notifyDataSetChanged();
                            }
                            database.close();
                        }catch (Exception e){
                            Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                normalDialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                normalDialog.show();
                break;
            case 1:
                //??????intent?????????????????????
                Intent intent = new Intent(getActivity(), DetailedActivity.class);
                intent.putExtra("id", studentBean.getId() + "");
                intent.putExtra("name", studentBean.getName() + "");
                intent.putExtra("age", studentBean.getAge() + "");
                intent.putExtra("sex", studentBean.getSex() + "");
                intent.putExtra("aihao", studentBean.getAihao() + "");
                intent.putExtra("phone", studentBean.getPhone() + "");
                intent.putExtra("date", studentBean.getDate() + "");
                startActivity(intent);
                break;
            case 2:
                //??????intent?????????????????????
                try {
                    Cursor cursor = database.rawQuery("select _id from studentinfo", null);
                    cursor.moveToPosition(index);
                    int id = cursor.getInt(0);
                Intent inte = new Intent(getActivity(), ChangedActivity.class);
                inte.putExtra("id", id + "");
                inte.putExtra("name", studentBean.getName() + "");
                inte.putExtra("age", studentBean.getAge() + "");
                inte.putExtra("sex", studentBean.getSex() + "");
                inte.putExtra("phone", studentBean.getPhone() + "");
                inte.putExtra("date", studentBean.getDate() + "");
                startActivity(inte);
                }catch (Exception e){
                    Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }


    //????????????????????????
    private void cBoxSratetrue() {
        for (int i = 0; i < studentList.size(); i++) {
            CheckBox checkBox = mlist.getChildAt(i).findViewById(R.id.item_cb);
            checkBox.setVisibility(View.VISIBLE);
        }
        btnDel.setVisibility(View.VISIBLE);
        btnQxsel.setVisibility(View.VISIBLE);
        btnQx.setVisibility(View.VISIBLE);
    }

    //????????????????????????
    private void cBoxSrateFlase() {
        for (int i = 0; i < studentList.size(); i++) {
            CheckBox checkBox = mlist.getChildAt(i).findViewById(R.id.item_cb);
            checkBox.setVisibility(View.INVISIBLE);
        }
        btnDel.setVisibility(View.INVISIBLE);
        btnQxsel.setVisibility(View.INVISIBLE);
        btnQx.setVisibility(View.INVISIBLE);
    }

    //??????
    private void allSel() {
        if (a) {
            for (int i = 0; i < studentList.size(); i++) {
                CheckBox checkBox = mlist.getChildAt(i).findViewById(R.id.item_cb);
                checkBox.setChecked(true);
            }
            btnQxsel.setText("????????????");
            a = false;
        } else {
            for (int i = 0; i < studentList.size(); i++) {
                CheckBox checkBox = mlist.getChildAt(i).findViewById(R.id.item_cb);
                checkBox.setChecked(false);
                btnQxsel.setText("??????");
            }
            a = true;
        }
    }

    public void allSelFale(){//???????????????checkBox?????????????????????????????????ListView
        for (int i = 0; i < studentList.size(); i++) {
            CheckBox checkBox = mlist.getChildAt(i).findViewById(R.id.item_cb);
            checkBox.setChecked(false);
        }
    }


}