package com.mac.model;

import java.util.List;

/**
 * Created by ewrfcas on 2016/12/21.
 */
//每日客流数据整合
@lombok.Data
public class CustomDateRow {
    //天数
    private int dayNum;

    //每日数据列表
    private List<CustomDataWithDate> customDataWithDates;

    //期间客流时段信息
    private CustomDataWithTime customDataWithTime;
}
