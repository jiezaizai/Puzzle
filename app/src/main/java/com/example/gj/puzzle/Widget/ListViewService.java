package com.example.gj.puzzle.Widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.gj.puzzle.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gj
 * Created on 8/27/19
 * Description
 */

public class ListViewService extends RemoteViewsService {
    public static final String INITENT_DATA = "extra_data";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    private class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;

        private List<String> mList = new ArrayList<>();

        public ListRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate() {
            mList.add("一");
            mList.add("二");
            mList.add("三");
            mList.add("四");
            mList.add("五");
            mList.add("六");
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {
            mList.clear();
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);
            views.setTextViewText(android.R.id.text1, "image:" + mList.get(position));
            views.setTextColor(android.R.id.text1,mContext.getResources().getColor(R.color.white));
            Intent changeIntent = new Intent();
            changeIntent.putExtra(ListViewService.INITENT_DATA,position);
            changeIntent.setAction(MyAppWidget.CHANGE_IMAGE);
            views.setOnClickFillInIntent(android.R.id.text1, changeIntent);
            return views;
        }
        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
