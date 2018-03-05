package com.example.flink;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;
import com.metamx.common.Granularity;
import com.metamx.tranquility.beam.Beam;
import com.metamx.tranquility.beam.ClusteredBeamTuning;
import com.metamx.tranquility.config.DataSourceConfig;
import com.metamx.tranquility.config.PropertiesBasedConfig;
import com.metamx.tranquility.config.TranquilityConfig;
import com.metamx.tranquility.druid.DruidBeams;
import com.metamx.tranquility.druid.DruidDimensions;
import com.metamx.tranquility.druid.DruidLocation;
import com.metamx.tranquility.druid.DruidRollup;
import com.metamx.tranquility.flink.BeamFactory;
import com.metamx.tranquility.tranquilizer.Tranquilizer;
import com.metamx.tranquility.typeclass.Timestamper;

import io.druid.granularity.QueryGranularities;
import io.druid.query.aggregation.AggregatorFactory;
import io.druid.query.aggregation.CountAggregatorFactory;
import io.druid.query.aggregation.hyperloglog.HyperUniquesAggregatorFactory;

@Component
public class MyBeamFactory implements BeamFactory {

	final protected String datasource = "location";
	final protected String druidZkHosts = "127.0.0.1:2181";
	final protected String druidZkDiscoveryPath = "/druid/discovery";
	final protected String druidIndexServiceName = "druid/overlord";
	final protected String druidFirehosePattern = "firehose:%s";
	final protected int numReplicants = 1;
	final protected int numPartitions = 1;

	@Override
	public Beam makeBeam() {
		final List<String> dimensions = ImmutableList.of("dimValue");

		final List<AggregatorFactory> aggregators = ImmutableList.of(new CountAggregatorFactory("agg_count"),
				new HyperUniquesAggregatorFactory("agg_distinct_dimValue", "dimValue"));

		// Tranquility uses ZooKeeper (through Curator) for coordination.
		final CuratorFramework curator = CuratorFrameworkFactory.builder().connectString(this.druidZkHosts)
				.retryPolicy(new ExponentialBackoffRetry(1000, 20, 30000)).build();
		curator.start();

		final Timestamper<Map<String, Object>> timestamper = new Timestamper<Map<String, Object>>() {
			public DateTime timestamp(Map<String, Object> map) {
				return new DateTime(map.get("timestamp"));
			}
		};

		final Beam<Map<String, Object>> beam = DruidBeams.builder(timestamper).curator(curator)
				.discoveryPath(this.druidZkDiscoveryPath)
				.location(DruidLocation.create(this.druidIndexServiceName, this.druidFirehosePattern, this.datasource))
				.rollup(DruidRollup.create(DruidDimensions.specific(dimensions), aggregators,
						QueryGranularities.MINUTE))
				.tuning(ClusteredBeamTuning.builder().segmentGranularity(Granularity.DAY)
						.windowPeriod(new Period("PT30M")).partitions(this.numPartitions).replicants(this.numReplicants)
						.build())
				.buildBeam();

		return beam;
	}

	@Override
	public Tranquilizer tranquilizer() {
		InputStream configStream = null;

		try {
			configStream = new FileInputStream("druidconfig.json");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TranquilityConfig<PropertiesBasedConfig> config = TranquilityConfig.read(configStream);
		DataSourceConfig<PropertiesBasedConfig> locationConfig = config.getDataSource(datasource);
		Tranquilizer<Map<String, Object>> sender = DruidBeams.fromConfig(locationConfig)
				.buildTranquilizer(locationConfig.tranquilizerBuilder());
		// TODO Auto-generated method stub
		return sender;
	}

}
