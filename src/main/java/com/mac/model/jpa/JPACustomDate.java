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
@Table(name = "mac_data_date")
public class JPACustomDate {

    @Id
    private String date;

    private int device_num;

    private int custom_num;

    private int custom_first_num;

    private int custom_hi_num;

    private double avg_stay_time;

}
