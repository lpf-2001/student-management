package com.hrl.studentmanagement.ui.search;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.hrl.studentmanagement.Bean.StudentBean;
import com.hrl.studentmanagement.R;
import com.hrl.studentmanagement.Sql.Sql;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private AutoCompleteTextView atvInput;
    private Button btnSearch;
    private TextView tv_title;
    private ListView listSearch;
    private MyaAapter myAdapter;
    Sql dbHelper;
    SQLiteDatabase database;
    List<StudentBean> studentList;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
         tv_title = (TextView)root.findViewById(R.id.tv_title_le);
        atvInput = (AutoCompleteTextView)root.findViewById(R.id.atv_input);
        btnSearch = (Button) root.findViewById(R.id.btn_search);
        listSearch = (ListView) root.findViewById(R.id.list_search);

        dbHelper = new Sql(getActivity());
        studentList = new ArrayList<StudentBean>();
        myAdapter = new MyaAapter();
        listSearch.setAdapter(myAdapter);
        listSearch.setDivider(null);
        tv_title.setText("搜索");
        btnSearch.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {//模糊搜索
        String key = atvInput.getText().toString().trim();
        try {
            database = dbHelper.getWritableDatabase();
            Cursor cursor = database.rawQuery("select * from studentinfo where name like '%" + key + "%'", null);
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
            listSearch.setAdapter(myAdapter);
        }catch (Exception e){
            Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT).show();
        }

    }

    class MyaAapter extends BaseAdapter {//自定义适配器

        @Override
        public int getCount() {
            return studentList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            StudentBean st = studentList.get(i);
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.search_item, viewGroup, false);
                viewHolder.se_tv_id = view.findViewById(R.id.se_tv_id);
                viewHolder.se_tv_name = view.findViewById(R.id.se_tv_name);
                viewHolder.se_tv_sex = view.findViewById(R.id.se_tv_sex);
                viewHolder.se_tv_age = view.findViewById(R.id.se_tv_age);
                viewHolder.se_tv_aihao = view.findViewById(R.id.se_tv_aihao);
                viewHolder.se_tv_phone = view.findViewById(R.id.se_tv_phone);
                viewHolder.se_tv_date = view.findViewById(R.id.se_tv_date);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.se_tv_id.setText(st.getId() + "");
            viewHolder.se_tv_name.setText(st.getName());
            viewHolder.se_tv_sex.setText(st.getSex());
            viewHolder.se_tv_age.setText(st.getAge());
            viewHolder.se_tv_aihao.setText(st.getAihao());
            viewHolder.se_tv_phone.setText(st.getPhone());
            viewHolder.se_tv_date.setText(st.getDate());
            return view;
        }
        class ViewHolder {
            private TextView se_tv_id, se_tv_name, se_tv_sex,se_tv_age ,se_tv_aihao,se_tv_phone,se_tv_date;
        }
    }
}