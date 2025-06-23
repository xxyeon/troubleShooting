package com.excel.v1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DescribeFilterDto {

	private String client;
	private String accountName;
	private String accountId;
	private String service;
	private String fromDate;
	private String toDate;
	private String searchDate;
	private String serviceDetail;
	private String changedORNot;
	private String compliance;
}
