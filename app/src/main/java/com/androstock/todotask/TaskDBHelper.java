package com.androstock.todotask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ferdousur Rahman Sarker on 3/19/2018.
 */

public class TaskDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "New9.db";
    public static final String CONTACTS_TABLE_NAME = "todo_parent";
    public static final String CHILD_TABLE_NAME="todo_child";

    public TaskDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL(
                "CREATE TABLE "+CONTACTS_TABLE_NAME +
                        "(id INTEGER PRIMARY KEY, task TEXT,description TEXT, dateStr INTEGER, hierarchy TEXT)"
        );
        db.execSQL(
                "CREATE TABLE "+CHILD_TABLE_NAME +
                        "(id INTEGER PRIMARY KEY, task TEXT,description TEXT, dateStr INTEGER , parent TEXT ,hierarchy TEXT)"
        );


        ContentValues contentValues = new ContentValues();
        contentValues.put("task", "Acads");
        contentValues.put("description","Padhai ki baatein" );
        contentValues.put("dateStr", getDate("31/12/2019"));
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);


        contentValues.put("task", "Self improvement");
        contentValues.put("description","Reading list, blogs, exercise, etc." );
        contentValues.put("dateStr", getDate("30/12/2019"));
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);

        contentValues.put("task", "Research");
        contentValues.put("description","Pet projects" );
        contentValues.put("dateStr", getDate(""));
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);


        contentValues.put("task","Hobbies");
        contentValues.put("description","<3" );
        contentValues.put("dateStr", getDate(""));
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);

        contentValues.put("task", "Exercise");
        contentValues.put("description","someday?" );
        contentValues.put("dateStr", getDate("27/02/2021"));
        contentValues.put("parent","Self improvement");
        db.insert(CHILD_TABLE_NAME, null, contentValues);

        contentValues.put("task", "Reading list");
        contentValues.put("description","My bucket list:\nHear the Wind Sing\nThe Fountainhead\nAtlas Shrugged\nA prisoner of birth" );
        contentValues.put("dateStr", getDate(""));
        contentValues.put("parent","Self improvement");
        db.insert(CHILD_TABLE_NAME, null, contentValues);




        contentValues.put("task", "Origami");
        contentValues.put("description","cranes and tigers." );
        contentValues.put("dateStr", getDate("29/10/2019"));
        contentValues.put("parent","Hobbies");
        db.insert(CHILD_TABLE_NAME, null, contentValues);

        contentValues.put("task", "Drum practice!");
        contentValues.put("description","Aim:\nHallowed be thy name,\nAcid Rain (LTE)" );
        contentValues.put("dateStr", getDate("29/10/2019"));
        contentValues.put("parent","Hobbies");
        db.insert(CHILD_TABLE_NAME, null, contentValues);


        //insertContact("Acads", "Padhai ki baatein","31/12/2019");
        //insertContact("Self improvement","Reading list, blogs, exercise, etc.","30/12/2019");
        //insertChildContact("Exercise","someday?","27/02/2021","Self improvement");
        //insertChildContact("Reading list","My bucket list:\nHear the Wind Sing\nThe Fountainhead\nAtlas Shrugged\nA prisoner of birth","","Self improvement");
        //insertContact("Research","Pet projects","");
        //insertContact("Hobbies","<3","");
        //insertChildContact("Origami","cranes and tigers.","29/10/2019","Hobbies");
        //insertChildContact("Drum practice!", "Aim:\nHallowed be thy name,\nAcid Rain (LTE)","29/10/2019","Hobbies");


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+CONTACTS_TABLE_NAME);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS "+CHILD_TABLE_NAME);
        onCreate(db);

    }



    private long getDate(String day) {
        if(day==""){return 0;}
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        try {
        date = dateFormat.parse(day);
        } catch (ParseException e) {}
        return date.getTime();
    }



    public boolean insertContact  (String task, String description,String dateStr)
    {
        Date date;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        contentValues.put("description", description);
        contentValues.put("dateStr", getDate(dateStr));
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertChildContact  (String task, String description,String dateStr,String parent)
    {
        Date date;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        contentValues.put("description", description);
        contentValues.put("parent", parent);
        contentValues.put("dateStr", getDate(dateStr));
        db.insert(CHILD_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateContact (String id, String task,String description, String dateStr)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("task", task);
        contentValues.put("description", description);
        contentValues.put("dateStr", getDate(dateStr));

        db.update(CONTACTS_TABLE_NAME, contentValues, "id = ? ", new String[] { id } );
        return true;
    }

    public boolean updateChildContact (String id, String task,String description, String dateStr,String parent)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("task", task);
        contentValues.put("description", description);
        contentValues.put("dateStr", getDate(dateStr));
        contentValues.put("parent", parent);
        db.update(CHILD_TABLE_NAME, contentValues, "id = ? ", new String[] { id } );
        return true;
    }


    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+CONTACTS_TABLE_NAME+" order by id desc", null);
        return res;

    }

    public Cursor getChildData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+CHILD_TABLE_NAME+" order by id desc", null);
        return res;

    }
    public Cursor getParent(String child){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+CHILD_TABLE_NAME+" WHERE task = '"+child+"' ", null);
        return res;

    }
    public Cursor getDataSpecific(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+CONTACTS_TABLE_NAME+" WHERE id = '"+id+"' order by id desc", null);
        return res;

    }

    public Cursor getChildDataSpecific(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+CHILD_TABLE_NAME+" WHERE id = '"+id+"' ", null);
        return res;

    }

    public Cursor getChildDataSpecificWithParent(String parent){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+CHILD_TABLE_NAME+" WHERE parent = '"+parent+"' ", null);
        return res;

    }


    public Cursor getDataToday(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+CONTACTS_TABLE_NAME, null);
        return res;

    }



    public Cursor getDataTomorrow(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+CONTACTS_TABLE_NAME+
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) = date('now', '+1 day', 'localtime')  order by id desc", null);
        return res;

    }


    public Cursor getDataUpcoming(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+CONTACTS_TABLE_NAME+
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) > date('now', '+1 day', 'localtime') order by id desc", null);
        return res;

    }

    public Cursor getDataonDate_Parent(String date_given){



            SQLiteDatabase db = this.getReadableDatabase();
            long date1=getDate(date_given);

            Cursor res =  db.rawQuery("select * from "+CONTACTS_TABLE_NAME+
                    " WHERE  date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) = date(datetime( '" + date1 + "'  / 1000 , 'unixepoch', 'localtime'))   order by id desc", null);
            return res;



    }

    public Cursor getDataonDate_Child(String date_given){



        SQLiteDatabase db = this.getReadableDatabase();
        long date1=getDate(date_given);

        Cursor res =  db.rawQuery("select * from "+CHILD_TABLE_NAME+
                " WHERE  date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) = date(datetime( '" + date1 + "'  / 1000 , 'unixepoch', 'localtime'))   order by id desc", null);
        return res;



    }
}
