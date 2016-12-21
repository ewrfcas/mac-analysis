package com.mac.model;

import java.util.Date;

/**
 * Created by ewrfcas on 2016/12/20.
 */
@lombok.Data
public class DataDetail {
    private String deviceId;

    private Integer RSSI;

    private Date time;
}
