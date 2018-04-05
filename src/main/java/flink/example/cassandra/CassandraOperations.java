package flink.example.cassandra;

import static flink.example.cassandra.CassandraConst.RECEIVETIMESTAMP;
import static flink.example.cassandra.CassandraConst.IP;
import static flink.example.cassandra.CassandraConst.KEYSPACE_NAME;
import static flink.example.cassandra.CassandraConst.KNOT;
import static flink.example.cassandra.CassandraConst.LAT;
import static flink.example.cassandra.CassandraConst.LNG;
import static flink.example.cassandra.CassandraConst.LOG_TABLE_NAME;
import static flink.example.cassandra.CassandraConst.DATA;
import static flink.example.cassandra.CassandraConst.REPORT_TABLE_NAME;

import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;
import flink.example.simpleclient.ParseMessageResult;
import flink.example.simpleclient.Report;
import nyomio.data.TrafficLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CassandraOperations {

  private CassandraConnector connector;

  @Autowired
  public CassandraOperations(CassandraConnector connector) {
    this.connector = connector;
  }

  public void insertLog(TrafficLog value) {
    Insert query = QueryBuilder.insertInto(KEYSPACE_NAME, LOG_TABLE_NAME)
        .value(RECEIVETIMESTAMP, UUIDs.startOf(value.getReceiveTimestamp())).value(IP, value.getSourceIp())
        .value(DATA, value.getData());
    System.out.println(query.toString());
    connector.getSession().execute(query);
  }

  public void insertReport(ParseMessageResult value) {
    for (Report report : value.getReportsAndExtData().getReportList()) {
      Insert query = QueryBuilder.insertInto(KEYSPACE_NAME, REPORT_TABLE_NAME)
          .value(RECEIVETIMESTAMP, UUIDs.startOf(report.getCreationTimestamp())).value(LAT, report.getLat())
          .value(LNG, report.getLng()).value(KNOT, report.getSpeed());
      System.out.println(query.toString());
      connector.getSession().execute(query);
    }
  }
}
