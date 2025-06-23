package com.excel.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.excel.v1.entity.DescribeEntity;

@Repository
public interface DescribeRepository extends JpaRepository<DescribeEntity, Long>, DescribeRepositoryCustom {
}
