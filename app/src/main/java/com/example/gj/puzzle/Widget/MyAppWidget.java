package com.example.gj.puzzle.Widget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.gj.puzzle.Activity.MainActivity;
import com.example.gj.puzzle.R;

/**
 * Implementation of App Widget functionality.
 */
public class MyAppWidget extends AppWidgetProvider {
    public static final String CHANGE_IMAGE = "com.example.gj.action.CHANGE_IMAGE";

    private RemoteViews mRemoteViews;
    private ComponentName mComponentName;

    private int[] imgs = new int[]{
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
    };


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
        mRemoteViews.setImageViewResource(R.id.iv_test, R.drawable.widget);
        mRemoteViews.setTextViewText(R.id.btn_test, "点击进入游戏");
        Intent skipIntent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 200, skipIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_test, pi);

        // 设置 ListView 的adapter。
        // (01) intent: 对应启动 ListViewService(RemoteViewsService) 的intent
        // (02) setRemoteAdapter: 设置 ListView 的适配器
        // 通过setRemoteAdapter将 ListView 和ListViewService关联起来，
        // 以达到通过 GridWidgetService 更新 gridview 的目的
        Intent lvIntent = new Intent(context, ListViewService.class);
        mRemoteViews.setRemoteAdapter(R.id.lv_test, lvIntent);


        // 设置响应 ListView 的intent模板
        // 说明：“集合控件(如GridView、ListView、StackView等)”中包含很多子元素，如GridView包含很多格子。
        // 它们不能像普通的按钮一样通过 setOnClickPendingIntent 设置点击事件，必须先通过两步。
        // (01) 通过 setPendingIntentTemplate 设置 “intent模板”，这是比不可少的！
        // (02) 然后在处理该“集合控件”的RemoteViewsFactory类的getViewAt()接口中 通过 setOnClickFillInIntent 设置“集合控件的某一项的数据”

 /*
  * setPendingIntentTemplate 设置pendingIntent 模板
  * setOnClickFillInIntent 可以将fillInIntent 添加到pendingIntent中
  */
        Intent toIntent = new Intent(context,MyAppWidget.class);
        toIntent.setAction(CHANGE_IMAGE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, toIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setPendingIntentTemplate(R.id.lv_test, pendingIntent);


        mComponentName = new ComponentName(context, MyAppWidget.class);
        appWidgetManager.updateAppWidget(mComponentName, mRemoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals(CHANGE_IMAGE)){
            int position = intent.getIntExtra(ListViewService.INITENT_DATA,0);
            mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
            mRemoteViews.setImageViewResource(R.id.iv_test, imgs[position]);
            mComponentName = new ComponentName(context, MyAppWidget.class);
            AppWidgetManager.getInstance(context).updateAppWidget(mComponentName, mRemoteViews);
        }


    }
}