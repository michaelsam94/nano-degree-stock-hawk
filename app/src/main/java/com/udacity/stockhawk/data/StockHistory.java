package com.udacity.stockhawk.data;

import java.util.List;

/**
 * Created by micky on 01-Apr-17.
 */

public class StockHistory {
    private String sympol;
    private List<String> date;
    private List<Float> value;


    public String getSympol() {
        return sympol;
    }

    public void setSympol(String sympol) {
        this.sympol = sympol;
    }

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public List<Float> getValue() {
        return value;
    }

    public void setValue(List<Float> value) {
        this.value = value;
    }

}
