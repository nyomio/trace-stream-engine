package flink.example.simpleclient;

public class Report {

  private long deviceId;
  private long creationTimestamp;
  private long storageTimestamp;

  private double lat;
  private double lng;
  private double alt;
  private double heading;
  private double speed;

  private double accuracy;

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

  public void setStorageTimestamp(long storageTimestamp) {
    this.storageTimestamp = storageTimestamp;
  }

  public long getStorageTimestamp() {
    return storageTimestamp;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLng() {
    return lng;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }

  public double getAlt() {
    return alt;
  }

  public void setAlt(double alt) {
    this.alt = alt;
  }

  public double getHeading() {
    return heading;
  }

  public void setHeading(double heading) {
    this.heading = heading;
  }

  public double getSpeed() {
    return speed;
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }

  public double getAccuracy() {
    return accuracy;
  }

  public void setAccuracy(double accuracy) {
    this.accuracy = accuracy;
  }

  @Override
  public String toString() {
    return "Report [deviceId=" + deviceId + ", creationTimestamp=" + creationTimestamp
        + ", storageTimestamp="
        + storageTimestamp + ", lat=" + lat + ", lng=" + lng + ", alt=" + alt + ", heading="
        + heading
        + ", speed=" + speed + ", accuracy=" + accuracy + "]";
  }

}