package com.excel.v1.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;


import com.excel.v1.common.StringUtils;
import com.excel.v1.dto.BaseXlsx;
import com.excel.v1.dto.DescribeFilterDto;
import com.excel.v1.dto.DescribeXlsxDto;
import com.excel.v1.dto.MemberFilterDto;
import com.excel.v1.dto.MemberXlsxDto;
import com.excel.v1.entity.DescribeEntity;
import com.excel.v1.entity.Member;
import com.excel.v1.enums.FillDownloadCase;
import com.excel.v1.enums.Role;
import com.excel.v1.repository.DescribeRepository;
import com.excel.v1.repository.MemberRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@RequiredArgsConstructor
public class DownloadService {

	private Sheet sheet;
	private final HttpServletResponse httpServletResponse;
	private final DescribeRepository describeRepository;
	private final MemberRepository memberRepository;
	// 256 * (최대 출력 글자수)
	private final int MAX_COLUMN_WIDTH = 256 * 30;

	@Setter
	private List<? extends BaseXlsx> writeDataList;

	public void downloadCsv(FillDownloadCase aCase) {
		String localTime = StringUtils.localTimeToFormat("yyyyMMdd");

		// 파일명
		String fileName = "cspm_" + aCase.getFileName() + "_" + localTime + ".xlsx"; // ".cvs" 대신 ".xlsx" 사용

		// attachment : 파일 다운로드 타입으로 설정
		httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		httpServletResponse.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
		httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		try (Workbook workbook = new XSSFWorkbook()) {
			sheet = workbook.createSheet("Data");
			switch (aCase) {
				case RESOURCE_DESCRIBE -> describeDataDownload();
				case MEMBER -> memberDataDownload();
				default -> throw new IllegalArgumentException("Invalid case: " + aCase);
			}

			setColumnWidth();

			// 파일 쓰기
			workbook.write(httpServletResponse.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public List<DescribeXlsxDto> downloadResource(DescribeFilterDto describeFilterDto) {
		List<DescribeEntity> describeEntityList = describeRepository.
			findAllQueryDescription(describeFilterDto);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		Map<AbstractMap.SimpleEntry<String, String>, LocalDateTime> latestScanTimes = describeEntityList.stream()
			.collect(Collectors.groupingBy(
				e -> new AbstractMap.SimpleEntry<>(e.getAccountId(), e.getAccountName()), // 계정 ID로 그룹화
				Collectors.collectingAndThen(
					Collectors.mapping(
						e -> LocalDateTime.parse(e.getScanTime(), formatter),
						Collectors.maxBy(Comparator.naturalOrder())
					),
					(Optional<LocalDateTime> optional) -> optional.orElseThrow(
						() -> new IllegalStateException("No scan_time found"))
				)
			));

		// 최신 스캔 시간에 대한 DescribeEntity 필터링
		List<DescribeEntity> latestEntities = describeEntityList.stream()
			.filter(entity -> {
				LocalDateTime entityScanTime = LocalDateTime.parse(entity.getScanTime(), formatter);
				AbstractMap.SimpleEntry<String, String> key = new AbstractMap.SimpleEntry<>(entity.getAccountId(),
					entity.getAccountName());
				LocalDateTime latestScanTime = latestScanTimes.get(key);
				return entityScanTime.isEqual(latestScanTime);
			})
			.toList();

		return latestEntities.stream().map(DescribeXlsxDto::of).toList();
	}

	private void describeDataDownload() {
		String[] headers = {"스캔 날짜", "고객사", "Account Name", "Account ID", "서비스", "기능", "Resource ID", "tag", "Json"};
		createHeaderRow(headers);

		int rowNum = 1;
		for (Object data : writeDataList) {
			DescribeXlsxDto dto = (DescribeXlsxDto) data;
			Row dataRow = sheet.createRow(rowNum++);
			dataRow.createCell(0).setCellValue(dto.getScanTime());
			dataRow.createCell(1).setCellValue(dto.getResourceRegisterTime());
			dataRow.createCell(2).setCellValue(dto.getClient());
			dataRow.createCell(3).setCellValue(dto.getAccountName());
			dataRow.createCell(4).setCellValue(dto.getAccountId());
			dataRow.createCell(5).setCellValue(dto.getService());
			dataRow.createCell(6).setCellValue(dto.getResourceId());
			dataRow.createCell(7).setCellValue(dto.getTag());
			dataRow.createCell(8).setCellValue(dto.getResourceJson());

		}
	}

	public List<? extends BaseXlsx> downloadMember(MemberFilterDto memberFilterDto) {
		Member found = memberRepository.findById(memberFilterDto.getId()).get();
		String role = found.getRole().getValue();
		String clientName;
		if (role.equals(Role.ROLE_USER.toString())) { //USER 권한이면 User 권한이 속한 고객사에 대해서만 조회
			String userEmail = found.getName();
			Member currentUser = memberRepository.findByEmail(userEmail)
				.orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
			clientName = currentUser.getClientName();


		} else {
			clientName = null;
		}
		return memberRepository.searchMembers(memberFilterDto)
			.stream()
			.filter(member -> clientName == null || member.getClientName().equals(clientName))
			.map(MemberXlsxDto::of)
			.toList();
	}

	private void memberDataDownload() {
		String[] headers = {"ID", "이름", "고객사", "권한", "부서", "직급", "비고"};
		createHeaderRow(headers);

		int rowNum = 1;
		for (Object data : writeDataList) {
			MemberXlsxDto dto = (MemberXlsxDto)data;
			Row dataRow = sheet.createRow(rowNum++);
			dataRow.createCell(0).setCellValue(dto.getEmail());
			dataRow.createCell(1).setCellValue(dto.getName());
			dataRow.createCell(2).setCellValue(dto.getClientName());
			dataRow.createCell(3).setCellValue(dto.getRole());
			dataRow.createCell(4).setCellValue(dto.getDivision());
			dataRow.createCell(5).setCellValue(dto.getRank());
			dataRow.createCell(6).setCellValue(dto.getDescription());
		}
	}

	private void createHeaderRow(String[] headers) {
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
		}
	}

	private void setColumnWidth() {
		Row firstRow = sheet.getRow(0);
		int numberOfColumns = firstRow.getPhysicalNumberOfCells();
		for (int i = 0; i < numberOfColumns; i++) {
			sheet.autoSizeColumn(i);
			String cellValue = firstRow.getCell(i).toString();
			int koreanCharCount = cellValue.replace("[^가-힣]", "").length();
			int adjustedWidth = sheet.getColumnWidth(i);
			if (koreanCharCount > 0) {
				adjustedWidth += 256*koreanCharCount;
			}
			if (adjustedWidth > MAX_COLUMN_WIDTH) {
				adjustedWidth = MAX_COLUMN_WIDTH;
			}
			sheet.setColumnWidth(i, adjustedWidth);
		}
	}

}
