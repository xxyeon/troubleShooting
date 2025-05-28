package com.asyn;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CompletableFuture;

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
}
