package com.mac.model;

/**
 * Created by ewrfcas on 2016/12/21.
 */
//客流总数统计
@lombok.Data
public class CustomData {
    //设备总数
    private int deviceNum;

    //总客流数量
    private int customAll;

    //首次进店客流数量
    private int customFirst;

    //高意向客流数量
    private int customHI;

    //客流平均驻店时长
    private int stayTime;

    //期间每日数据
    private CustomDateRow customDateRow;

}
