package com.example.transactionalDeadLock.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transactionalDeadLock.entity.Board;
import com.example.transactionalDeadLock.entity.Like;
import com.example.transactionalDeadLock.entity.Member;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByMemberAndBoard(Member member, Board board);

    List<Like> findByBoard(Board board);

}
