package com.excel.v1.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberFilterDto {
	private Long id;
	private String client;
	private String role;
	private String searchData;
	private List<Long> checkList;
}
