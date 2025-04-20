package com.example.transactionalDeadLock.service;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transactionalDeadLock.entity.Board;
import com.example.transactionalDeadLock.entity.Like;
import com.example.transactionalDeadLock.entity.Member;
import com.example.transactionalDeadLock.repository.BoardRepository;
import com.example.transactionalDeadLock.repository.LikeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public void saveLike_DeadLock(Long id, Member member) {
        Board findBoard = getBoardById(id);

        Like like = likeRepository.findByMemberAndBoard(member, findBoard)
            .orElse(Like.builder()
                .board(findBoard)
                .member(member).build());
        likeRepository.save(like);
        findBoard.updateLike();
    }

    @Transactional
    public void saveLike_jpqlDeadLock(Long id, Member member) {
        Board findBoard = getBoardById(id);

        Like like = likeRepository.findByMemberAndBoard(member, findBoard)
            .orElse(Like.builder()
                .board(findBoard)
                .member(member).build());
        likeRepository.save(like);
        int likeCnt = findBoard.getLikes() + 1;
        boardRepository.updateCountOfLike(likeCnt, findBoard.getId());
    }

    @Transactional
    public void saveLike_optimisticLock(Long id, Member member) {
        Board findBoard = getBoardById(id);

        Like like = likeRepository.findByMemberAndBoard(member, findBoard)
            .orElse(Like.builder()
                .board(findBoard)
                .member(member).build());
        findBoard.updateLike();
        likeRepository.save(like);
    }


    @Transactional
    public void saveLike_jpql(Long id, Member member) {

        Board findBoard = getBoardById(id); //이건 단순 호출이므로 lock이 안걸림

        Like like = likeRepository.findByMemberAndBoard(member, findBoard)
            .orElse(Like.builder()
                .board(findBoard)
                .member(member).build());

        int likeCnt = findBoard.getLikes() + 1;
        boardRepository.updateCountOfLike(likeCnt, findBoard.getId()); //like 저장하기 전에 board 좋아요 수 늘리기
        likeRepository.save(like);
    }

    @Transactional
    public void saveLike_jpqlAndOptimisticLock(Long id, Member member) {

        Board findBoard = boardRepository.getReferenceById(id);

        int likeCnt = findBoard.getLikes() + 1;

        int updatedCount = boardRepository.updateLikesWithOptimisticLock(likeCnt, findBoard.getId(), findBoard.getVersion());

        if (updatedCount == 0) {
            throw new OptimisticLockingFailureException("Board version mismatch");
        }
        Like like = likeRepository.findByMemberAndBoard(member, findBoard)
            .orElse(Like.builder()
                .board(findBoard)
                .member(member).build());
        likeRepository.save(like);
    }

    @Transactional
    public void saveLike_increment(Long id, Member member) {
        //이미 좋아요가 반영되었다면 예외
        Board findBoard = getBoardById(id);

        Like like = likeRepository.findByMemberAndBoard(member, findBoard)
            .orElse(Like.builder()
                .board(findBoard)
                .member(member).build());


        boardRepository.incrementLike(findBoard.getId());

        likeRepository.save(like);

    }

    @Transactional
    public void saveLike_pessimisticLock(Long id, Member member) {
        //이미 좋아요가 반영되었다면 예외
        Board findBoard = boardRepository.findByIdForUpdate(id)
            .orElseThrow(() -> new NullPointerException());

        Like like = likeRepository.findByMemberAndBoard(member, findBoard)
            .orElse(Like.builder()
                .board(findBoard)
                .member(member).build());


        findBoard.updateLike();

        likeRepository.save(like);

    }



    public Board getBoardById(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new NullPointerException());
    }

}