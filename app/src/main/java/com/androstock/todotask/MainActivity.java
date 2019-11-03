package com.androstock.todotask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ToHomeActivity(View v)
    {
        Intent i = new Intent(this, TaskHome.class);
        startActivity(i);
    }

    public void ToDayViewActivity(View v)
    {
        Intent i = new Intent(this, DayViewActivity.class);
        startActivity(i);
    }
}
