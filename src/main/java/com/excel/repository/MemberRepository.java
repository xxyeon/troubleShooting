package com.excel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.excel.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
	Optional<Member> findByEmail(String email);

	List<Member> findAllByEmailIn(List<String> emails);

	void delete(Member member);

	List<Member> findByClientName(String clientName);
}
