package com.udacity.stockhawk.data;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.sync.StockWidgetService;
import com.udacity.stockhawk.ui.StockHistoryActivity;


class StockDataProviderObserver extends ContentObserver {
    private AppWidgetManager mAppWidgetManager;
    private ComponentName mComponentName;
    StockDataProviderObserver(AppWidgetManager mgr, ComponentName cn, Handler h) {
        super(h);
        mAppWidgetManager = mgr;
        mComponentName = cn;
    }
    @Override
    public void onChange(boolean selfChange) {
        // The data has changed, so notify the widget that the collection view needs to be updated.
        // In response, the factory's onDataSetChanged() will be called which will requery the
        // cursor for the new data.
        mAppWidgetManager.notifyAppWidgetViewDataChanged(
                mAppWidgetManager.getAppWidgetIds(mComponentName), R.id.lv_widget_stocks);
    }
}

public class StockWidgetProvider extends AppWidgetProvider {
    private static HandlerThread sWorkerThread;
    private static Handler sWorkerQueue;
    private static StockDataProviderObserver sDataObserver;

    public StockWidgetProvider() {
        sWorkerThread = new HandlerThread("WeatherWidgetProvider-worker");
        sWorkerThread.start();
        sWorkerQueue = new Handler(sWorkerThread.getLooper());
    }

    @Override
    public void onEnabled(Context context) {
        final ContentResolver r = context.getContentResolver();
        if(sDataObserver == null){
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context, StockWidgetProvider.class);
            sDataObserver = new StockDataProviderObserver(mgr, cn, sWorkerQueue);
            r.registerContentObserver(Contract.Quote.URI, true, sDataObserver);
        }
        super.onEnabled(context);
    }
    @Override
    public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i=0; i<appWidgetIds.length; i++) {
            Intent svcIntent=new Intent(ctxt, StockWidgetService.class);

            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews widget=new RemoteViews(ctxt.getPackageName(),
                    R.layout.stock_widget);

            widget.setRemoteAdapter(R.id.lv_widget_stocks, svcIntent);

            Intent clickIntent=new Intent(ctxt, StockHistoryActivity.class);
            PendingIntent clickPI=PendingIntent
                    .getActivity(ctxt, 0,
                            clickIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

            widget.setPendingIntentTemplate(R.id.lv_widget_stocks, clickPI);

            appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
        }

        super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context ctx, Intent intent) {
        super.onReceive(ctx, intent);
    }


    @Override
    public void onDisabled(Context context) {
        final ContentResolver r = context.getContentResolver();
        if(r != null) r.unregisterContentObserver(sDataObserver);
        super.onDisabled(context);
    }
}
