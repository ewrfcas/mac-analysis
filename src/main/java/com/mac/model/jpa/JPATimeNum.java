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
@Table(name = "time_num")
public class JPATimeNum {
    @Id
    private String id;

    private int time;

    private int num;

    private Date date;
}
