package flink.example;

import flink.example.spring.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig.class);
		ctx.refresh();

		LocationStream locationStream = ctx.getBean(LocationStream.class);

		locationStream.precessStream();

	}

}
