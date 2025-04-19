package com.example.transactionalDeadLock.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.example.transactionalDeadLock.entity.Board;
import com.example.transactionalDeadLock.entity.Like;
import com.example.transactionalDeadLock.entity.Member;

import jakarta.persistence.LockModeType;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByMemberAndBoard(Member member, Board board);

    List<Like> findByBoard(Board board);

    // @Query("select count(*) from Like l where l.board.id = :boardId")
    // int getCountOfLikes(Long boardId);
}
