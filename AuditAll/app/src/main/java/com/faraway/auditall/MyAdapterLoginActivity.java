package com.faraway.auditall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * @author Fan
 * @description
 * @date 2019-09-20
 * @time 8:33
 **/
public class MyAdapterLoginActivity extends BaseAdapter {

    private Context context;
    private List<String> list;

    public MyAdapterLoginActivity(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size()-1;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(context);
        view = _LayoutInflater.inflate(R.layout.item, null);
        if (view != null) {
            TextView tv1 = (TextView) view.findViewById(R.id.textView1);
            tv1.setTextColor(context.getResources().getColor(R.color.divider));
            tv1.setText(list.get(i));
        }
        return view;
    }
}
