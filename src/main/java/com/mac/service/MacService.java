package com.mac.service;

import com.mac.model.CustomData;
import com.mac.model.CustomDateRow;
import com.mac.model.Data;
import com.mac.util.Response;

import java.util.List;

/**
 * Created by ewrfcas on 2016/12/20.
 */
@SuppressWarnings("all")
public interface MacService {
    Response<String> analysis();
    Response<CustomDateRow>save(String fileName,String storeId);
    Response<CustomData>getAllCustoms(String startTime,String endTime);
    List<Data> getDataFromTxt(String path);
}
