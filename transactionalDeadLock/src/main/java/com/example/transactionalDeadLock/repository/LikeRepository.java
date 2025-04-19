package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Board;
import com.example.entity.Like;
import com.example.entity.Member;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByMemberAndBoard(Member member, Board board);
}
