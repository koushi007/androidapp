package com.androstock.todotask;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ferdousur Rahman Sarker on 10/23/2017.
 */

public class ListTaskAdapter2 extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    TaskDBHelper mydb=new TaskDBHelper(activity);
    public int pos;
    public static String KEY_ID = "id";
    public static String KEY_TASK = "task";
    public static String KEY_DESC = "desc";
    public static String KEY_DATE = "date";

    public ListTaskAdapter2(Activity a, ArrayList<HashMap<String, String>> d,int p) {
        activity = a;
        data=d;
        mydb=new TaskDBHelper(a);
        pos=p;
    }
    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListTaskViewHolder2 holder = null;
        if (convertView == null) {
            holder = new ListTaskViewHolder2();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.task_list_row, parent, false);
            holder.task_image = (TextView) convertView.findViewById(R.id.task_image);
            holder.task_name = (TextView) convertView.findViewById(R.id.task_name);
            holder.task_desc = (TextView) convertView.findViewById(R.id.task_desc);
            holder.task_date = (TextView) convertView.findViewById(R.id.task_date);
            holder.editbutton = (Button) convertView.findViewById(R.id.editbutton);
            holder.taskchildlist=(NoScrollListView) convertView.findViewById(R.id.taskchildlist);
            convertView.setTag(holder);
        } else {
            holder = (ListTaskViewHolder2) convertView.getTag();
        }

        if(pos==position) {
            holder.editbutton.setVisibility(View.VISIBLE);
            holder.taskchildlist.setVisibility(View.VISIBLE);
            holder.task_desc.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.editbutton.setVisibility(View.GONE);
            holder.taskchildlist.setVisibility(View.GONE);

            holder.task_desc.setVisibility(View.GONE);

        }
        holder.task_image.setId(position);
        holder.task_name.setId(position);
        holder.task_date.setId(position);
        holder.task_desc.setId(position);
        holder.editbutton.setId(position);
        holder.taskchildlist.setId(position);
        holder.editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent (activity,AddTask.class);
                i.putExtra("isUpdate", true);
                i.putExtra("id", data.get(position).get(KEY_ID));
                activity.startActivity(i);
            }
        });

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);

        try{
            holder.task_name.setText(song.get(TaskHome.KEY_TASK));
            holder.task_date.setText(song.get(TaskHome.KEY_DATE));
            holder.task_desc.setText("Description :"+song.get(TaskHome.KEY_DESC));
           if(pos==position){
            Cursor childlist=mydb.getChildDataSpecificWithParent(song.get(KEY_TASK));
            holder.childList.clear();
            loadDataList(childlist,holder.childList);

            loadListView(holder.taskchildlist,holder.childList);
            /* Image */}
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(getItem(position));
            holder.task_image.setTextColor(color);
            holder.task_image.setText(Html.fromHtml("&#11044;"));
            /* Image */

        }catch(Exception e) {}
        return convertView;
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

    public void loadListView(ListView listView, final ArrayList<HashMap<String, String>> dataList) {
        ListTaskAdapter adapter = new ListTaskAdapter(activity, dataList);
        listView.setAdapter(adapter);


    }




}





class ListTaskViewHolder2 {
    TextView task_image;
    TextView task_name, task_date,task_desc;
    Button editbutton;
    NoScrollListView taskchildlist;
    ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();

}