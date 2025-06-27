package com.excel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.excel.entity.DescribeEntity;

@Repository
public interface DescribeRepository extends JpaRepository<DescribeEntity, Long>, DescribeRepositoryCustom {
}
