package com.androstock.todotask;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ferdousur Rahman Sarker on 10/23/2017.
 */

public class ListTaskAdapter_Day extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    public int state=0,pos;
    TaskDBHelper mydb=new TaskDBHelper(activity);

    public ListTaskAdapter_Day(Activity a, ArrayList<HashMap<String, String>> d,int s) {
        activity = a;
        data=d;
        state= s;

        mydb=new TaskDBHelper(a);
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
        ListTaskViewHolder_Day holder = null;
        if (convertView == null) {
            holder = new ListTaskViewHolder_Day();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.list_row_day, parent, false);
            holder.task_image = (TextView) convertView.findViewById(R.id.task_image);
            holder.task_name = (TextView) convertView.findViewById(R.id.task_name);
            holder.task_date = (TextView) convertView.findViewById(R.id.task_date);
            holder.hierarchy = (TextView) convertView.findViewById(R.id.hierarchy);
            holder.task_desc = (TextView) convertView.findViewById(R.id.task_desc);
            convertView.setTag(holder);
        } else {
            holder = (ListTaskViewHolder_Day) convertView.getTag();
        }
        holder.task_image.setId(position);
        holder.task_name.setId(position);
        holder.task_date.setId(position);
        holder.hierarchy.setId(position);
        holder.task_desc.setId(position);
        holder.hierarchy.setVisibility(View.VISIBLE);







        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);

        try{
            holder.task_name.setText(song.get(TaskHome.KEY_TASK));
            holder.task_date.setText(song.get(TaskHome.KEY_DATE));
            holder.task_desc.setText("Description :" +song.get(TaskHome.KEY_DESC));
            if(state==0)
            {
                holder.hierarchy.setText("Hierarchy: root/"+song.get(TaskHome.KEY_TASK));
            }
            else if(state==1)
            {
                Cursor parent1=mydb.getParent(song.get(TaskHome.KEY_TASK));
                 parent1.moveToFirst();
                 String p=parent1.getString(4);
                holder.hierarchy.setText("Heirarchy : root/"+p+"/"+song.get(TaskHome.KEY_TASK));
            }
            /* Image */
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(getItem(position));
            holder.task_image.setTextColor(color);
            holder.task_image.setText(Html.fromHtml("&#11044;"));
            /* Image */

        }catch(Exception e) {}
        return convertView;
    }
}

class ListTaskViewHolder_Day {
    TextView task_image;
    TextView task_name, task_date,task_desc;
    TextView hierarchy;
    Button editbutton;
    NoScrollListView taskchildlist;
    ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();

}