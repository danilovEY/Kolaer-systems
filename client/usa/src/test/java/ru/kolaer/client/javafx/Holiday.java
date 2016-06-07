package ru.kolaer.client.javafx;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Holiday {

	public static void main(String[] args) throws IOException, InterruptedException {
		ExecutorService service = Executors.newSingleThreadExecutor();
		ExecutorService service1 = Executors.newSingleThreadExecutor();
		ExecutorService service2 = Executors.newSingleThreadExecutor();
		ExecutorService service3 = Executors.newSingleThreadExecutor();
		
		CompletableFuture.runAsync(() -> {
			while(true)
				System.out.println("AAA");
		}, service);
		
		CompletableFuture.runAsync(() -> {
			while(true)
				System.out.println("BBB");
		}, service2);
		CompletableFuture.runAsync(() -> {
			while(true)
				System.out.println("CCC");
		}, service3);
		CompletableFuture.runAsync(() -> {
			while(true)
				System.out.println("DDD");
		}, service1);
		service.shutdown();
		service2.shutdown();
		service3.shutdown();
		service1.shutdown();
		TimeUnit.SECONDS.sleep(10);
	}

}
