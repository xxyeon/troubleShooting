package com.excel.dto;

import com.excel.annotations.ExcelColumn;
import com.excel.entity.DescribeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DescribeXlsxDto implements BaseXlsx {
	@ExcelColumn(headerName = "스캔 날짜")
	private String scanTime;
	@ExcelColumn(headerName = "고객사")
	private String resourceRegisterTime;
	@ExcelColumn(headerName = "Account Name")
	private String client;
	@ExcelColumn(headerName = "Account ID")
	private String accountName;
	@ExcelColumn(headerName = "서비스")
	private String accountId;
	@ExcelColumn(headerName = "기능")
	private String service;
	@ExcelColumn(headerName = "Resource ID\"")
	private String resourceId;
	@ExcelColumn(headerName = "tag")
	private String tag;
	@ExcelColumn(headerName = "Json")
	private String resourceJson;

	public static DescribeXlsxDto of(DescribeEntity describeEntity) {
		return new DescribeXlsxDto(
			describeEntity.getScanTime(),
			describeEntity.getClient(),
			describeEntity.getAccountName(),
			describeEntity.getAccountId(),
			describeEntity.getService(),
			describeEntity.getServiceDetail(),
			describeEntity.getResourceId(),
			describeEntity.getTag(),
			describeEntity.getResourceJson()
		);
	}
}
