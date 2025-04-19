package com.example.transactionalDeadLock.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.transactionalDeadLock.entity.Board;
import com.example.transactionalDeadLock.entity.Member;
import com.example.transactionalDeadLock.repository.BoardRepository;
import com.example.transactionalDeadLock.repository.LikeRepository;
import com.example.transactionalDeadLock.repository.MemberRepository;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class BoardServiceTest {

	private static final int TOTAL_COUNT = 3;

	private Board board;
	private ExecutorService executorService;
	private CountDownLatch latch;
	private Member member;

	@Autowired
	BoardRepository boardRepository;

	@Autowired
	EntityManager em;

	@Autowired
	MemberRepository memberRepository;
	@Autowired
	BoardService boardService;
	@Autowired
	private LikeRepository likeRepository;

	@BeforeEach
	void setUp() {
		executorService = Executors.newFixedThreadPool(TOTAL_COUNT);
		latch = new CountDownLatch(TOTAL_COUNT);

		member = memberRepository.save(new Member("test@gmail.com", "username", "1234"));
		board = boardRepository.save(new Board("제목", "내용", member));
	}


	@Test
	@DisplayName("DeadLock")
	void deadlock() throws Exception {
		for (int i = 0; i < TOTAL_COUNT; i++) {

			executorService.submit(() -> {
				try {
					Member member = memberRepository.save(new Member("test@gmail.com", "username", "1234"));
					boardService.saveLike_jpqlDeadLock(board.getId(), member);
				} catch (Exception e) {
					log.error("Exception", e);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		assertThat(board.getLikes()).isNotEqualTo(TOTAL_COUNT);
		// assertThat(likeRepository.findByBoard(board).size()).isNotEqualTo(TOTAL_COUNT);
	}


	@Test
	@DisplayName("JPQL적용 + DeadLock")
	void jpqlAndDeadlock() throws Exception {
		for (int i = 0; i < TOTAL_COUNT; i++) {

			executorService.submit(() -> {
				try {
					Member member = memberRepository.save(new Member("test@gmail.com", "username", "1234"));
					boardService.saveLike_jpqlDeadLock(board.getId(), member);
				} catch (Exception e) {
					log.error("Exception", e);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		assertThat(board.getLikes()).isNotEqualTo(TOTAL_COUNT);
		// assertThat(likeRepository.findByBoard(board).size()).isNotEqualTo(TOTAL_COUNT);
	}

	@Test
	@DisplayName("board 좋아요 수 늘리는 순서 변경 + JPQL -> 레코드 락")
	void recordLock() throws Exception {
		int like;
		for (int i = 0; i < TOTAL_COUNT; i++) {

			executorService.submit(() -> {
				try {
					Member member = memberRepository.save(new Member("test@gmail.com", "username", "1234"));
					boardService.saveLike_jpql(board.getId(), member);
				} catch (Exception e) {
					log.error("Exception", e);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		assertThat(board.getLikes()).isNotEqualTo(TOTAL_COUNT);
		// assertThat(likeRepository.findByBoard(board).size()).isEqualTo(TOTAL_COUNT); //멀티 쓰레드에서 테스트해서 각 스레드마다 뷴리된 트랜잭션이 계속 롤백
	}


	@Test
	@DisplayName("JPQL + 낙관락 적용")
	void optimisticLock() throws Exception {
		for (int i = 0; i < TOTAL_COUNT; i++) {

			executorService.submit(() -> {
				try {
					Member member = memberRepository.save(new Member("test@gmail.com", "username", "1234"));
					boardService.saveLike_jpqlAndOptimisticLock(board.getId(), member);
				} catch (Exception e) {
					log.error("Exception", e);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		assertThat(board.getLikes()).isNotEqualTo(TOTAL_COUNT);
		// assertThat(likeRepository.findByBoard(board).size()).isEqualTo(TOTAL_COUNT);
	}

	@Test
	@DisplayName("JPQL + 누적합")
	void increment() throws Exception {
		for (int i = 0; i < TOTAL_COUNT; i++) {

			executorService.submit(() -> {
				try {
					Member member = memberRepository.save(new Member("test@gmail.com", "username", "1234"));
					boardService.saveLike_increment(board.getId(), member);
				} catch (Exception e) {
					log.error("Exception", e);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();


		//영속성 컨텍스트 비워주고 다시 조회
		//@BeforeEach 에서 이미 조회한 board를 캐시에서 가져와서 likes 수가 0
		em.clear();
		assertThat(boardRepository.findById(board.getId()).get().getLikes()).isEqualTo(TOTAL_COUNT);
		// assertThat(likeRepository.findByBoard(board).size()).isEqualTo(TOTAL_COUNT);
	}


	@Test
	@DisplayName("비관락")
	void pessimisticLok() throws Exception {
		for (int i = 0; i < TOTAL_COUNT; i++) {

			executorService.submit(() -> {
				try {
					Member member = memberRepository.save(new Member("test@gmail.com", "username", "1234"));
					boardService.saveLike_pessimisticLock(board.getId(), member);
				} catch (Exception e) {
					log.error("Exception", e);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();


		//영속성 컨텍스트 비워주고 다시 조회
		//@BeforeEach 에서 이미 조회한 board를 캐시에서 가져와서 likes 수가 0
		em.clear();
		assertThat(boardRepository.findById(board.getId()).get().getLikes()).isEqualTo(TOTAL_COUNT);
		// assertThat(likeRepository.findByBoard(board).size()).isEqualTo(TOTAL_COUNT);
	}

}