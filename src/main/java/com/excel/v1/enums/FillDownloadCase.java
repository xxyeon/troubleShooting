package com.excel.v1.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FillDownloadCase {
	ACCOUNT("scanlist"),
	RESOURCE_DESCRIBE("describe"),
	DESCRIIBE_TO_COMPLIANCE("compliance"),
	ALL_POLICY("policy"),
	MEMBER("member");

	private final String fileName;
}
