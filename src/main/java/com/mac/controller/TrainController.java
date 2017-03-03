package com.mac.controller;

import com.mac.model.Data;
import com.mac.model.train.TrainResult;
import com.mac.service.MacService;
import com.mac.service.impl.CutImpl;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ewrfcas on 2017/3/2.
 */
@RestController
public class TrainController {
    @Autowired
    private transient MacService macService;
    @Autowired
    private transient CutImpl cut;
    //训练得到最佳距离;入店距离阈值distanceIn,修车距离阈值distanceIg，批次分钟m
    @RequestMapping(value="/train/data" ,method = RequestMethod.GET)
    public List<TrainResult> train() {
        int[] distanceIns={-66};
        int[] distanceIgs={-66};
        int[] ms={8,9,10,12,14};
        int[] trueDays={20,29,10,22,36,25,23,28,35,25,43,41,26,53};
        String url="origin_139_2017-01-";
        List<List<Data>> lists=new ArrayList<>();
        //数据获取
        try{
            for(int i=1;i<=14;i++){
                String path=url;
                if(i<10)path+="0";
                path+=i;
                path+=".txt";
                List<Data> datas=macService.getDataFromTxt(path);
                lists.add(datas);
            }
        }catch (Exception e){
            System.out.print(e.toString());
            return null;
        }
        List<TrainResult> trainResults=new ArrayList<>();
        for(int q=0;q<distanceIns.length;q++){
            for(int w=0;w<distanceIgs.length;w++){
                for(int e=0;e<ms.length;e++){
                    //备份（非常重要）
                    List<List<Data>> temp=new ArrayList<>();
                    for(List<Data> datas:lists){
                        List<Data> newTemp=new ArrayList<>();
                        for(Data data:datas){
                            Data data1=new Data();
                            data1.setDataDetails(data.getDataDetails());
                            data1.setMAC(data.getMAC());
                            data1.setMinDistance(data.getMinDistance());
                            data1.setMacs(data.getMacs());
                            data1.setNum(data.getNum());
                            newTemp.add(data1);
                        }
                        temp.add(newTemp);
                    }
                    //备份over
                    TrainResult trainResult=new TrainResult();
                    int[] days=new int[14];
                    int dayDiff=0;
                    double perDiff=0;
                    for(int i=0;i<lists.size();i++){
                        List<Data> temp2=new ArrayList<>();
                        //初步删减28
                        temp2=cut.cutToCustom(distanceIns[q],distanceIgs[w],temp.get(i));
                        //批次合并
                        temp2=cut.allInOne(ms[e],temp2);
                        days[i]=temp2.size();
                        dayDiff+=Math.abs(days[i]-trueDays[i]);
                        double sub=Math.abs(days[i]-trueDays[i]);
                        double range=(days[i]+trueDays[i])/2.000;
                        perDiff+=(sub/range);
                    }
                    trainResult.setDistanceIn(distanceIns[q]);
                    trainResult.setDistanceIg(distanceIgs[w]);
                    trainResult.setM(ms[e]);
                    trainResult.setDays(days);
                    trainResult.setDayDiff(dayDiff);
                    trainResult.setPerDiff(perDiff/14.000);
                    trainResults.add(trainResult);
                }
            }
        }
        Collections.sort(trainResults, new Comparator<TrainResult>() {
            @Override
            public int compare(TrainResult o1, TrainResult o2) {
                if(o1.getPerDiff()-o2.getPerDiff()<0)return -1;
                else if(o1.getPerDiff()-o2.getPerDiff()==0)return 0;
                else return 1;
            }
        });
        try{
            //Excel
            File excelFile = new File("C:\\Users\\ewrfcas\\Desktop\\mac-analysis\\result\\阈值训练结果.xlsx");
            OutputStream outputStream=new FileOutputStream(excelFile);
            XSSFWorkbook excelWrite = new XSSFWorkbook();
            XSSFSheet sheetOut=excelWrite.createSheet("trainResult");
            XSSFRow rowOut = sheetOut.createRow(0);
            rowOut.createCell(0).setCellValue("rank");
            rowOut.createCell(1).setCellValue("daysDiff");
            rowOut.createCell(2).setCellValue("perDiff");
            rowOut.createCell(3).setCellValue("distanceIn");
            rowOut.createCell(4).setCellValue("distanceIg");
            rowOut.createCell(5).setCellValue("minute");
            for(int i=6;i<=19;i++){
                rowOut.createCell(i).setCellValue("day"+(i-6));
            }
            int i=1;
            for(TrainResult trainResult:trainResults){
                rowOut=sheetOut.createRow(i);
                rowOut.createCell(0).setCellValue(i++);
                rowOut.createCell(1).setCellValue(trainResult.getDayDiff());
                rowOut.createCell(2).setCellValue(trainResult.getPerDiff());
                rowOut.createCell(3).setCellValue(trainResult.getDistanceIn());
                rowOut.createCell(4).setCellValue(trainResult.getDistanceIg());
                rowOut.createCell(5).setCellValue(trainResult.getM());
                for(int j=6;j<=19;j++){
                    rowOut.createCell(j).setCellValue(trainResult.getDays()[j-6]);
                }
            }
            excelWrite.write(outputStream);
            outputStream.flush();
            outputStream.close();
            //Excel end
        }catch (Exception e){
            return trainResults;
        }
        return trainResults;
    }
}
