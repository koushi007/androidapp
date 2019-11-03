package com.androstock.todotask;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


/**
 * Created by Ferdousur Rahman Sarker on 3/16/2018.
 */

public class SubTaskDetail extends AppCompatActivity {

    Activity activity;
    TaskDBHelper mydb;
    NoScrollListView taskListChild;
    NestedScrollView scrollView;
    ProgressBar loader;
    String parent;
    String Taskname;
    String Taskdate;
    String Taskdesc;
    ArrayList<HashMap<String, String>> ChildList = new ArrayList<HashMap<String, String>>();
    public static String KEY_ID = "id";
    public static String KEY_TASK = "task";
    public static String KEY_DESC = "desc";
    public static String KEY_DATE = "date";

    Intent intent;
    Boolean isUpdate;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_task_detail);

        intent = getIntent();
        isUpdate = intent.getBooleanExtra("isUpdate", true);

        mydb = new TaskDBHelper(getApplicationContext());
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        loader = (ProgressBar) findViewById(R.id.loader);
        taskListChild = (NoScrollListView) findViewById(R.id.taskListChild);

        id = intent.getStringExtra("id");
        Taskname = intent.getStringExtra("task_name");
        Taskdate = intent.getStringExtra("task_date");
        Taskdesc = intent.getStringExtra("task_desc");
        parent = intent.getStringExtra("parent");
        TextView task_name = (TextView) findViewById(R.id.task_name);
        task_name.setText(Taskname);
        TextView task_date = (TextView) findViewById(R.id.task_date);
        task_date.setText(Taskdate);
        TextView task_desc = (TextView) findViewById(R.id.task_desc);
        task_desc.setText("Description :" +Taskdesc);

    }


    public void openAddSubTask(View v) {


        Intent i = new Intent(SubTaskDetail.this, AddSubTask.class);
        i.putExtra("isUpdate", true);
        i.putExtra("parent", parent);
        i.putExtra("id", id);
        startActivity(i);
    }
}





