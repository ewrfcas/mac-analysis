package com.mac.service;

import com.mac.model.CustomData;
import com.mac.model.CustomDateRow;
import com.mac.util.Response;

/**
 * Created by ewrfcas on 2016/12/20.
 */
@SuppressWarnings("all")
public interface MacService {
    Response<String> analysis();
    Response<CustomDateRow>save(String fileName);
    Response<CustomData>getAllCustoms(String startTime,String endTime);
}
