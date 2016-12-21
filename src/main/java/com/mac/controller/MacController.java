package com.mac.controller;

import com.mac.model.CustomData;
import com.mac.model.CustomDateRow;
import com.mac.model.CustomHIData;
import com.mac.service.MacService;
import com.mac.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    //数据存储（存储一天检测到的mac信号到sql）
    @RequestMapping(value="/save/data" ,method = RequestMethod.GET)
    public Response<String> save() {
        return macService.save();
    }

    //输入开始时间，结束时间 返回开始时间——结束时间（日）中间的客流信息CustomData
    @RequestMapping(value="/get/allcustoms/{startTime}/{endTime}" ,method = RequestMethod.GET)
    public Response<CustomData> getAllCustoms(@PathVariable("startTime")String startTime,@PathVariable("endTime")String endTime){
        return null;
    }

    //输入开始时间，结束时间 返回期间每日客流信息
    @RequestMapping(value="/get/daycustoms/{startTime}/{endTime}" ,method = RequestMethod.GET)
    public Response<CustomDateRow> getDayCustoms(@PathVariable("startTime")String startTime,@PathVariable("endTime")String endTime){
        return null;
    }

    //输入开始时间，结束时间 返回高意向客流信息
    @RequestMapping(value="/get/HIcustoms/{startTime}/{endTime}" ,method = RequestMethod.GET)
    public Response<CustomHIData> getHICustoms(@PathVariable("startTime")String startTime,@PathVariable("endTime")String endTime){
        return null;
    }
}
