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
import com.example.gj.puzzle.entity.ImageSoures;

/**
 * Implementation of App Widget functionality.
 */
public class MyAppWidget extends AppWidgetProvider {
    public static final String CHANGE_IMAGE = "com.example.gj.action.CHANGE_IMAGE";
    //这个对象用来加载指定的页面布局
    private RemoteViews mRemoteViews;
    /**
     * ComponentName，顾名思义，就是组件名称，这个类主要用来定义一个应用程序的组件，
     * 通过调用Intent中的setComponent方法，我们可以打开同个应用以及不同应用中的组件。例如：Activity，Service。
     */
    private ComponentName mComponentName;

    private int[] imgs = ImageSoures.imageSours;

    //更新桌面控件的方法
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
        mRemoteViews.setImageViewResource(R.id.iv_test, R.drawable.widget);
        mRemoteViews.setTextViewText(R.id.btn_test, "点击进入游戏");
        Intent skipIntent = new Intent(context, MainActivity.class);
        //延时进入MainActivity,FLAG_CANCEL_CURRENT参数是指如果描述的PendingIntent已经存在，则在产生新的Intent之前会先取消掉当前的
        PendingIntent pi = PendingIntent.getActivity(context, 200, skipIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        // PendingIntent是指把Intent包装了一层, 并且把PendingIntent放入一个新的进程. 通过触发事件去触发这个PendingIntent.
        mRemoteViews.setOnClickPendingIntent(R.id.btn_test, pi);

        /**
         * 设置ListView的适配器
         * @param lvintent 对应启动ListViewService（继承RemoteViewsService）的intent
         * 通过setRemoteAdapter将 ListView 和ListViewService关联起来
         * 从而达到ListViewService 更新 ListView 的目的
         */

        Intent lvIntent = new Intent(context, ListViewService.class);
        mRemoteViews.setRemoteAdapter(R.id.lv_test, lvIntent);

        /**
         * 通过 setPendingIntentTemplate 设置intent模板
         * 然后在处理该“集合控件”的RemoteViewsFactory类的getViewAt()接口中
         * 通过 setOnClickFillInIntent 设置“集合控件的某一项的数据”
         */

        Intent toIntent = new Intent(context, MyAppWidget.class);
        toIntent.setAction(CHANGE_IMAGE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, toIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setPendingIntentTemplate(R.id.lv_test, pendingIntent);

        //更新小部件
        mComponentName = new ComponentName(context, MyAppWidget.class);
        appWidgetManager.updateAppWidget(mComponentName, mRemoteViews);
    }

    //重写这个方法，将组件当成广播来使用。
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(CHANGE_IMAGE)) {
            int position = intent.getIntExtra(ListViewService.INITENT_DATA, 0);
            mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
            mRemoteViews.setImageViewResource(R.id.iv_test, imgs[position]);
            mComponentName = new ComponentName(context, MyAppWidget.class);
            AppWidgetManager.getInstance(context).updateAppWidget(mComponentName, mRemoteViews);
        }


    }
}