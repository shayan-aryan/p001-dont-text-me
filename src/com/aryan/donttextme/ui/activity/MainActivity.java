package com.aryan.donttextme.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.aryan.donttextme.R;
import com.aryan.donttextme.core.DataBaseManager;
import com.aryan.donttextme.core.SMSReceiver;
import com.aryan.donttextme.ui.adapter.SmsAdapter;

public class MainActivity extends FragmentActivity {
    static final int ITEMS = 3;
    private ViewPager mPager;
    private SectionsAdapter mAdapter;

    /**
     * Called when the activity is first created.
     */

    public MainActivity(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new SectionsAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);


/*        Button on = (Button) findViewById(R.id.on);
        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseManager db = new DataBaseManager(MainActivity.this);
                db.eraseBlacklist();
                db.AddToKeywordsBlackList("Hi");
                ComponentName componentName = new ComponentName(MainActivity.this, SMSReceiver.class);
                PackageManager packageManager = getPackageManager();
                packageManager.setComponentEnabledSetting(componentName,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            }
        });
        Button off = (Button) findViewById(R.id.off);
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentName componentName = new ComponentName(MainActivity.this, SMSReceiver.class);
                PackageManager packageManager = getPackageManager();
                packageManager.setComponentEnabledSetting(componentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            }
        });*/
    }

    public class SectionsAdapter extends FragmentPagerAdapter {
        public SectionsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FiltersFragment();
                default:
                    Fragment fragment = new DummySectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
                    fragment.setArguments(args);
                    return fragment;
            }

        }

        @Override
        public int getCount() {
            return ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Section " + (position + 1);
        }
    }

    public class FiltersFragment extends Fragment {
        private ListView list;
        private SmsAdapter adapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.activity_main_old, container, false);
            list = (ListView) view.findViewById(R.id.list);
            adapter = new SmsAdapter(MainActivity.this);
            list.setAdapter(adapter);

            Button on = (Button) view.findViewById(R.id.on);
            on.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataBaseManager db = new DataBaseManager(MainActivity.this);
                    db.eraseBlacklist();
                    db.AddToKeywordsBlackList("Hi");
                    ComponentName componentName = new ComponentName(MainActivity.this, SMSReceiver.class);
                    PackageManager packageManager = getPackageManager();
                    packageManager.setComponentEnabledSetting(componentName,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                }
            });
            Button off = (Button) view.findViewById(R.id.off);
            off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ComponentName componentName = new ComponentName(MainActivity.this, SMSReceiver.class);
                    PackageManager packageManager = getPackageManager();
                    packageManager.setComponentEnabledSetting(componentName,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                }
            });
            return view;
        }
    }

    public static class DummySectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dummy, container, false);
            Bundle args = getArguments();
            ((TextView) view.findViewById(R.id.text1)).setText(
                    getString(R.string.month_aban, args.getInt(ARG_SECTION_NUMBER)));
            return view;
        }
    }
}
