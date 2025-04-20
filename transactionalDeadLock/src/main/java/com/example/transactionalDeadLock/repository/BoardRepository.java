package com.example.transactionalDeadLock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.transactionalDeadLock.entity.Board;

import jakarta.persistence.LockModeType;

public interface BoardRepository extends JpaRepository<Board, Long> {

	@Modifying
	@Query("update Board b set b.likes = :count where b.id = :boardId")
	void updateCountOfLike(@Param("count") int count, @Param("boardId") Long boardId);

	@Modifying
	@Query("update Board b set b.likes = :count, b.version = :version + 1 where b.id = :boardId and b.version = :version")
	int updateLikesWithOptimisticLock(@Param("count") int count, @Param("boardId") Long boardId, @Param("version") int version);

	@Modifying
	@Query("update Board b set b.likes = b.likes + 1 where b.id = :id")
	void incrementLike(@Param("id") Long id);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select b from Board b where b.id = :id")
	Optional<Board> findByIdForUpdate(@Param("id") Long id);

}