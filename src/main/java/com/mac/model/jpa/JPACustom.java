package com.mac.model.jpa;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by ewrfcas on 2016/12/22.
 */
@Entity
@Data
@Table(name = "custom")
public class JPACustom {
    @Id
    private String mac;

    private Date time_first;

    //是否可能是员工
    private int is_staff;

    //上次统计的时间
    private Date last_sta_time;

    //一周内统计到的次数（天）
    private int count;
}
