package com.mac.service.impl;

import com.mac.model.Data;
import com.mac.model.DataDetail;
import com.mac.model.dataset.Cluster;
import com.mac.model.dataset.DataSet;
import com.mac.util.TimeSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by ewrfcas on 2017/1/16.
 */
@SuppressWarnings("all")
@Service
public class DBSCANImpl {
    @Autowired
    private transient TimeSort timeSort;

    public void DBscan(List<Data> datas){
        List<DataSet> dataSets=getDataSet(datas);
        double[][] nearMatrix=new double[dataSets.size()][dataSets.size()];
        double maxNear=-1;
        //求临近矩阵
        for(int i=0;i<dataSets.size();i++){
            for(int j=0;j<dataSets.size();j++){
                if(i!=j){
                    DataSet dataSeti=dataSets.get(i);
                    DataSet dataSetj=dataSets.get(j);
                    nearMatrix[i][j]=Math.sqrt(Math.pow((dataSeti.getRSSIAvg()-dataSetj.getRSSIAvg()),2.0)+
                            Math.pow((dataSeti.getNum()-dataSetj.getNum()),2.0)+
                            Math.pow((dataSeti.getTime()-dataSetj.getTime()),2.0));
                    if(nearMatrix[i][j]>maxNear)maxNear=nearMatrix[i][j];
                }
            }
        }
        List<Integer> indexLead=new ArrayList<>();//核心对象
        int minPts=100;//近邻下限
        double nearLimit=0.2;
        for(int i=0;i<dataSets.size();i++){
            int nearNum=0;
            for(int j=0;j<dataSets.size();j++){
                nearMatrix[i][j]/=maxNear;//归一化
                if(nearMatrix[i][j]<=nearLimit){
                    nearNum++;
                }
            }
            if(nearNum>=minPts){
                indexLead.add(i);
            }
        }
        //聚类簇群数初始化
        int k=0;
        List<Cluster> clusters=new ArrayList<>();
        for(int q=0;q<indexLead.size();q++){
            if(!dataSets.get(indexLead.get(q)).getCanUse())continue;
            Queue<Integer> queue=new LinkedList<Integer>();
            queue.offer(indexLead.get(q));
            dataSets.get(indexLead.get(q)).setCanUse(false);
            Cluster cluster=new Cluster();
            List<DataSet> dataSetsInCluster=new ArrayList<>();
            while(!queue.isEmpty()){
                int nearNum=0;
                int index=queue.poll();
                dataSetsInCluster.add(dataSets.get(index));
                List<Integer> addList=new ArrayList<>();
                for(int j=0;j<dataSets.size();j++){
                    if(nearMatrix[index][j]<=nearLimit){
                        nearNum++;
                        addList.add(j);
                    }
                }
                if(nearNum>=minPts){
                    for(Integer integer:addList){
                        if(dataSets.get(integer).getCanUse()){
                            queue.offer(integer);
                            dataSets.get(integer).setCanUse(false);
                        }
                    }
                }
            }
            k++;
            cluster.setDataSets(dataSetsInCluster);
            clusters.add(cluster);
        }
        int a=111;
    }

    public List<DataSet> getDataSet(List<Data> datas){
        List<DataSet> dataSets=new ArrayList<>();
        for(Data data:datas){
            Collections.sort(data.getDataDetails(), timeSort);//按时间排序
            Date firstTime=data.getDataDetails().get(data.getDataDetails().size()-1).getTime();
            Date lastTime=data.getDataDetails().get(0).getTime();
            double stayTime=(lastTime.getTime()-firstTime.getTime())/60000.00;
            if(firstTime.getHours()>=8&&lastTime.getHours()<=20&&stayTime>=15){
                DataSet dataSet=new DataSet();
                dataSet.setMac(data.getMAC());
                dataSet.setNum(data.getNum());
                dataSet.setTime(stayTime);
                double sumRSSI=0;
                for(DataDetail dataDetail:data.getDataDetails()){
                    sumRSSI+=dataDetail.getRSSI();
                }
                dataSet.setRSSIAvg(sumRSSI/data.getDataDetails().size());
                dataSet.setCanUse(true);//设置可用
                dataSets.add(dataSet);
            }
        }
        return dataSets;
    }
}
