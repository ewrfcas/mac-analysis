package com.mac.dao;

import com.mac.model.jpa.JPACustom;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by ewrfcas on 2016/12/22.
 */
public interface CustomDao extends PagingAndSortingRepository<JPACustom,String> {
}
