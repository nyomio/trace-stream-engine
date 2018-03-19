package flink.example.cassandra;

import static flink.example.cassandra.CassandraConst.KEYSPACE_NAME;
import static flink.example.cassandra.CassandraConst.TABLE_NAME;

import org.springframework.stereotype.Component;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;

@Component
public class CassandraConnector {

	private Cluster cluster;

	private Session session;

	public void connect(String node, Integer port) {

		Builder b = Cluster.builder().addContactPoint(node);
		if (port != null) {
			b.withPort(port);
		}
		cluster = b.build();

		session = cluster.connect();
		createKeyspace("SimpleStrategy", 1);
		createTable();
	}

	public Session getSession() {
		return this.session;
	}

	public void close() {
		session.close();
		cluster.close();
	}

	public void createKeyspace(String replicationStrategy, int replicationFactor) {
		StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ").append(KEYSPACE_NAME)
				.append(" WITH replication = {").append("'class':'").append(replicationStrategy)
				.append("','replication_factor':").append(replicationFactor).append("};");

		String query = sb.toString();
		session.execute(query);
	}

	public void createTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append("(")
				.append(CassandraConst.ID).append(" uuid PRIMARY KEY,").append(CassandraConst.TIME)
				.append(" timestamp,").append(CassandraConst.IP).append(" text,").append(CassandraConst.MESSAGE)
				.append(" text);");

		String query = sb.toString();
		session.execute(query);
	}
}
