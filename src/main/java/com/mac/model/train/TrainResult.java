package com.mac.model.train;

import lombok.Data;

/**
 * Created by ewrfcas on 2017/3/2.
 */
@Data
public class TrainResult {
    private int distanceIn;//进店RSSI阈值
    private int distanceIg;//修车RSSI阈值
    private int m;//批次分钟
    private int dayDiff;//天数差异总和
    private double perDiff;//百分比差异
    private int[] days=new int[14];
}
