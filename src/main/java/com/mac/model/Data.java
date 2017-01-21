package com.mac.model;

import java.util.List;

/**
 * Created by ewrfcas on 2016/12/18.
 */
@lombok.Data
public class Data {
    private String MAC;

    private List<DataDetail> dataDetails;

    private int num;

    private int minDistance;
}
