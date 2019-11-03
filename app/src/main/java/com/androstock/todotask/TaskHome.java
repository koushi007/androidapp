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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Ferdousur Rahman Sarker on 3/16/2018.
 */

public class TaskHome extends AppCompatActivity {

    Activity activity;
    TaskDBHelper mydb;
    NoScrollListView taskListToday, taskListTomorrow, taskListUpcoming,taskListChild;
    NestedScrollView scrollView;
    Button updatebutton;
    ProgressBar loader;
    ArrayList<HashMap<String, String>> todayList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> tomorrowList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> upcomingList = new ArrayList<HashMap<String, String>>();

    public static String KEY_ID = "id";
    public static String KEY_TASK = "task";
    public static String KEY_DESC = "desc";
    public static String KEY_DATE = "date";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_home);

        activity = TaskHome.this;
        mydb = new TaskDBHelper(activity);
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        loader = (ProgressBar) findViewById(R.id.loader);
        taskListToday = (NoScrollListView) findViewById(R.id.taskListToday);
        taskListTomorrow = (NoScrollListView) findViewById(R.id.taskListTomorrow);
        taskListUpcoming = (NoScrollListView) findViewById(R.id.taskListUpcoming);
        TextView titledesc= (TextView) findViewById(R.id.titledesc);
        titledesc.setText("If you chase two rabbits, you catch none.");

    /*
       mydb.insertContact("Acads", "Padhai ki baatein","31/12/2019");
      mydb.insertContact("Self improvement","Reading list, blogs, exercise, etc.","30/12/2019");
      mydb.insertChildContact("Exercise","someday?","27/02/2021","Self improvement");
      mydb.insertChildContact("Reading list","My bucket list:\nHear the Wind Sing\nThe Fountainhead\nAtlas Shrugged\nA prisoner of birth","","Self improvement");
      mydb.insertContact("Research","Pet projects","");
      mydb.insertContact("Hobbies","<3","");
      mydb.insertChildContact("Origami","cranes and tigers.","29/10/2019","Hobbies");
      mydb.insertChildContact("Drum practice!", "Aim:\nHallowed be thy name,\nAcid Rain (LTE)","29/10/2019","Hobbies");
*/

    }


    public void openAddTask(View v)
    {
        Intent i = new Intent(this, AddTask.class);
        startActivity(i);
    }

    public void populateData()
    {
        mydb = new TaskDBHelper(activity);
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

            todayList.clear();
            tomorrowList.clear();
            upcomingList.clear();
        }

        protected String doInBackground(String... args) {
            String xml = "";

            /* ===== TODAY ========*/
            Cursor today = mydb.getDataToday();
            loadDataList(today, todayList);
            /* ===== TODAY ========*/

            /* ===== TOMORROW ========*/
            Cursor tomorrow = mydb.getDataTomorrow();
            //loadDataList(tomorrow, tomorrowList);
            /* ===== TOMORROW ========*/

            /* ===== UPCOMING ========*/
            Cursor upcoming = mydb.getDataUpcoming();
           // loadDataList(upcoming, upcomingList);
            /* ===== UPCOMING ========*/

            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {


            loadListView(taskListToday,todayList);
            //loadListView(taskListTomorrow,tomorrowList);
//            loadListView(taskListUpcoming,upcomingList);




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
        final ListTaskAdapter2 adapter = new ListTaskAdapter2(activity, dataList,-1);
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
                Intent i = new Intent(activity, TaskDetail.class);
                i.putExtra("isUpdate", true);
                i.putExtra("id", dataList.get(+position).get(KEY_ID));
                i.putExtra("task_name",dataList.get(+position).get(KEY_TASK));
                i.putExtra("task_desc",dataList.get(+position).get(KEY_DESC));
                startActivity(i);
            }
            public void onItemSingleClick(AdapterView<?> parent, View view,
                                          int position, long id){

                todayList.clear();
                Cursor today = mydb.getDataToday();
                loadDataList(today, todayList);
                final ListTaskAdapter2 adapter = new ListTaskAdapter2(activity, todayList,position);
                listView.setAdapter(adapter);

            }


        });
    }
}
