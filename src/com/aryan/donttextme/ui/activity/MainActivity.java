package com.aryan.donttextme.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.renderscript.Element;
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
import com.aryan.donttextme.ui.fragment.BlackListFragment;
import com.aryan.donttextme.ui.fragment.SmsListFragment;

public class MainActivity extends FragmentActivity {
    static final int ITEMS = 3;
    private ViewPager mPager;
    private SectionsAdapter mAdapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new SectionsAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
    }

    public class SectionsAdapter extends FragmentPagerAdapter {
        public SectionsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SmsListFragment();
                case 1:
                    return new BlackListFragment();
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
