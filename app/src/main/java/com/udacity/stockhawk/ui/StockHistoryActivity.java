package com.udacity.stockhawk.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.StockHistory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class StockHistoryActivity extends AppCompatActivity {

    public static final int STOCK_HISTORY_LOADER = 0;
    @BindView(R.id.lcStockHistory)
    LineChart mChart;

    private String mSymbol;
    private String mHistory;
    private StockHistory mStockHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        mStockHistory = new StockHistory();

        mSymbol = getIntent().getStringExtra(MainActivity.KEY_SYMBOL);

        mHistory = getIntent().getStringExtra(MainActivity.KEY_HISTORY);
        Timber.d("history %s", mHistory);

        setStockHistory(mHistory);
        setData(mStockHistory.getValue());

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mStockHistory.getDate().get((int) value);
            }
        });
        xAxis.setTextColor(Color.WHITE);



    }

    private void setStockHistory(String history) {
        mStockHistory = new StockHistory();
        List<String> daysStrList = new ArrayList<>();
        List<Float> valuesStrList = new ArrayList<>();
        List<String> days = Arrays.asList(history.split("\\n"));
        String date;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar;
        for (String day : days) {
            List<String> dateValue = Arrays.asList(day.split(","));
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(dateValue.get(0)));
            date = formatter.format(calendar.getTime());
            daysStrList.add(date);
            valuesStrList.add(Float.valueOf(dateValue.get(1)));
        }
        mStockHistory.setSympol(mSymbol);
        mStockHistory.setDate(daysStrList);
        mStockHistory.setValue(valuesStrList);
    }


    private void setData(List<Float> yValues) {

        ArrayList<Entry> xyValues = new ArrayList<>();
        float xValue = 0.0f;
        for (Float yValue : yValues) {
            xyValues.add(new Entry(xValue, yValue));
            xValue += 1.0f;
        }

        LineDataSet set1;
        // create a dataset and give it a type
        set1 = new LineDataSet(xyValues, mSymbol);


        set1.setFillColor(Color.WHITE);

        set1.setValueTextColor(Color.WHITE);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(dataSets);

        // set data
        mChart.setData(data);
    }

}

