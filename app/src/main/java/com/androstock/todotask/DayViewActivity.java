package com.androstock.todotask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


/**
 * Created by Ferdousur Rahman Sarker on 3/16/2018.
 */

public class DayViewActivity extends AppCompatActivity {

    Activity activity;
    TaskDBHelper mydb;
    NoScrollListView taskListToday, taskListTomorrow, taskListParent, taskListChild;
    NestedScrollView scrollView;
    Button updatebutton;
    ProgressBar loader;
    ArrayList<HashMap<String, String>> ChildList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> tomorrowList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> ParentList = new ArrayList<HashMap<String, String>>();
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    public static String KEY_ID = "id";
    public static String KEY_TASK = "task";
    public static String KEY_DESC = "desc";
    public static String KEY_DATE = "date";
    public static String KEY_PARENT="parent";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);

        activity = DayViewActivity.this;
        mydb = new TaskDBHelper(activity);
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        loader = (ProgressBar) findViewById(R.id.loader);
        taskListChild = (NoScrollListView) findViewById(R.id.taskListToday);
        taskListTomorrow = (NoScrollListView) findViewById(R.id.taskListTomorrow);
        taskListParent = (NoScrollListView) findViewById(R.id.taskListUpcoming);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


    }


    public void openAddTask(View v) {
        Intent i = new Intent(this, AddTask.class);
        startActivity(i);
    }


    public void populateData() {
        mydb = new TaskDBHelper(activity);
        scrollView.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);

        LoadTask loadTask = new LoadTask();
        loadTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();

        //populateData();

    }


    class LoadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ChildList.clear();
            tomorrowList.clear();
            ParentList.clear();
        }

        protected String doInBackground(String... args) {
            String xml = "";

            /* ===== TODAY ========*/
            Cursor today = mydb.getDataToday();
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {


            loader.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }


    public void loadDataList(Cursor cursor, ArrayList<HashMap<String, String>> dataList,int s) {
        if (cursor != null && s==0) {
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

        if (cursor != null && s==1) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                HashMap<String, String> mapToday = new HashMap<String, String>();
                mapToday.put(KEY_ID, cursor.getString(0).toString());
                mapToday.put(KEY_TASK, cursor.getString(1).toString());
                mapToday.put(KEY_DESC, cursor.getString(2).toString());
                mapToday.put(KEY_PARENT,cursor.getString(4).toString());
                mapToday.put(KEY_DATE, Function.Epoch2DateString(cursor.getString(3).toString(), "dd-MM-yyyy"));
                dataList.add(mapToday);
                cursor.moveToNext();
            }
        }
    }


    public void loadListView(ListView listView, final ArrayList<HashMap<String, String>> dataList,final int s) {
        ListTaskAdapter_Day adapter = new ListTaskAdapter_Day(DayViewActivity.this, dataList,s);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            final int num=s;
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

               if(num==0){
                    Intent i = new Intent(activity, AddTask.class);
                    i.putExtra("isUpdate", true);
                    i.putExtra("id", dataList.get(+position).get(KEY_ID));
                    startActivity(i);
            }
               else{
                   Intent i = new Intent(activity, AddSubTask.class);
                   i.putExtra("isUpdate", true);
                   i.putExtra("id", dataList.get(+position).get(KEY_ID));
                   i.putExtra("parent",dataList.get(+position).get(KEY_PARENT));
                   startActivity(i);
               }


        }});
    }


    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "select date",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    int monthAddOne = arg2 + 1;
                    String date = (arg3 < 10 ? "0" + arg3 : "" + arg3) + "/" +
                            (monthAddOne < 10 ? "0" + monthAddOne : "" + monthAddOne) + "/" +
                            arg1;
                    String Year=new Integer(arg1).toString();
                    String Month=new Integer(arg2).toString();
                    String Day=new Integer(arg3).toString();
                    TextView title=(TextView) findViewById(R.id.title);
                    title.setText(date);
                    /* ===== TODAY ========*/
                    Cursor parentlist = mydb.getDataonDate_Parent(date);
                    ParentList.clear();
                    ChildList.clear();
                    loadDataList(parentlist, ParentList,0);
                    loadListView(taskListParent,ParentList,0);
                    Cursor childlist=mydb.getDataonDate_Child(date);
                    loadDataList(childlist,ChildList,1);
                    loadListView(taskListChild,ChildList,1);
                }
            };

}