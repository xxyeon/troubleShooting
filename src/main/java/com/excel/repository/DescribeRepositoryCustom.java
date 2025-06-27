package com.excel.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.excel.dto.DescribeFilterDto;
import com.excel.entity.DescribeEntity;

@Repository
public interface DescribeRepositoryCustom {
	List<DescribeEntity> findAllQueryDescription(DescribeFilterDto describeFilterDto);
}
