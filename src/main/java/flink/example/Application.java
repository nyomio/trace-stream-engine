package flink.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import flink.example.cassandra.CassandraConnector;
import flink.example.spring.AppConfig;

public class Application {

	public static AnnotationConfigApplicationContext ctx;

	public static void main(String[] args) {
		ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig.class);
		ctx.refresh();

		initCassandra();

		LocationStream locationStream = ctx.getBean(LocationStream.class);

		locationStream.precessStream();

	}

	private static void initCassandra() {
		CassandraConnector connector = ctx.getBean(CassandraConnector.class);
		connector.connect("127.0.0.1", 9042);
	}

}
