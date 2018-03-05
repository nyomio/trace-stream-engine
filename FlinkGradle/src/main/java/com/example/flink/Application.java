package com.example.flink;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.flink.spring.AppConfig;

public class Application {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig.class);
		ctx.refresh();

		LocationStream locationStream = ctx.getBean(LocationStream.class);

		locationStream.precessStream();

	}

}
