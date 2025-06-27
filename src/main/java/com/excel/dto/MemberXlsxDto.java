package com.excel.dto;

import com.excel.annotations.ExcelColumn;
import com.excel.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class MemberXlsxDto implements BaseXlsx {
	private Long id; // 멤버 ID
	@ExcelColumn(headerName = "고객사")
	private String clientName; // 고객사
	@ExcelColumn(headerName = "이름")
	private String name; // 이름
	@ExcelColumn(headerName = "ID")
	private String email; // 이메일
	@ExcelColumn(headerName = "권한")
	private String role; // 권한
	@ExcelColumn(headerName = "부서")
	private String division; // 부서
	@ExcelColumn(headerName = "직급")
	private String rank; // 직급
	@ExcelColumn(headerName = "비고")
	private String description; // 비고

	public static MemberXlsxDto of(Member member) {
		return new MemberXlsxDto.MemberXlsxDtoBuilder()
			.email(member.getEmail())
			.name(member.getName())
			.clientName(member.getClientName())
			.role(member.getRole().getValue())
			.division(member.getDivision())
			.rank(member.getRank())
			.description(member.getDescription())
			.build();
	}
}
