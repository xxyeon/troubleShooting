package com.excel.repository;

import java.util.List;

import com.excel.dto.MemberFilterDto;
import com.excel.entity.Member;

public interface MemberRepositoryCustom {
	List<Member> searchMembers(String clientName, String role, String searchTerm);
	List<Member> searchMembers(MemberFilterDto dto);
}
