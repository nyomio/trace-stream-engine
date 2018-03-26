package flink.example.simpleclient;

public class ExtendedData {

	private long deviceId;
	private long creationTimestamp;
	private long storageTimestamp;
	private String dataName;
	private String dataValue;

	public ExtendedData() {
	}

	public ExtendedData(long creationTimestamp, String dataName, String dataValue) {
		this.creationTimestamp = creationTimestamp;
		this.dataName = dataName;
		this.dataValue = dataValue;
	}

	public ExtendedData(String dataName, String dataValue) {
		this.dataName = dataName;
		this.dataValue = dataValue;
	}

	public ExtendedData(String dataName) {
		super();
		this.dataName = dataName;
	}

	public Long getTrackerId() {
		return deviceId;
	}

	public long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}

	public long getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(long creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public long getStorageTimestamp() {
		return storageTimestamp;
	}

	public void setStorageTimestamp(long storageTimestamp) {
		this.storageTimestamp = storageTimestamp;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public String getDataValue() {
		return dataValue;
	}

	@Override
	public String toString() {
		return "Ext:[dev:" + deviceId + ";time:" + creationTimestamp + ";stTime:" + storageTimestamp + ";" + dataName
				+ "=" + dataValue + "]";
	}

	public String getName() {
		return this.dataName;
	}

	public String getValueAsString() {
		return this.dataValue;
	}
}
