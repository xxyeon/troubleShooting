package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.repository.*;
import com.example.entity.*;

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
    public void saveLike(Long id, Member member) {
        //이미 좋아요가 반영되었다면 예외
        Board findBoard = getBoardById(id);

        likeRepository.findByMemberAndBoard(member, findBoard)
            .orElseThrow(NullPointerException::new);

        Like like = Like.builder()
                .board(findBoard)
                .member(member).build();

        likeRepository.save(like);
        int likeCnt = findBoard.getLikes();
        findBoard.updateLike(++likeCnt);
    }

    @Transactional
    public void deleteLike(Long id, Member member) {
        Board findBoard = getBoardById(id);
        try {
            Like findLike = likeRepository.findByMemberAndBoard(member, findBoard)
                .orElseThrow(NullPointerException::new);
            likeRepository.delete(findLike);
            int likeCnt = findBoard.getLikes();
            findBoard.updateLike(--likeCnt);
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }
    }

    public Board getBoardById(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new NullPointerException());
    }

}