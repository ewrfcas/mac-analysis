package com.mac.model.dataset;

import lombok.Data;

/**
 * Created by ewrfcas on 2017/1/16.
 */
@Data
public class DataSet {
    private String Mac;

    //平均RSSI
    private double RSSIAvg;

    private int num;

    private double time;

    private boolean canUse;

    public boolean getCanUse(){
        return this.canUse;
    }
}
