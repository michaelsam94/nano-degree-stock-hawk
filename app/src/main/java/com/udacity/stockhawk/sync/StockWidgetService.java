package com.udacity.stockhawk.sync;

import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.StockWidgetProvider;
import com.udacity.stockhawk.ui.StockWidgetView;



public class StockWidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        StockWidgetView stockWidgetView = new StockWidgetView(getApplicationContext(),intent);
        Cursor cursor = getContentResolver().query(Contract.Quote.URI,
                new String[]{ Contract.Quote.COLUMN_SYMBOL,Contract.Quote.COLUMN_PRICE,Contract.Quote.COLUMN_HISTORY},
                null,
                null,
                Contract.Quote.COLUMN_SYMBOL);

        stockWidgetView.setWidgetCursor(cursor);

        return stockWidgetView;
    }




}
