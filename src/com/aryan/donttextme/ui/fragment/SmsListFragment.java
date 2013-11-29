package com.aryan.donttextme.ui.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.aryan.donttextme.R;
import com.aryan.donttextme.core.DataBaseManager;
import com.aryan.donttextme.core.SMSReceiver;
import com.aryan.donttextme.ui.adapter.SmsAdapter;

public class SmsListFragment extends Fragment {
    private ListView list;
    private SmsAdapter adapter;
    private Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_old, container, false);
        list = (ListView) view.findViewById(R.id.list);
        adapter = new SmsAdapter(mActivity);
        list.setAdapter(adapter);

        Button on = (Button) view.findViewById(R.id.on);
        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseManager db = new DataBaseManager(mActivity);
                db.eraseBlacklist();
                db.AddToKeywordsBlackList("Hi");
                ComponentName componentName = new ComponentName(mActivity, SMSReceiver.class);
                PackageManager packageManager = mActivity.getPackageManager();
                packageManager.setComponentEnabledSetting(componentName,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            }
        });
        Button off = (Button) view.findViewById(R.id.off);
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentName componentName = new ComponentName(mActivity, SMSReceiver.class);
                PackageManager packageManager = mActivity.getPackageManager();
                packageManager.setComponentEnabledSetting(componentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}