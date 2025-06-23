package com.excel.v1.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.excel.v1.dto.DescribeFilterDto;
import com.excel.v1.entity.DescribeEntity;

@Repository
public interface DescribeRepositoryCustom {
	List<DescribeEntity> findAllQueryDescription(DescribeFilterDto describeFilterDto);
}
