package nyomio.cassandra;

import static nyomio.cassandra.CassandraConst.DATA;
import static nyomio.cassandra.CassandraConst.IP;
import static nyomio.cassandra.CassandraConst.KEYSPACE_NAME;
import static nyomio.cassandra.CassandraConst.KNOT;
import static nyomio.cassandra.CassandraConst.LAT;
import static nyomio.cassandra.CassandraConst.LNG;
import static nyomio.cassandra.CassandraConst.LOG_TABLE_NAME;
import static nyomio.cassandra.CassandraConst.RECEIVETIMESTAMP;
import static nyomio.cassandra.CassandraConst.REPORT_TABLE_NAME;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CassandraConnector {

  private static final Logger logger = LoggerFactory.getLogger(CassandraConnector.class);

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
    logger.debug(query);
    session.execute(query);
  }

  private void createLogTable() {
    String query =
        "CREATE TABLE IF NOT EXISTS " + KEYSPACE_NAME + "." + LOG_TABLE_NAME + "("
            + RECEIVETIMESTAMP + " timeuuid PRIMARY KEY,"
            + IP + " text,"
            + DATA + " blob);";
    logger.debug(query);
    session.execute(query);
  }

  private void createReportTable() {
    String query =
        "CREATE TABLE IF NOT EXISTS " + KEYSPACE_NAME + "." + REPORT_TABLE_NAME + "("
            + RECEIVETIMESTAMP + " timeuuid PRIMARY KEY,"
            + LAT + " double,"
            + LNG + " double,"
            + KNOT + " double);";
    logger.debug(query);
    session.execute(query);
  }
}
