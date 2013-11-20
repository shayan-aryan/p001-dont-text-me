package com.aryan.donttextme.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aryan.donttextme.R;
import com.aryan.donttextme.core.DataBaseManager;
import com.aryan.donttextme.model.SMS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by User on 11/20/13.
 */
public class SmsAdapter extends BaseAdapter {
    ArrayList<SMS> items = new ArrayList<SMS>();
    private Context context;

    public SmsAdapter(Context context) {
        this.context = context;
        DataBaseManager dbm = new DataBaseManager(context);
        items = dbm.getAllSms();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public SMS getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TextView sender;
        TextView time;
        TextView body;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.sms_row, parent, false);
            sender = (TextView) view.findViewById(R.id.sender);
            time = (TextView) view.findViewById(R.id.time);
            body = (TextView) view.findViewById(R.id.body);
            view.setTag(new ViewHolder(sender, time, body));
        }else{
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            sender=viewHolder.sender;
            time=viewHolder.time;
            body=viewHolder.body;
        }
        time.setText(String.valueOf(items.get(position).getTime()));
        sender.setText(items.get(position).getSender());
        body.setText(items.get(position).getBody());

        return view;
    }

    public void updateItems(ArrayList<SMS> list) {
        this.items = list;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public final TextView sender;
        public final TextView time;
        public final TextView body;

        public ViewHolder(TextView sender, TextView time, TextView body) {
            this.sender = sender;
            this.time = time;
            this.body = body;
        }

    }
}
