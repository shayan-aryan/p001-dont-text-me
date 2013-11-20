package com.aryan.donttextme;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.aryan.donttextme.ui.adapter.SmsAdapter;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list = (ListView) findViewById(R.id.list);
        SmsAdapter adapter = new SmsAdapter(this);
        list.setAdapter(adapter);
    }
}
