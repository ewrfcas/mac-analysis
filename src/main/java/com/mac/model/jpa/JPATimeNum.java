package com.mac.model.jpa;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by ewrfcas on 2016/12/22.
 */
@Entity
@Data
@Table(name = "time_num")
public class JPATimeNum {
    @Id
    private String date;

    private int time_8;

    private int time_9;

    private int time_10;

    private int time_11;

    private int time_12;

    private int time_13;

    private int time_14;

    private int time_15;

    private int time_16;

    private int time_17;

    private int time_18;

    private int time_19;

    private int time_20;
}
