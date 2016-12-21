package com.mac.controller;

import com.mac.service.MacService;
import com.mac.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ewrfcas on 2016/12/20.
 */
@RestController
public class MacController {
    @Autowired
    private transient MacService macService;

    //数据分析
    @RequestMapping(value="/analysis/data" ,method = RequestMethod.GET)
    public Response<String> analysis() {
        return macService.analysis();
    }

    //数据存储
    @RequestMapping(value="/save/data" ,method = RequestMethod.GET)
    public Response<String> save() {
        return macService.save();
    }
}
