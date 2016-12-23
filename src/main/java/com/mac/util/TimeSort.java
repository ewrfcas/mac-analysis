package com.mac.util;

import com.mac.model.DataDetail;
import org.springframework.stereotype.Component;

import java.util.Comparator;

/**
 * Created by ewrfcas on 2016/12/22.
 */
@Component
public class TimeSort implements Comparator {
    public int compare(Object a1,Object a2){
        DataDetail b1=(DataDetail) a1;
        DataDetail b2=(DataDetail) a2;
        return b2.getTime().compareTo(b1.getTime());
    }
}
