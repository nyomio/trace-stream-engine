package flink.example.cassandra;

import static flink.example.cassandra.CassandraConst.DATA;
import static flink.example.cassandra.CassandraConst.IP;
import static flink.example.cassandra.CassandraConst.KEYSPACE_NAME;
import static flink.example.cassandra.CassandraConst.KNOT;
import static flink.example.cassandra.CassandraConst.LAT;
import static flink.example.cassandra.CassandraConst.LNG;
import static flink.example.cassandra.CassandraConst.LOG_TABLE_NAME;
import static flink.example.cassandra.CassandraConst.RECEIVETIMESTAMP;
import static flink.example.cassandra.CassandraConst.REPORT_TABLE_NAME;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;
import org.springframework.stereotype.Component;

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
    String query = "CREATE KEYSPACE IF NOT EXISTS " + KEYSPACE_NAME
        + " WITH replication = {" + "'class':'" + replicationStrategy
        + "','replication_factor':" + replicationFactor + "};";
    session.execute(query);
  }

  private void createLogTable() {
    String query =
        "CREATE TABLE IF NOT EXISTS " + KEYSPACE_NAME + "." + LOG_TABLE_NAME + "("
            + RECEIVETIMESTAMP + " timeuuid PRIMARY KEY,"
            + IP + " text,"
            + DATA + " blob);";
    session.execute(query);
  }

  private void createReportTable() {
    String query =
        "CREATE TABLE IF NOT EXISTS " + KEYSPACE_NAME + "." + REPORT_TABLE_NAME + "("
            + RECEIVETIMESTAMP + " timeuuid PRIMARY KEY,"
            + LAT + " double,"
            + LNG + " double,"
            + KNOT + " double);";
    session.execute(query);
  }
}
