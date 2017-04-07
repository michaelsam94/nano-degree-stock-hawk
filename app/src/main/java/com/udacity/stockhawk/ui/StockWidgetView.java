package com.udacity.stockhawk.ui;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.StockWidgetProvider;


public class StockWidgetView implements RemoteViewsService.RemoteViewsFactory {


    private Context ctxt=null;
    private Cursor mWidgetCursor = null;
    private int appWidgetId;

    public StockWidgetView(Context ctxt, Intent intent){
        this.ctxt=ctxt;
        appWidgetId=
                intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
    }
    public void setWidgetCursor(Cursor widgetCursor){
        this.mWidgetCursor = widgetCursor;
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }



    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(mWidgetCursor != null) return mWidgetCursor.getCount();
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row=
                new RemoteViews(ctxt.getPackageName(), R.layout.row);
        if(mWidgetCursor != null){
            mWidgetCursor.moveToPosition(position);
            String stockName = mWidgetCursor.getString(0);
            String stockPrice = mWidgetCursor.getString(1);
            row.setTextViewText(R.id.tv_stock_name,stockName);
            row.setTextViewText(R.id.tv_stock_price,stockPrice);
        }

        Intent i=new Intent();
        Bundle extras=new Bundle();

        extras.putString(MainActivity.KEY_SYMBOL, mWidgetCursor
                .getString(0));
        extras.putString(MainActivity.KEY_HISTORY,mWidgetCursor.getString(2));
        extras.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        i.putExtras(extras);
        row.setOnClickFillInIntent(R.id.rl_widget_stock_item, i);
        return row;
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
        return true;
    }
}
