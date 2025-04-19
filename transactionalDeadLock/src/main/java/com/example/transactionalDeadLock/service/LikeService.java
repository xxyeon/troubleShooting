package com.example.transactionalDeadLock.service;

import org.springframework.stereotype.Service;

import com.example.transactionalDeadLock.repository.LikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

	private final LikeRepository likeRepository;

	// public int getLikes(Long boardId) { //트랜잭션 사용하지 않고 slock만 거는 경우,
	// 	return likeRepository.getCountOfLikes(boardId); //쿼리 한번 실행 -> 트랜잭션 필요 없음 (논리적 단위가 필요 없다는 말
	// }
}
