package com.example.transactionalDeadLock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transactionalDeadLock.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
