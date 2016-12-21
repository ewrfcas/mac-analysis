package com.mac.model.jpa;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by ewrfcas on 2016/12/20.
 */
@Entity
@Data
@Table(name = "mac")
public class JPAMac {
    @Id
    private int id;
    @Column(name="mac")
    private String mac;
    @Column(name="num")
    private int num;
}
