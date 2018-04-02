package flink.example.cassandra;

import static flink.example.cassandra.CassandraConst.ID;
import static flink.example.cassandra.CassandraConst.IP;
import static flink.example.cassandra.CassandraConst.KEYSPACE_NAME;
import static flink.example.cassandra.CassandraConst.KNOT;
import static flink.example.cassandra.CassandraConst.LAT;
import static flink.example.cassandra.CassandraConst.LNG;
import static flink.example.cassandra.CassandraConst.LOG_TABLE_NAME;
import static flink.example.cassandra.CassandraConst.MESSAGE;
import static flink.example.cassandra.CassandraConst.REPORT_TABLE_NAME;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;

import flink.example.Location;
import flink.example.simpleclient.ParseMessageResult;
import flink.example.simpleclient.Report;

@Component
public class CassandraOperations {

	private CassandraConnector connector;

	@Autowired
	public CassandraOperations(CassandraConnector connector) {
		this.connector = connector;
	}

	public void insertLog(Location value) {
		Insert query = QueryBuilder.insertInto(KEYSPACE_NAME, LOG_TABLE_NAME)
				.value(ID, UUIDs.startOf(value.getTimestamp())).value(IP, value.getIp())
				.value(MESSAGE, value.getMessage());
		System.out.println(query.toString());
		connector.getSession().execute(query);
	}

	public void insertReport(ParseMessageResult value) {
		for (Report report : value.getReportsAndExtData().getReportList()) {
			Insert query = QueryBuilder.insertInto(KEYSPACE_NAME, REPORT_TABLE_NAME)
					.value(ID, UUIDs.startOf(report.getCreationTimestamp())).value(LAT, report.getLat())
					.value(LNG, report.getLng()).value(KNOT, report.getSpeed());
			System.out.println(query.toString());
			connector.getSession().execute(query);
		}
	}
}
