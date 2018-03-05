package com.example.flink;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Location {

	private Long timestamp;
	private Double lat;
	private Double lng;
	private String dimValue = "dim" + timestamp;
	private int numValue;

	public Location(Long timestamp, Double lat, Double lng) {
		super();
		this.timestamp = timestamp;
		this.lat = lat;
		this.lng = lng;
		this.numValue = lat.intValue();
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getDimValue() {
		return dimValue;
	}

	public void setDimValue(String dimValue) {
		this.dimValue = dimValue;
	}

	public int getNumValue() {
		return numValue;
	}

	public void setNumValue(int numValue) {
		this.numValue = numValue;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
