package com.mac.service.impl;

import com.mac.model.Data;
import com.mac.model.DataDetail;
import com.mac.util.TimeSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by ewrfcas on 2017/1/21.
 */
@Service
public class CutImpl {
    @Autowired
    private transient TimeSort timeSort;

    public List<Data> CutToCustom(List<Data> datas){
        List<Data> datasReal=new ArrayList<>();
        for(Data data:datas){
            if(data.getMinDistance()>-70){//进店条件
                List<DataDetail> dataDetailsReal=new ArrayList<>();
                for(DataDetail dataDetail:data.getDataDetails()){//只获取-70以内的数据作为店内数据
                    if(dataDetail.getRSSI()>-70)dataDetailsReal.add(dataDetail);
                }
                Collections.sort(dataDetailsReal,timeSort);//时间排序
                Date firstTime=dataDetailsReal.get(dataDetailsReal.size() - 1).getTime();
                Date lastTime=dataDetailsReal.get(0).getTime();
                double stayTime=(lastTime.getTime()-firstTime.getTime())/60000.00;
                if(stayTime>=15&&firstTime.getHours()>=8&&lastTime.getHours()<=20){//驻店时长要超过15分钟&&时间在8点到20点之间
                    datasReal.add(data);
                }
            }
        }
        return datasReal;
    }
}
