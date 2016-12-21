package com.mac.model;

import java.util.List;

/**
 * Created by ewrfcas on 2016/12/21.
 */
//高意向客流信息
@lombok.Data
public class CustomHIData {
    //天数
    private int dayNum;

    //高意向客流数量
    private int customHI;

    //高意向客流首次数量
    private int customHIFirst;

    //高意向车型关注度（没法做）

    //高意向客流每日平均驻店时长
    private List<Integer> stayTimeHID;
}
