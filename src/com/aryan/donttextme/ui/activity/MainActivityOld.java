package com.aryan.donttextme.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.aryan.donttextme.R;
import com.aryan.donttextme.core.DataBaseManager;
import com.aryan.donttextme.core.SMSReceiver;
import com.aryan.donttextme.ui.adapter.SmsAdapter;

public class MainActivityOld extends Activity {
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

        Button on = (Button) findViewById(R.id.on);
        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseManager db = new DataBaseManager(MainActivityOld.this);
                db.eraseBlacklist();
                db.AddToKeywordsBlackList("Hi");
                ComponentName componentName = new ComponentName(MainActivityOld.this, SMSReceiver.class);
                PackageManager packageManager = getPackageManager();
                packageManager.setComponentEnabledSetting(componentName,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            }
        });
        Button off = (Button) findViewById(R.id.on);
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentName componentName = new ComponentName(MainActivityOld.this, SMSReceiver.class);
                PackageManager packageManager = getPackageManager();
                packageManager.setComponentEnabledSetting(componentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
