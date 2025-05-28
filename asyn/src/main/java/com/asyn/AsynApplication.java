package com.asyn;

import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AsynApplication {

	public static void main(String[] args) throws InterruptedException{
		SpringApplication.run(AsynApplication.class, args);
		ExecutorService executor;
		executor = Executors.newFixedThreadPool(2);

		Supplier<String> slowTask = () -> {
			String threadName = Thread.currentThread().getName();
			System.out.println("[" + LocalTime.now() + "] " + threadName + " - 작업 시작");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			System.out.println("[" + LocalTime.now() + "] " + threadName + " - 작업 완료");
			return "완료";
		};

		// 비동기 작업 등록
		CompletableFuture.supplyAsync(slowTask, executor);
		CompletableFuture.supplyAsync(slowTask, executor);
		CompletableFuture.supplyAsync(slowTask, executor);

		// 비동기 로그가 찍히도록 충분히 대기
		Thread.sleep(7000);

		System.out.println("[" + LocalTime.now() + "] 메인 종료");
		executor.shutdown();
	}

}

