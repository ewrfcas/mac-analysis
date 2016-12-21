package com.mac.dao;

import com.mac.model.jpa.JPAMac;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by ewrfcas on 2016/12/20.
 */
public interface MacDao extends PagingAndSortingRepository<JPAMac,String> {
    @Query("select m from JPAMac m where m.mac=?1")
    public JPAMac findMacByMac(String mac);
}
