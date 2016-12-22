package com.mac.service;

import com.mac.model.jpa.JPAMac;
import com.mac.util.Response;

/**
 * Created by ewrfcas on 2016/12/20.
 */
@SuppressWarnings("all")
public interface MacService {
    Response<String> analysis();
    Response<JPAMac> getJPAMacByMac(String Mac);
    Response<String>save(String fileName);
}
