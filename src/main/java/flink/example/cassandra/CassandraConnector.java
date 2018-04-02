package flink.example.cassandra;

import static flink.example.cassandra.CassandraConst.KEYSPACE_NAME;
import static flink.example.cassandra.CassandraConst.LOG_TABLE_NAME;

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
		createLogTable();
		createReportTable();
	}

	public Session getSession() {
		return this.session;
	}

	public void close() {
		session.close();
		cluster.close();
	}

	private void createKeyspace(String replicationStrategy, int replicationFactor) {
		StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ").append(KEYSPACE_NAME)
				.append(" WITH replication = {").append("'class':'").append(replicationStrategy)
				.append("','replication_factor':").append(replicationFactor).append("};");

		String query = sb.toString();
		session.execute(query);
	}

	private void createLogTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(CassandraConst.KEYSPACE_NAME)
				.append(".").append(LOG_TABLE_NAME).append("(").append(CassandraConst.ID).append(" uuid PRIMARY KEY,")
				.append(CassandraConst.IP).append(" text,").append(CassandraConst.MESSAGE).append(" text);");

		String query = sb.toString();
		session.execute(query);
	}

	private void createReportTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(CassandraConst.KEYSPACE_NAME)
				.append(".").append(CassandraConst.REPORT_TABLE_NAME).append("(").append(CassandraConst.ID)
				.append(" uuid PRIMARY KEY,").append(CassandraConst.LAT).append(" double,").append(CassandraConst.LNG)
				.append(" double,").append(CassandraConst.KNOT).append(" double);");

		String query = sb.toString();
		session.execute(query);
	}
}
