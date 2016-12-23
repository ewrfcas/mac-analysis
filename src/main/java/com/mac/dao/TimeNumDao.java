package com.mac.dao;

import com.mac.model.jpa.JPATimeNum;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by ewrfcas on 2016/12/23.
 */
public interface TimeNumDao extends PagingAndSortingRepository<JPATimeNum,String> {
}
