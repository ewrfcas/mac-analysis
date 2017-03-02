package com.mac.service.impl;

import com.mac.dao.CustomDao;
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
    @Autowired
    private transient CustomDao customDao;

    //筛选
    public List<Data> cutToCustom(List<Data> datas){
        List<Data> datasReal=new ArrayList<>();
        for(Data data:datas){
            if(data.getMinDistance()>-70){//进店条件
                //过滤员工
                if(customDao.exists(data.getMAC())){
                    if(customDao.findOne(data.getMAC()).getIs_staff()==1)continue;//是员工,忽略
                }
                List<DataDetail> dataDetailsReal=new ArrayList<>();
                boolean flagInFix=false;
                for(DataDetail dataDetail:data.getDataDetails()){//只获取-70以内的数据作为店内数据
                    if(dataDetail.getDeviceId().equals("8482f42f22d8")&&dataDetail.getRSSI()>-70){//休息室内（或者修车）
                        flagInFix=true;
                        break;
                    }
                    if(dataDetail.getRSSI()>-70&&dataDetail.getDeviceId().length()>10){
                        dataDetailsReal.add(dataDetail);
                    }
                }
                if(flagInFix){//出现在休息室或者修车的直接剔除
                    continue;
                }
                if(dataDetailsReal.size()>=2){
                    Collections.sort(dataDetailsReal,timeSort);//时间排序
                    Date firstTime=dataDetailsReal.get(dataDetailsReal.size()- 1).getTime();
                    Date lastTime=dataDetailsReal.get(0).getTime();
                    double stayTime=getStayTime(dataDetailsReal);
                    if(stayTime>=15&&firstTime.getHours()>=8&&lastTime.getHours()<=20){//驻店时长要超过15分钟&&时间在8点到20点之间
                        data.setDataDetails(dataDetailsReal);
                        datasReal.add(data);
                    }
                }
            }
        }
        return datasReal;
    }
    //高意向????????????????
    public List<Data> cutToHigh(List<Data> datas){
        List<Data> datasReal=new ArrayList<>();
        for(Data data:datas){
            List<DataDetail> dataDetailsReal=new ArrayList<>();
            for(DataDetail dataDetail:data.getDataDetails()){
                if(dataDetail.getDeviceId().length()<12){
                    dataDetailsReal.add(dataDetail);
                }
            }
            if(dataDetailsReal.size()>=1){
                Data dataTemp=new Data();
                dataTemp.setDataDetails(dataDetailsReal);
                dataTemp.setNum(dataDetailsReal.size());
                dataTemp.setMAC(data.getMAC());
                datasReal.add(dataTemp);
            }
        }
        return datasReal;
    }
    //批次合并
    public List<Data> allInOne(List<Data> datas){
        List<Data> datasReal=new ArrayList<>();
        for(Data data:datas){
            if(data.getMAC().equals("merged"))continue;//如果已经合并，则忽略
            for(Data data1:datas){
                if(data1.getMAC().equals(data.getMAC()))continue;
                //出店时间差
                double outTime=Math.abs(data.getDataDetails().get(0).getTime().getTime()
                        -data1.getDataDetails().get(0).getTime().getTime());
                //入店时间差
                double inTime=Math.abs(data.getDataDetails().get(data.getDataDetails().size()-1).getTime().getTime()
                        -data1.getDataDetails().get(data1.getDataDetails().size()-1).getTime().getTime());
                if(inTime<3*60*1000&&outTime<3*60*1000){//如果进店和出店的时间差都没有超过10min，我们认为是同一批次的顾客
                    data1.setMAC("merged");
                }
            }
            datasReal.add(data);
        }
        return datasReal;
    }

    //驻店时间计算(需要排序后)
    public double getStayTime(List<DataDetail> dataDetail){
        Date firstTime=dataDetail.get(dataDetail.size() - 1).getTime();
        Date lastTime=dataDetail.get(0).getTime();
        double stayTime=lastTime.getTime()-firstTime.getTime();
        double leavingTime=0;
        for(int i=0;i<dataDetail.size()-1;i++){
            if(dataDetail.get(i).getTime().getTime()-dataDetail.get(i + 1).getTime().getTime()>15*60000){
            //间隔15分钟以上的时间去除(离开再返回中间时间忽略)
                leavingTime+=(dataDetail.get(i).getTime().getTime()-dataDetail.get(i+1).getTime().getTime());
            }
        }
        return (stayTime-leavingTime)/60000.00;
    }
}
