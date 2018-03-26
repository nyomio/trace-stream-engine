package flink.example.simpleclient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ParseMessageResult {

	private final ReportsAndExtData reportsAndExtData = new ReportsAndExtData();
	private String nativeId;
	private byte[] responseToTracker = null;
	private int protocolId;
	private int messageTypeId;
	private int sequenceNumber = 0;

	public ParseMessageResult() {
	}

	public ReportsAndExtData getReportsAndExtData() {
		return reportsAndExtData;
	}

	public String getNativeId() {
		return nativeId;
	}

	public void setNativeId(String nativeId) {
		this.nativeId = nativeId;
	}

	public byte[] getResponseToTracker() {
		return responseToTracker;
	}

	public void setResponseToTracker(byte[] responseToTracker) {
		this.responseToTracker = responseToTracker;
	}

	public int getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(int protocolId) {
		this.protocolId = protocolId;
	}

	public int getMessageTypeId() {
		return messageTypeId;
	}

	public void setMessageTypeId(int messageTypeId) {
		this.messageTypeId = messageTypeId;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
