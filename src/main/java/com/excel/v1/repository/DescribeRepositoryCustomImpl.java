package com.excel.v1.repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;

import com.excel.v1.dto.DescribeFilterDto;
import com.excel.v1.entity.ComplianceEntity;
import com.excel.v1.entity.DescribeEntity;
import com.excel.v1.entity.QComplianceEntity;
import com.excel.v1.entity.QDescribeEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.Getter;

public class DescribeRepositoryCustomImpl implements DescribeRepositoryCustom {

	@Getter
	private final JPAQueryFactory queryFactory;
	private final QComplianceEntity qComplianceEntity = QComplianceEntity.complianceEntity;
	private final QDescribeEntity qDescribeEntity = QDescribeEntity.describeEntity;

	public DescribeRepositoryCustomImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public List<DescribeEntity> findAllQueryDescription(DescribeFilterDto describeFilterDto) {

		if (describeFilterDto == null) {
			return queryFactory.selectFrom(qDescribeEntity).fetch();
		}

		BooleanBuilder builder = new BooleanBuilder();

		if (describeFilterDto.getClient() != null && !describeFilterDto.getClient().isEmpty())
			builder.and(qDescribeEntity.client.eq(describeFilterDto.getClient()));
		if (describeFilterDto.getAccountName() != null && !describeFilterDto.getAccountName().isEmpty())
			builder.and(qDescribeEntity.accountName.eq(describeFilterDto.getAccountName()));
		if (describeFilterDto.getAccountId() != null && !describeFilterDto.getAccountId().isEmpty())
			builder.and(qDescribeEntity.accountId.eq(describeFilterDto.getAccountId()));
		if (describeFilterDto.getService() != null && !describeFilterDto.getService().isEmpty())
			builder.and(qDescribeEntity.service.eq(describeFilterDto.getService()));
		if (describeFilterDto.getServiceDetail() != null && !describeFilterDto.getServiceDetail().isEmpty())
			builder.and(qDescribeEntity.serviceDetail.eq(describeFilterDto.getServiceDetail()));

		String fromDate = describeFilterDto.getFromDate();
		String toDate = describeFilterDto.getToDate();
		if (fromDate != null && toDate != null && !toDate.isEmpty() && !fromDate.isEmpty()) {
			builder.and(qDescribeEntity.scanTime.between(fromDate, toDate));
		} else if (fromDate != null && !fromDate.isEmpty()) {
			builder.and(qDescribeEntity.scanTime.goe(fromDate));
		} else if (toDate != null && !toDate.isEmpty()) {
			builder.and(qDescribeEntity.scanTime.loe(toDate));
		}

		if (describeFilterDto.getSearchDate() != null && !describeFilterDto.getSearchDate().isEmpty()) {
			String searchData = describeFilterDto.getSearchDate();
			BooleanExpression likeExpression = qDescribeEntity.client.like("%" + searchData + "%")
				.or(qDescribeEntity.accountName.contains(searchData))
				.or(qDescribeEntity.accountId.contains(searchData))
				.or(qDescribeEntity.service.contains(searchData))
				.or(qDescribeEntity.scanTime.contains(searchData))
				.or(qDescribeEntity.client.contains(searchData))
				.or(qDescribeEntity.tag.contains(searchData))
				.or(qDescribeEntity.serviceDetail.contains(searchData))
				.or(qDescribeEntity.resourceId.contains(searchData))
				.or(qDescribeEntity.resourceJson.contains(searchData))
				.or(qDescribeEntity.resourceValue1.contains(searchData))
				.or(qDescribeEntity.resourceValue2.contains(searchData))
				.or(qDescribeEntity.resourceValue3.contains(searchData))
				.or(qDescribeEntity.resourceValue4.contains(searchData))
				.or(qDescribeEntity.resourceValue5.contains(searchData));
			builder.and(likeExpression);
		}

		// changedORNot 필터링
		if (describeFilterDto.getChangedORNot() != null && !describeFilterDto.getChangedORNot().isEmpty()) {
			if (describeFilterDto.getChangedORNot().equalsIgnoreCase("true")) {
				builder.and(
					qDescribeEntity.id.notIn(
						queryFactory.select(qDescribeEntity.id)
							.from(qDescribeEntity)
							.groupBy(qDescribeEntity.resourceId, qDescribeEntity.serviceDetail,
								qDescribeEntity.accountId, qDescribeEntity.accountName)
							.having(qDescribeEntity.id.count().goe(1L)) // 2 이상인 경우
					)
				);
			} else if (describeFilterDto.getChangedORNot().equalsIgnoreCase("false")) {
				builder.and(
					qDescribeEntity.id.in(
						queryFactory.select(qDescribeEntity.id)
							.from(qDescribeEntity)
							.groupBy(qDescribeEntity.resourceId, qDescribeEntity.serviceDetail,
								qDescribeEntity.accountId, qDescribeEntity.accountName)
							.having(qDescribeEntity.id.count().eq(1L)) // 1인 경우 제외
					)
				);
			}
		}

		List<DescribeEntity> describeEntities = queryFactory.selectFrom(qDescribeEntity)
			.where(builder)
			.fetch();
		List<ComplianceEntity> complianceEntities = queryFactory.selectFrom(qComplianceEntity).fetch();

		Set<String> complianceKeys = complianceEntities.stream()
			.map(e -> generateKey(e.getResourceId(), e.getAccountName(), e.getAccountId(), e.getClient()))
			.collect(Collectors.toSet());

		String compliance = describeFilterDto.getCompliance();
		if (compliance != null && !compliance.isEmpty()) {
			if (compliance.equalsIgnoreCase("true")) {
				// ComplianceEntity에 있는 DescribeEntity만 필터링
				builder.and(qDescribeEntity.id.in(
					describeEntities.stream()
						.filter(e -> complianceKeys.contains(
							generateKey(e.getResourceId(), e.getAccountName(), e.getAccountId(), e.getClient())))
						.map(DescribeEntity::getId)
						.collect(Collectors.toSet())
				));
			} else if (compliance.equalsIgnoreCase("false")) {
				// ComplianceEntity에 없는 DescribeEntity만 필터링
				builder.and(qDescribeEntity.id.in(
					describeEntities.stream()
						.filter(e -> !complianceKeys.contains(
							generateKey(e.getResourceId(), e.getAccountName(), e.getAccountId(), e.getClient())))
						.map(DescribeEntity::getId)
						.collect(Collectors.toSet())
				));
			}
		}

		return queryFactory.selectFrom(qDescribeEntity).where(builder).orderBy(qDescribeEntity.scanTime.desc()).fetch();
	}

	private String generateKey(String resourceId, String accountName, String accountId, String client) {
		return resourceId + "-" + accountName + "-" + accountId + "-" + client;
	}
}
