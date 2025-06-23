package com.excel.v1.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import com.excel.v1.dto.MemberFilterDto;
import com.excel.v1.entity.Member;
import com.excel.v1.entity.QMember;
import com.excel.v1.enums.Role;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;


public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

	private final JPAQueryFactory memberJpaQueryFactory;
	private final QMember qMember = QMember.member;

	public MemberRepositoryCustomImpl(JPAQueryFactory memberJpaQueryFactory) {
		this.memberJpaQueryFactory = memberJpaQueryFactory;
	}

	@Override
	public List<Member> searchMembers(String clientName, String role, String searchTerm) {
		QMember member = QMember.member;
		BooleanBuilder builder = new BooleanBuilder();
		if (clientName != null && !clientName.isEmpty()) {
			builder.and(member.clientName.eq(clientName));
		}

		if (role != null && !role.isEmpty()) {
			builder.and(member.role.eq(Role.fromValue(role)));
		}

		if (searchTerm != null && !searchTerm.isEmpty()) {
			builder.and(member.email.containsIgnoreCase(searchTerm)
				.or(member.name.containsIgnoreCase(searchTerm))
				.or(member.rank.containsIgnoreCase(searchTerm))
				.or(member.division.containsIgnoreCase(searchTerm))
				.or(member.description.containsIgnoreCase(searchTerm)));
		}

		return memberJpaQueryFactory.selectFrom(member)
			.where(builder)
			.fetch();
	}

	@Override
	public List<Member> searchMembers(MemberFilterDto dto) {
		String clientName = dto.getClient();
		String role = dto.getRole();
		String searchTerm = dto.getSearchData();
		List<Long> checkList = dto.getCheckList();

		QMember member = QMember.member;
		BooleanBuilder builder = new BooleanBuilder();

		if (!checkList.isEmpty()){
			builder.and(member.id.in(checkList));
		}

		if (clientName != null && !clientName.isEmpty()) {
			builder.and(member.clientName.eq(clientName));
		}

		if (role != null && !role.isEmpty()) {
			builder.and(member.role.eq(Role.fromValue(role)));
		}

		if (searchTerm != null && !searchTerm.isEmpty()) {
			builder.and(member.email.containsIgnoreCase(searchTerm)
					.or(member.name.containsIgnoreCase(searchTerm))
					.or(member.rank.containsIgnoreCase(searchTerm))
					.or(member.division.containsIgnoreCase(searchTerm))
					.or(member.description.containsIgnoreCase(searchTerm)));
		}

		return memberJpaQueryFactory.selectFrom(member)
				.where(builder)
				.fetch();
	}

}
