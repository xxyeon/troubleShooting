package com.excel.v1.repository;

import java.util.List;

import com.excel.v1.dto.MemberFilterDto;
import com.excel.v1.entity.Member;

public interface MemberRepositoryCustom {
	List<Member> searchMembers(String clientName, String role, String searchTerm);
	List<Member> searchMembers(MemberFilterDto dto);
}
