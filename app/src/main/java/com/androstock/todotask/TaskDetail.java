package com.androstock.todotask;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class TaskDetail extends AppCompatActivity {

    Activity activity;
    TaskDBHelper mydb;
    NoScrollListView taskListChild;
    NestedScrollView scrollView;
    ProgressBar loader;
    String Taskname,Taskdesc;
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
        setContentView(R.layout.activity_task_detail);

        intent = getIntent();
        isUpdate = intent.getBooleanExtra("isUpdate", true);

        mydb = new TaskDBHelper(getApplicationContext());
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        loader = (ProgressBar) findViewById(R.id.loader);
        taskListChild = (NoScrollListView) findViewById(R.id.taskListChild);

        id = intent.getStringExtra("id");
        Taskname=intent.getStringExtra("task_name");
        Taskdesc=intent.getStringExtra("task_desc");
        TextView task_name=(TextView) findViewById(R.id.task_title);
        task_name.setText(Taskname);
        TextView task_desc=(TextView) findViewById(R.id.desc);
        task_desc.setText("DESCRIPTION: "+Taskdesc);

    }



    public void openAddSubTask(View v)
    {
        Intent i = new Intent(this, AddSubTask.class);
        i.putExtra("parent",Taskname);
        startActivity(i);
    }

    public void populateData()
    {
        mydb = new TaskDBHelper(getApplicationContext());
        scrollView.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);

        LoadTask loadTask = new LoadTask();
        loadTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();

       populateData();

    }





    class LoadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ChildList.clear();

        }

        protected String doInBackground(String... args) {
            String xml = "";

            /* ===== TODAY ========*/
            Cursor today = mydb.getChildDataSpecificWithParent(Taskname);
            loadDataList(today, ChildList);
            /* ===== TODAY ========*/

            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {


            loadListView(taskListChild,ChildList);

            loader.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }



    public void loadDataList(Cursor cursor, ArrayList<HashMap<String, String>> dataList)
    {
        if(cursor!=null ) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                HashMap<String, String> mapToday = new HashMap<String, String>();
                mapToday.put(KEY_ID, cursor.getString(0).toString());
                mapToday.put(KEY_TASK, cursor.getString(1).toString());
                mapToday.put(KEY_DESC, cursor.getString(2).toString());

                mapToday.put(KEY_DATE, Function.Epoch2DateString(cursor.getString(3).toString(), "dd-MM-yyyy"));
                dataList.add(mapToday);
                cursor.moveToNext();
            }
        }
    }


    public void loadListView(final ListView listView, final ArrayList<HashMap<String, String>> dataList)
    {
        ListTaskAdapter3 adapter = new ListTaskAdapter3(TaskDetail.this, dataList,-1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            long mLastClickTime=0;
            private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

            long lastClickTime = 0;
            boolean tap = true;

            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long l) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                    onItemDoubleClick(adapterView,view,position,l);
                    tap = false;
                } else
                    tap = true;

                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(tap)
                            onItemSingleClick(adapterView,view,position,l);
                    }
                },DOUBLE_CLICK_TIME_DELTA);

                lastClickTime = clickTime;
            }
            public void onItemDoubleClick(AdapterView<?> parent, View view,
                                          int position, long id) {
                Intent i = new Intent(TaskDetail.this, SubTaskDetail.class);
                i.putExtra("isUpdate", true);
                i.putExtra("id", dataList.get(+position).get(KEY_ID));
                i.putExtra("task_name",dataList.get(+position).get(KEY_TASK));
                i.putExtra("task_desc",dataList.get(+position).get(KEY_DESC));
                i.putExtra("parent",Taskname);
                startActivity(i);
            }

            public void onItemSingleClick(AdapterView<?> parent, View view,
            int position, long id){
                ChildList.clear();
                Cursor today = mydb.getChildDataSpecificWithParent(Taskname);
                loadDataList(today, ChildList);
                final ListTaskAdapter2 adapter = new ListTaskAdapter2(TaskDetail.this, ChildList,position);
                listView.setAdapter(adapter);

            }
        });
    }
}
