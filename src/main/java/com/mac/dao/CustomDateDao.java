package com.mac.dao;

import com.mac.model.jpa.JPACustomDate;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by ewrfcas on 2016/12/22.
 */
public interface CustomDateDao extends PagingAndSortingRepository<JPACustomDate,String> {
}
