package com.excel.v1.dto;

import com.excel.v1.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class MemberXlsxDto implements BaseXlsx {
	//MemberAllProfileResponse 와 겹치는 필드가 많음
	private Long id; // 멤버 ID
	private String clientName; // 고객사
	private String name; // 이름
	private String email; // 이메일
	private String role; // 권한
	private String division; // 부서
	private String rank; // 직급
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
