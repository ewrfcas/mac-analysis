package com.mac.service.impl;

import com.mac.dao.CustomDao;
import com.mac.dao.CustomDateDao;
import com.mac.dao.TimeNumDao;
import com.mac.model.*;
import com.mac.model.jpa.JPACustom;
import com.mac.model.jpa.JPACustomDate;
import com.mac.model.jpa.JPATimeNum;
import com.mac.service.MacService;
import com.mac.util.Response;
import com.mac.util.ResponseStatus;
import com.mac.util.TimeSort;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ewrfcas on 2016/12/20.
 */
@SuppressWarnings("all")
@Service
public class MacServiceImpl implements MacService {
    @Autowired
    private transient CustomDao customDao;
    @Autowired
    private transient CustomDateDao customDateDao;
    @Autowired
    private transient TimeNumDao timeNumDao;
    @Autowired
    private transient TimeSort timeSort;

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

    @Override
    public Response<CustomDateRow> save(String fileName){
        Response<CustomDateRow>response=new Response<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        try{
            List<Data> datas=new ArrayList<Data>();
            BufferedReader brname = new BufferedReader(new FileReader("C:\\Users\\ewrfcas\\Desktop\\mac-analysis\\"+fileName));
            String s=null;
            boolean contian=false;
            //解析数据
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
            //sql存储
            String idTime=sdf2.format(datas.get(0).getDataDetails().get(0).getTime());
            JPACustomDate jpaCustomDate=new JPACustomDate();
            jpaCustomDate.setCustom_hi_num(0);//未实现
            jpaCustomDate.setCustom_first_num(0);
            jpaCustomDate.setCustom_num(0);
            jpaCustomDate.setAvg_stay_time(0);
            jpaCustomDate.setDevice_num(datas.size());
            jpaCustomDate.setDate(idTime);
            //统计早上8点到晚上20点各个时间段人数
            HashMap<Integer,Integer> timeHashMap=new HashMap<>();
            for(int i=8;i<=20;i++){
                timeHashMap.put(i,0);
            }
            for(Data data:datas){
                if(data.getNum()>2&&data.getNum()<200){//筛选非客户
                    Collections.sort(data.getDataDetails(),timeSort);
                    for(DataDetail dataDetail:data.getDataDetails()){//遍历得到各时间段情况
                        int hour=dataDetail.getTime().getHours();
                        if(hour>=8&&hour<=20){
                            timeHashMap.put(hour,timeHashMap.get(hour)+1);
                        }
                    }
                    Date firstTime=data.getDataDetails().get(data.getDataDetails().size()-1).getTime();
                    Date lastTime=data.getDataDetails().get(0).getTime();
                    jpaCustomDate.setCustom_num(jpaCustomDate.getCustom_num() + 1);
                    if(!customDao.exists(data.getMAC())){
                        //判断为首次入店客户
                        jpaCustomDate.setCustom_first_num(jpaCustomDate.getCustom_first_num()+1);
                        JPACustom jpaCustom=new JPACustom();
                        jpaCustom.setMac(data.getMAC());
                        jpaCustom.setTime_first(data.getDataDetails().get(0).getTime());
                        customDao.save(jpaCustom);
                    }
                    //计算驻店时长
                    double stayTime=(lastTime.getTime()-firstTime.getTime())/60000.00;
                    jpaCustomDate.setAvg_stay_time(jpaCustomDate.getAvg_stay_time()+stayTime);
                }
            }
            jpaCustomDate.setAvg_stay_time(jpaCustomDate.getAvg_stay_time() / jpaCustomDate.getCustom_num());
            if(customDateDao.exists(idTime)){
                customDateDao.delete(idTime);
            }
            customDateDao.save(jpaCustomDate);
            List<Integer> numFrom8To20=new ArrayList<>();
            JPATimeNum jpaTimeNum=new JPATimeNum();
            jpaTimeNum.setDate(idTime);
            for(int i=8;i<=20;i++){
                switch (i){
                    case 8:jpaTimeNum.setTime_8(timeHashMap.get(i));break;
                    case 9:jpaTimeNum.setTime_9(timeHashMap.get(i));break;
                    case 10:jpaTimeNum.setTime_10(timeHashMap.get(i));break;
                    case 11:jpaTimeNum.setTime_11(timeHashMap.get(i));break;
                    case 12:jpaTimeNum.setTime_12(timeHashMap.get(i));break;
                    case 13:jpaTimeNum.setTime_13(timeHashMap.get(i));break;
                    case 14:jpaTimeNum.setTime_14(timeHashMap.get(i));break;
                    case 15:jpaTimeNum.setTime_15(timeHashMap.get(i));break;
                    case 16:jpaTimeNum.setTime_16(timeHashMap.get(i));break;
                    case 17:jpaTimeNum.setTime_17(timeHashMap.get(i));break;
                    case 18:jpaTimeNum.setTime_18(timeHashMap.get(i));break;
                    case 19:jpaTimeNum.setTime_19(timeHashMap.get(i));break;
                    case 20:jpaTimeNum.setTime_20(timeHashMap.get(i));break;
                }
                numFrom8To20.add(timeHashMap.get(i));
            }
            if(timeNumDao.exists(idTime)){
                timeNumDao.delete(idTime);
            }
            timeNumDao.save(jpaTimeNum);
            //返回成功插入的数据
            CustomDateRow customDateRow=new CustomDateRow();
            customDateRow.setDayNum(1);
            customDateRow.setNumFrom8To20(numFrom8To20);
            CustomDataWithDate customDataWithDate=new CustomDataWithDate();
            customDataWithDate.setDate(sdf2.parse(jpaCustomDate.getDate()));
            customDataWithDate.setCustomAllD(jpaCustomDate.getCustom_num());
            customDataWithDate.setCustomFirstD(jpaCustomDate.getCustom_first_num());
            customDataWithDate.setCustomHID(jpaCustomDate.getCustom_hi_num());
            customDataWithDate.setStayTimeD(jpaCustomDate.getAvg_stay_time());
            List<CustomDataWithDate> customDataWithDates=new ArrayList<>();
            customDataWithDates.add(customDataWithDate);
            customDateRow.setCustomDataWithDates(customDataWithDates);
            response.setStatus(ResponseStatus.SUCCESS);
            response.setMessage("数据插入成功");
            response.setData(customDateRow);
            return response;
        }catch (Exception e){
            e.printStackTrace();;
            response.setStatus(ResponseStatus.FAIL);
            response.setMessage(e.toString());
            return response;
        }
    }

    @Override
    public Response<CustomData> getAllCustoms(String startTime,String endTime){
        Response<CustomData> response=new Response<>();
        CustomData customData=new CustomData();
        customData.setCustomAll(0);
        customData.setCustomFirst(0);
        customData.setCustomHI(0);
        customData.setDeviceNum(0);
        customData.setStayTime(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date startDate=sdf.parse(startTime);
            Date endDate=sdf.parse(endTime);
            //获取期间每日数据
            CustomDateRow customDateRow=new CustomDateRow();
            int dayNum=(int)((endDate.getTime()-startDate.getTime())/1000*60*24)+1;
            customDateRow.setDayNum(dayNum);
            Date tempDate=startDate;
            Calendar cal = Calendar.getInstance();
            List<Integer> numFrom8To20=new ArrayList<>();
            for(int i=0;i<=12;i++){
                numFrom8To20.add(0);
            }
            List<CustomDataWithDate> customDataWithDates=new ArrayList<>();
            JPATimeNum jpaTimeNum=new JPATimeNum();
            JPACustomDate jpaCustomDate=new JPACustomDate();
            for(int i=0;i<dayNum;i++){
                jpaCustomDate=customDateDao.findOne(sdf2.format(tempDate));
                CustomDataWithDate customDataWithDate=new CustomDataWithDate();
                customDataWithDate.setDate(sdf2.parse(jpaCustomDate.getDate()));
                customDataWithDate.setCustomHID(jpaCustomDate.getCustom_hi_num());
                customDataWithDate.setCustomFirstD(jpaCustomDate.getCustom_first_num());
                customDataWithDate.setCustomAllD(jpaCustomDate.getCustom_num());
                customDataWithDate.setStayTimeD(jpaCustomDate.getAvg_stay_time());
                customDataWithDates.add(customDataWithDate);
                jpaTimeNum=timeNumDao.findOne(sdf2.format(tempDate));
                numFrom8To20.set(0,numFrom8To20.get(0)+jpaTimeNum.getTime_8()/dayNum);
                numFrom8To20.set(1,numFrom8To20.get(1)+jpaTimeNum.getTime_9()/dayNum);
                numFrom8To20.set(2,numFrom8To20.get(2)+jpaTimeNum.getTime_10()/dayNum);
                numFrom8To20.set(3,numFrom8To20.get(3)+jpaTimeNum.getTime_11()/dayNum);
                numFrom8To20.set(4,numFrom8To20.get(4)+jpaTimeNum.getTime_12()/dayNum);
                numFrom8To20.set(5,numFrom8To20.get(5)+jpaTimeNum.getTime_13()/dayNum);
                numFrom8To20.set(6,numFrom8To20.get(6)+jpaTimeNum.getTime_14()/dayNum);
                numFrom8To20.set(7,numFrom8To20.get(7)+jpaTimeNum.getTime_15()/dayNum);
                numFrom8To20.set(8,numFrom8To20.get(8)+jpaTimeNum.getTime_16()/dayNum);
                numFrom8To20.set(9,numFrom8To20.get(9)+jpaTimeNum.getTime_17()/dayNum);
                numFrom8To20.set(10,numFrom8To20.get(10)+jpaTimeNum.getTime_18()/dayNum);
                numFrom8To20.set(11,numFrom8To20.get(11)+jpaTimeNum.getTime_19()/dayNum);
                numFrom8To20.set(12, numFrom8To20.get(12) + jpaTimeNum.getTime_20() / dayNum);
                customData.setDeviceNum(customData.getDeviceNum()+jpaCustomDate.getDevice_num());
                customData.setStayTime(customData.getStayTime()+jpaCustomDate.getAvg_stay_time()/dayNum);
                customData.setCustomHI(customData.getCustomHI()+jpaCustomDate.getCustom_hi_num());
                customData.setCustomFirst(customData.getCustomFirst()+jpaCustomDate.getCustom_first_num());
                customData.setCustomAll(customData.getCustomAll()+jpaCustomDate.getCustom_num());
                cal.setTime(tempDate);
                cal.add(Calendar.DATE, 1);
                tempDate=cal.getTime();
            }
            customDateRow.setCustomDataWithDates(customDataWithDates);
            customDateRow.setNumFrom8To20(numFrom8To20);
            customData.setCustomDateRow(customDateRow);
            response.setStatus(ResponseStatus.SUCCESS);
            response.setMessage("数据查询成功");
            response.setData(customData);
            return response;
        }catch (Exception e){
            response.setStatus(ResponseStatus.FAIL);
            response.setMessage(e.toString());
            return response;
        }
    }
}
