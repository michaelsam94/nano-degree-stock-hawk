package com.udacity.stockhawk.sync;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.ui.StockWidgetView;



public class StockWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockWidgetView(this.getApplicationContext(),
                intent);
    }
}
