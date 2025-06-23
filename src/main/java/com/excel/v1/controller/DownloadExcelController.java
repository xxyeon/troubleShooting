package com.excel.v1.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.excel.v1.dto.BaseXlsx;
import com.excel.v1.dto.DescribeFilterDto;
import com.excel.v1.dto.MemberFilterDto;
import com.excel.v1.dto.PolicyDownloadDto;
import com.excel.v1.enums.FillDownloadCase;
import com.excel.v1.service.DownloadService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class DownloadExcelController {
	private final DownloadService downloadService;

	// 기존에 3개의 feature 가 존재했음
	@PostMapping("/resource")
	public void downloadDescribeData(@RequestBody DescribeFilterDto describeFilterDto) {
		List<? extends BaseXlsx> execlList = downloadService.downloadResource(describeFilterDto);
		downloadService.setWriteDataList(execlList);
		downloadService.downloadCsv(FillDownloadCase.RESOURCE_DESCRIBE);
	}

	//추가된 feature
	@PostMapping("/members")
	public void downloadMemberData(@RequestBody MemberFilterDto memberFilterDto) {
		List<? extends BaseXlsx> execlList = downloadService.downloadMember(memberFilterDto);
		downloadService.setWriteDataList(execlList);
		downloadService.downloadCsv(FillDownloadCase.MEMBER);
	}
}
