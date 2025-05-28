package com.asyn;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AsynApplicationTests {

	@Test
	void contextLoads() {
	}


	@Test
	@DisplayName("주문 정보를 조회하는 예시입니다.")
	void supplyAsync() throws Exception {
		String orderNo = "1234567890";
		CompletableFuture<String> orderInfoFuture = CompletableFuture.supplyAsync(() -> getOrderInfo(orderNo));

		assertEquals("iPhone 15", orderInfoFuture.get()); // CompletableFuture.get() 호출로 비동기 작업이 시작되고 2초 뒤 결과 반환
	}

	private String getOrderInfo(String orderNo) {
		try {
			Thread.sleep(2000); // orderInfoRepository.findByOrderNo(orderNo);
			System.out.println("2초후 실행됨");
		} catch (InterruptedException e) {
			// ..
		}
		return "iPhone 15";
	}

	@Test
	@DisplayName("스레드 2개로 조정하고  작업 4개 등록하기")
	void supplyAsyncCustomThread() throws ExecutionException, InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(2);

		// 🕐 오래 걸리는 작업 정의
		Supplier<String> slowTask = () -> {
			String threadName = Thread.currentThread().getName();
			System.out.println("[" + LocalTime.now() + "] " + threadName + " - 작업 시작");
			try {
				Thread.sleep(5000); // 일부러 5초 지연
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			System.out.println("[" + LocalTime.now() + "] " + threadName + " - 작업 완료");
			return "작업 완료";
		};


		// 📦 작업 4개 등록 → 스레드는 2개 뿐이므로 나머지 2개는 대기해야 함
		CompletableFuture<String> f1 = CompletableFuture.supplyAsync(slowTask, executor);//바로 실행 (5초)
		CompletableFuture<String> f2 = CompletableFuture.supplyAsync(slowTask, executor);//바로 실행(5초)
		CompletableFuture<String> f3 = CompletableFuture.supplyAsync(slowTask, executor);//위에서 1개 완료되면 스레드 할당받아서 실행(10초)
		CompletableFuture<String> f4 = CompletableFuture.supplyAsync(slowTask, executor);//위에서 작업 완료해서 스레드 비면 할당받아서 실행 (10초)

		// 💡 2개는 바로 실행, 나머지 2개는 5초 뒤에 실행 시작됨
		System.out.println("[" + LocalTime.now() + "] 메인 스레드 - get() 호출 시작");

		// 📦 get() 호출 → 각각 완료될 때까지 대기
		System.out.println("결과1: " + f1.get());
		System.out.println("결과2: " + f2.get());
		System.out.println("결과3: " + f3.get());
		System.out.println("결과4: " + f4.get());


		executor.shutdown();
	}

}
