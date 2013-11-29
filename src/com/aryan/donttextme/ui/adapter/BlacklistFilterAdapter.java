package com.aryan.donttextme.ui.adapter;

/**
 * Created by Shayan on 11/29/13.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aryan.donttextme.R;
import com.aryan.donttextme.core.DataBaseManager;
import com.aryan.donttextme.model.Filter;
import com.aryan.donttextme.model.SMS;
import com.aryan.donttextme.util.DateUtil.DateConverter;
import com.aryan.donttextme.util.DateUtil.MiladiDate;
import com.aryan.donttextme.util.DateUtil.PersianDate;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class BlacklistFilterAdapter extends BaseAdapter {
    ArrayList<Filter> items = new ArrayList<Filter>();
    private Context context;
    private String filterKey, name;
    private DataBaseManager dbm;

    public BlacklistFilterAdapter(Context context) {
        this.context = context;
        dbm = new DataBaseManager(context);
        items = dbm.getAllFilters();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Filter getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TextView name;
        ImageView total;
        ImageView message;
        ImageView edit;
        ImageView trash;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.filter_blacklist_row, parent, false);
            name = (TextView) view.findViewById(R.id.name);
            total = (ImageView) view.findViewById(R.id.total);
            message = (ImageView) view.findViewById(R.id.message);
            edit = (ImageView) view.findViewById(R.id.edit);
            trash = (ImageView) view.findViewById(R.id.trash);
            view.setTag(new ViewHolder(name, total, message, edit, trash));
        }else{
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            name=viewHolder.name;
            total=viewHolder.total;
            message=viewHolder.message;
            edit=viewHolder.edit;
            trash=viewHolder.trash;
        }
        name.setText(items.get(position).getName());

        return view;
    }

    public void updateItems() {
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        dbm = new DataBaseManager(context);
        items = dbm.getAllFilters();
        super.notifyDataSetChanged();
    }

    private static class ViewHolder {
        public  final TextView name;
        public  final ImageView total;
        public  final ImageView message;
        public  final ImageView edit;
        public  final  ImageView trash;

        public ViewHolder(TextView name, ImageView total, ImageView message,ImageView edit,ImageView trash) {
            this.name = name;
            this.total = total;
            this.message = message;
            this.edit = edit;
            this.trash = trash;
        }

    }
}

