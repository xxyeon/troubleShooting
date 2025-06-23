package com.excel.v1.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class PolicyDownloadDto {
	private String client;
	private String policyGroup;
	private String policyCompliance;
	private String policySeverity;
	private String policyType;
	private String searchData;
	private String service;
	// policyGroupPolicy Id List
	private List<Long> checkList;
}
