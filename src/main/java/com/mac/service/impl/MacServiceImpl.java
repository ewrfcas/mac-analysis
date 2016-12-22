package com.mac.service.impl;

import com.mac.dao.CustomDao;
import com.mac.dao.CustomDateDao;
import com.mac.dao.MacDao;
import com.mac.model.Data;
import com.mac.model.DataDetail;
import com.mac.model.jpa.JPACustom;
import com.mac.model.jpa.JPACustomDate;
import com.mac.model.jpa.JPAMac;
import com.mac.service.MacService;
import com.mac.util.Response;
import com.mac.util.ResponseStatus;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ewrfcas on 2016/12/20.
 */
@SuppressWarnings("all")
@Service
public class MacServiceImpl implements MacService {
    @Autowired
    private transient MacDao macDao;
    @Autowired
    private transient CustomDao customDao;
    @Autowired
    private transient CustomDateDao customDateDao;

    @Override
    public Response<String> analysis(){
        Response<String> response=new Response<>();
        List<Data> datas=new ArrayList<Data>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            BufferedReader brname = new BufferedReader(new FileReader("C:\\Users\\ewrfcas\\Desktop\\mac-analysis\\2016-11-21_store_139.txt"));
            BufferedWriter bwname = new BufferedWriter(new FileWriter("C:\\Users\\ewrfcas\\Desktop\\mac-analysis\\result.json"));
            File excelFile = new File("C:\\Users\\ewrfcas\\Desktop\\mac-analysis\\分析结果.xlsx");
            OutputStream outputStream=new FileOutputStream(excelFile);
            XSSFWorkbook excelWrite = new XSSFWorkbook();
            String s=null;
            boolean contian=false;
            while((s=brname.readLine())!=null){
                contian=false;
                JSONObject json=new JSONObject(s);
                for(Data data:datas){
                    if(data.getMAC().equals(json.getString("MAC"))){
                        contian=true;
                        data.setNum(data.getNum() + 1);
                        DataDetail dataDetail=new DataDetail();
                        dataDetail.setDeviceId(json.getString("Device"));
                        dataDetail.setRSSI(json.getInt("RSSI"));
                        dataDetail.setTime(sdf.parse(json.getString("Time")));
                        data.getDataDetails().add(dataDetail);
                    }
                }
                if(!contian){
                    Data data=new Data();
                    data.setNum(1);
                    data.setMAC(json.getString("MAC"));
                    DataDetail dataDetail=new DataDetail();
                    dataDetail.setDeviceId(json.getString("Device"));
                    dataDetail.setRSSI(json.getInt("RSSI"));
                    dataDetail.setTime(sdf.parse(json.getString("Time")));
                    data.setDataDetails(new ArrayList<DataDetail>());
                    data.getDataDetails().add(dataDetail);
                    datas.add(data);
                }
            }
            //Excel
            XSSFSheet sheetOut=excelWrite.createSheet("data");
            XSSFRow rowOut = sheetOut.createRow(0);
            rowOut.createCell(0).setCellValue("num");
            rowOut.createCell(1).setCellValue("num_of_people");
            HashMap<Integer,Integer> map=new HashMap<Integer,Integer>();
            for(int i=0;i<datas.size();i++){
                if(map.containsKey(datas.get(i).getNum())){
                    map.put(datas.get(i).getNum(),map.get(datas.get(i).getNum())+1);
                }else{
                    map.put(datas.get(i).getNum(),1);
                }
            }
            int i=1;
            for(Integer num:map.keySet()){
                rowOut=sheetOut.createRow(i++);
                rowOut.createCell(0).setCellValue(num);
                rowOut.createCell(1).setCellValue(map.get(num));
            }
            //Excel

            //json
            for(Data data:datas){
                bwname.write("{");
                bwname.write("\"MAC\":" +"\""+ data.getMAC()+"\"");
                bwname.write(",");
                bwname.write("\"Num\":"+"\""+data.getNum()+"\"");
                bwname.write("}");
                bwname.newLine();
//                for(DataDetail dataDetail:data.getDataDetails()){
//                    bwname.write("Device:"+dataDetail.getDeviceId()+" ");
//                    bwname.write("RSSI:" + dataDetail.getRSSI() + " ");
//                    bwname.write("Time:" + dataDetail.getTime() + " ");
//                }
//                bwname.newLine();
//                bwname.newLine();
            }
            //json

            excelWrite.write(outputStream);
            outputStream.flush();
            outputStream.close();
            brname.close();
            bwname.flush();
            bwname.close();
            response.setStatus(ResponseStatus.SUCCESS);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            response.setStatus(ResponseStatus.FAIL);
            return response;
        }
    }

    public Response<JPAMac> getJPAMacByMac(String mac){
        Response<JPAMac>response=new Response<>();
        return response;
    }

    public Response<String> save(String fileName){
        Response<String>response=new Response<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            List<Data> datas=new ArrayList<Data>();
            BufferedReader brname = new BufferedReader(new FileReader("C:\\Users\\ewrfcas\\Desktop\\mac-analysis\\"+fileName));
            String s=null;
            boolean contian=false;
            while((s=brname.readLine())!=null){
                contian=false;
                JSONObject json=new JSONObject(s);
                for(Data data:datas){
                    if(data.getMAC().equals(json.getString("MAC"))){
                        contian=true;
                        data.setNum(data.getNum() + 1);
                        DataDetail dataDetail=new DataDetail();
                        dataDetail.setDeviceId(json.getString("Device"));
                        dataDetail.setRSSI(json.getInt("RSSI"));
                        dataDetail.setTime(sdf.parse(json.getString("Time")));
                        data.getDataDetails().add(dataDetail);
                    }
                }
                if(!contian){
                    Data data=new Data();
                    data.setNum(1);
                    data.setMAC(json.getString("MAC"));
                    DataDetail dataDetail=new DataDetail();
                    dataDetail.setDeviceId(json.getString("Device"));
                    dataDetail.setRSSI(json.getInt("RSSI"));
                    dataDetail.setTime(sdf.parse(json.getString("Time")));
                    data.setDataDetails(new ArrayList<DataDetail>());
                    data.getDataDetails().add(dataDetail);
                    datas.add(data);
                }
            }
            JPACustomDate jpaCustomDate=new JPACustomDate();
            jpaCustomDate.setCustom_hi_num(0);//未实现
            jpaCustomDate.setCustom_first_num(0);
            jpaCustomDate.setCustom_num(0);
            jpaCustomDate.setAvg_stay_time(0);
            jpaCustomDate.setDevice_num(datas.size());
            jpaCustomDate.setDate(new Date());
            jpaCustomDate.setId(sdf.format(new Date()));
            for(Data data:datas){
                if(data.getNum()>2&&data.getNum()<200){
                    jpaCustomDate.setCustom_num(jpaCustomDate.getCustom_num() + 1);
                    if(!customDao.exists(data.getMAC())){
                        jpaCustomDate.setCustom_first_num(jpaCustomDate.getCustom_first_num()+1);
                        JPACustom jpaCustom=new JPACustom();
                        jpaCustom.setMac(data.getMAC());
                        Date firstTime=new Date();
                        for(DataDetail dataDetail:data.getDataDetails()){
                            if(dataDetail.getTime().before(firstTime)){
                                firstTime=dataDetail.getTime();
                            }
                        }
                        jpaCustom.setTime_first(firstTime);
                        customDao.save(jpaCustom);
                    }
                    Date firstTime=new Date();
                    Date lastTime=new Date();
                    lastTime.setTime(0);
                    for(DataDetail dataDetail:data.getDataDetails()){
                        if(dataDetail.getTime().before(firstTime)){
                            firstTime=dataDetail.getTime();
                        }
                        if(dataDetail.getTime().after(lastTime)){
                            lastTime=dataDetail.getTime();
                        }
                    }
                    double stayTime=(lastTime.getTime()-firstTime.getTime())/60000.00;
                    jpaCustomDate.setAvg_stay_time(jpaCustomDate.getAvg_stay_time()+stayTime);
                }
            }
            jpaCustomDate.setAvg_stay_time(jpaCustomDate.getAvg_stay_time()/jpaCustomDate.getCustom_num());
            customDateDao.save(jpaCustomDate);
            response.setStatus(ResponseStatus.SUCCESS);
            response.setMessage("数据插入成功");
            return response;
        }catch (Exception e){
            e.printStackTrace();;
            response.setStatus(ResponseStatus.FAIL);
            response.setMessage(e.toString());
            return response;
        }
    }
}
