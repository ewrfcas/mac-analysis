package com.mac.dao;

import com.mac.model.jpa.JPAParamData;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by ewrfcas on 2017/3/7.
 */
public interface ParamDataDao extends PagingAndSortingRepository<JPAParamData,String> {
}
