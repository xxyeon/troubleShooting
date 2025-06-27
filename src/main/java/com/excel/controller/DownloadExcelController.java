package com.excel.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.excel.dto.BaseXlsx;
import com.excel.dto.DescribeFilterDto;
import com.excel.dto.DescribeXlsxDto;
import com.excel.dto.MemberFilterDto;
import com.excel.dto.MemberXlsxDto;
import com.excel.enums.FillDownloadCase;
import com.excel.excel.SimpleExcelFile;
import com.excel.service.DownloadService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@RestController
public class DownloadExcelController {
	private final DownloadService downloadService;

	// 기존에 3개의 feature 가 존재했음
	@PostMapping("/resource")
	public void downloadDescribeData(@RequestBody DescribeFilterDto describeFilterDto) {
		StopWatch sw = new StopWatch();
		sw.start();
		List<? extends BaseXlsx> execlList = downloadService.downloadResource(describeFilterDto);
		downloadService.setWriteDataList(execlList);
		downloadService.downloadCsv(FillDownloadCase.RESOURCE_DESCRIBE);
		sw.stop();
		System.out.println("Elapsed time: " + sw.getTotalTimeMillis() + " ms");
	}

	//추가된 feature
	@PostMapping("/members")
	public void downloadMemberData(@RequestBody MemberFilterDto memberFilterDto) {
		List<? extends BaseXlsx> execlList = downloadService.downloadMember(memberFilterDto);
		downloadService.setWriteDataList(execlList);
		downloadService.downloadCsv(FillDownloadCase.MEMBER);
	}

	// 기존에 3개의 feature 가 존재했음
	@PostMapping("/v2/resource")
	public void downloadDescribeData(HttpServletResponse response, @RequestBody DescribeFilterDto describeFilterDto) throws
		IOException {
		StopWatch sw = new StopWatch();
		sw.start();
		List<DescribeXlsxDto> execlList = downloadService.downloadResource(describeFilterDto);
		SimpleExcelFile<DescribeXlsxDto> excelFile = new SimpleExcelFile<>(execlList, DescribeXlsxDto.class);
		excelFile.write(response.getOutputStream());
		sw.stop();
		System.out.println("Elapsed time: " + sw.getTotalTimeMillis() + " ms");
	}

	@PostMapping("/v2/member")
	public void downloadMemberData(HttpServletResponse response, @RequestBody MemberFilterDto memberFilterDto) throws
		IOException {
		System.out.println("멤버 엑셀 다운 요청");
		List<MemberXlsxDto> execlList = downloadService.downloadMember(memberFilterDto);
		SimpleExcelFile<MemberXlsxDto> excelFile = new SimpleExcelFile<>(execlList, MemberXlsxDto.class);
		excelFile.write(response.getOutputStream());
	}
}
