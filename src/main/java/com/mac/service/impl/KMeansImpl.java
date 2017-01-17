package com.mac.service.impl;

import com.mac.model.Data;
import com.mac.model.dataset.DataSet;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ewrfcas on 2017/1/16.
 */
public class KMeansImpl {
    @Autowired
    private transient DBSCANImpl dbscan;

    public void Kmeans(List<Data> datas){
        List<DataSet> dataSets=new ArrayList<>();
        dataSets=dbscan.getDataSet(datas);
    }
}
