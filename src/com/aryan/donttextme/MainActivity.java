package com.aryan.donttextme;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.aryan.donttextme.core.DataBaseManager;
import com.aryan.donttextme.ui.adapter.SmsAdapter;

public class MainActivity extends Activity {
    private  SmsAdapter adapter;
    private ListView list;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.list);
        adapter = new SmsAdapter(this);
        list.setAdapter(adapter);

        Button ok = (Button) findViewById(R.id.button);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseManager db = new DataBaseManager(MainActivity.this);
                db.eraseBlacklist();
//                db.AddToSpecificNumbersBlackList("15555215556");
                db.AddToKeywordsBlackList("Hi");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
