package com.mac.model.jpa;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by ewrfcas on 2017/3/7.
 */
//4S店静态数据
@Entity
@Data
@Table(name = "param_data")
public class JPAParamData {
    @Id
    private String store_id;//店id

    private int distance_in;//进店RSSI阈值
    private int distance_ig;//休息室RSSI阈值
    private int minutes;//批次计算时间
}
