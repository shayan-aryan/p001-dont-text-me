package com.aryan.donttextme.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.aryan.donttextme.R;
import com.aryan.donttextme.ui.activity.AddToBlackList;
import com.aryan.donttextme.ui.adapter.BlacklistFilterAdapter;

/**
 * Created by User on 11/27/13.
 */
public class BlackListFragment extends Fragment {

    private Activity mActivity;
    private ListView list;
    private BlacklistFilterAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.black_list, container, false);

        list = (ListView) view.findViewById(R.id.list);
        adapter = new BlacklistFilterAdapter(mActivity);
        list.setAdapter(adapter);

        View footer = (View) inflater.inflate(R.layout.footer_black_list, container,false);
        list.addFooterView(footer);

        Button add = (Button) footer.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, AddToBlackList.class));
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
}
