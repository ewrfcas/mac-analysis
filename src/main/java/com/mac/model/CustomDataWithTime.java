package com.mac.model;

import java.util.List;

/**
 * Created by ewrfcas on 2016/12/21.
 */
//客流——时间关系
@lombok.Data
public class CustomDataWithTime {
    //早上8点到晚上20点各时间段平均人数
    private List<Integer> numFrom8To20;
}
