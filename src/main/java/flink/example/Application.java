package flink.example;

import flink.example.cassandra.CassandraConnector;
import flink.example.spring.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
    connector.connect("localhost", 9042);
  }

}
