package flink.example;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Location {

	private Long timestamp;
	private String message;
	private String ip;

	public Location(Long timestamp, String message, String ip) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.ip = ip;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getIp() {
		return ip;
	}
}
