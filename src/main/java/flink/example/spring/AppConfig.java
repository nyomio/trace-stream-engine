package flink.example.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import flink.example.cassandra.CassandraConnector;
import flink.example.cassandra.CassandraOperations;

@Configuration
@ComponentScan("flink.example")
public class AppConfig {

	@Bean
	@Lazy
	public CassandraOperations cassandraOperations(CassandraConnector connector) {
		return new CassandraOperations(connector);
	}

}
