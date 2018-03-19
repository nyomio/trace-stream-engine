package flink.example.cassandra;

import org.springframework.beans.factory.annotation.Autowired;

import com.datastax.driver.core.utils.UUIDs;

import flink.example.Location;

public class CassandraOperations {

	private CassandraConnector connector;

	@Autowired
	public CassandraOperations(CassandraConnector connector) {
		this.connector = connector;
	}

	public void insert(Location value) {
		StringBuilder sb = new StringBuilder("INSERT INTO ").append(CassandraConst.TABLE_NAME)
				.append("(id, time, ip, message) ").append("VALUES (").append(UUIDs.timeBased()).append(", ")
				.append(value.getTimestamp()).append(", '").append(value.getIp()).append("', '")
				.append(value.getMessage()).append("');");

		String query = sb.toString();
		System.out.println(query);
		connector.getSession().execute(query);
	}
}
