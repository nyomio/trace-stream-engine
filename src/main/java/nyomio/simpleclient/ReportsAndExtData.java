package nyomio.simpleclient;

import java.util.ArrayList;
import java.util.List;

public class ReportsAndExtData {

  private Long deviceId;
  private List<Report> reportList = new ArrayList<>();
  private List<ExtendedData> extendedDataList = new ArrayList<>();

  /**
   * @return The report(s) contained in the message.
   */
  public List<Report> getReportList() {
    return reportList;
  }

  public void addReport(Report report) {
    reportList.add(report);
  }

  /**
   * @return The extended nyomio.data(s) contained in the message.
   */
  public List<ExtendedData> getExtendedDataList() {
    return extendedDataList;
  }

  public void addExtendedData(ExtendedData extData) {
    extendedDataList.add(extData);
  }

  public Long getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(Long deviceId) {
    this.deviceId = deviceId;
  }

  @Override
  public String toString() {
    return "ReportsAndExtData [deviceId=" + deviceId + ", reportList=" + reportList
        + ", extendedDataList="
        + extendedDataList + "]";
  }

  public boolean hasAnyExtendedData() {
    return !extendedDataList.isEmpty();
  }

  public boolean hasAnyReport() {
    return !reportList.isEmpty();
  }
}
