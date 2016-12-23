package com.mac.model;

import java.util.Date;

/**
 * Created by ewrfcas on 2016/12/21.
 */
//每日客流相关数据
@lombok.Data
public class CustomDataWithDate {
    //日期
    private Date date;

    //当日客流总数
    private int customAllD;

    //当日首次客流总数
    private int customFirstD;

    //当日高意向客流总数
    private int customHID;

    //当日平均驻店时长
    private double stayTimeD;
}
