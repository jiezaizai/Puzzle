package com.example.gj.puzzle.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gj.puzzle.R;

import java.util.List;
import java.util.Map;

/**
 * Created by gj
 * Created on 8/26/19
 * Description
 */

public class MySimpleAdapter extends SimpleAdapter {
    private Context context;
    private List<Map<String, Object>> lists;

    public MySimpleAdapter(Context context, List<Map<String, Object>> data, int resource, String[] form, int[] to) {
        super(context, data, resource, form, to);
        this.context = context;
        this.lists = data;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        return view;
    }
}
