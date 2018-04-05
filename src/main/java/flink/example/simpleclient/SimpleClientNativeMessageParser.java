package flink.example.simpleclient;

import java.util.ArrayList;
import java.util.List;
import nyomio.data.TrafficLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleClientNativeMessageParser {

  private SimpleClientMessageComposer messageComposer;

  @Autowired
  public SimpleClientNativeMessageParser(SimpleClientMessageComposer messageComposer) {
    super();
    this.messageComposer = messageComposer;
  }

  public ParseMessageResult parseNativeMessage(TrafficLog trafficLog)
      throws InvalidNativeMessageException {
    try {
      // sample message:
      // 0.1;1002;abcd;0;0000;r:0,126000000,47.9,19.915,10.0,20.0,100.0,-1;r:0,126000001,47.91,19.925,11.0,21.0,120.0,-1;e:126000001,Sos:FF;\n

      String strNativeMessage = new String(trafficLog.getData());

      String[] messageParts = strNativeMessage.split(";");

      // expect at least 5 parts
      if (messageParts.length < 5) // protocol version, deviceId,
      // messageType, messageSeqNr,
      // (report*), (extendedData*),
      // terminating character
      {
        throw new InvalidNativeMessageException(
            "Invalid message, not enough message parts: '" + strNativeMessage + "'.");
      }

      String protocolVersion = messageParts[0];
      String nativeIdStr = messageParts[1];
      String messageTypeStr = messageParts[2];
      String messageSeqNrStr = messageParts[3];
      int seqNum = 0;
      int msgType = 0;

      // protocol version check
      if (!protocolVersion.equals("0.2")) {
        throw new InvalidNativeMessageException(
            "Invalid message protocol version, client: '" + protocolVersion + "', server: 0.2");
      }
      // messageType, messageSeqNr: they must be int
      try {
        msgType = Integer.parseInt(messageTypeStr);
        seqNum = Integer.parseInt(messageSeqNrStr);
      } catch (Throwable e) {
        throw new InvalidNativeMessageException(
            "Invalid deviceId, messageType, or messageSeqNr; message: '" + strNativeMessage + "'.");
      }

      ParseMessageResult messageResult = new ParseMessageResult();
      messageResult.setNativeId(nativeIdStr);
      messageResult.setProtocolId(1);
      messageResult.setMessageTypeId(MessageTypes.getTypeByValue(msgType).ordinal());
      messageResult.setSequenceNumber(seqNum);
      switch (MessageTypes.getTypeByValue(msgType)) {
        case NORMAL_UPLOAD:
          List<Report> reports = new ArrayList<Report>();
          List<ExtendedData> extDatas = new ArrayList<ExtendedData>();
          // optional report and extendedData entries follow
          int i;
          for (i = 4; i < messageParts.length; i++) {
            String currentMessagePart = messageParts[i];
            if (currentMessagePart == null) {
              continue;
            }
            if (currentMessagePart.length() < 2)
            // throw new InvalidNativeMessageException("Invalid report or extendedData
            // entry: '"
            // + currentMessagePart + "', message: '" + strNativeMessage + "'.");
            {
              continue;
            }
            if (currentMessagePart.charAt(0) == 'r') {
              reports.add(messagePartToReport(currentMessagePart));
            } else if (currentMessagePart.charAt(0) == 'e') {
              extDatas.add(messagePartToExtendedData(currentMessagePart));
            } else {
              throw new InvalidNativeMessageException("Invalid report or extendedData entry: '"
                  + currentMessagePart + "', message: '" + strNativeMessage + "'.");
            }
          }

          // message end: messageParts[i] (terminating character)
          byte[] responseBytes = null;
          messageResult.getReportsAndExtData().getExtendedDataList().addAll(extDatas);
          messageResult.getReportsAndExtData().getReportList().addAll(reports);

          // get server response for normal upload
          String response = messageComposer.getNormalUploadSuccess(messageSeqNrStr, reports.size(),
              extDatas.size());

          responseBytes = response.getBytes();
          messageResult.setResponseToTracker(responseBytes);

          break;
        case PAIRING:
          break;
        case EXPIRATION_CHECK:
          break;
        case DATA:
        case CONFIG:
        default: {
          throw new InvalidNativeMessageException(
              "Invalid message type: '" + messageTypeStr + "'.");
        }
      }
      return messageResult;

    } catch (Throwable e) {
      if (e instanceof InvalidNativeMessageException) {
        throw (InvalidNativeMessageException) e;
      } else {
        throw new InvalidNativeMessageException("Message parsing failed.", e);
      }
    }
  }

  private Report messagePartToReport(String messagePart) throws InvalidNativeMessageException {
    /*
     * - "r:0,126000000,47.9,19.915,10.0,20.0,100.0,-1": the first report -- "r:":
     * report; split the remaining string by the ',' character: -- "0": GPS position
     * -- "126000000": timestamp -- "47.9": lat -- "19.915": lng -- "10.0": speed,
     * knots -- "20.0": heading, degrees -- "100.0": altitude, meters -- "-1":
     * accuracy
     */

    if (messagePart == null) {
      throw new InvalidNativeMessageException("Parameter messagePart may not be null.");
    }

    Report report = null;

    // skip the first two characters("r:"), split by ',', must get at least
    // 2 parts (positionSource, position data)
    String[] reportStrParts = messagePart.substring(2).split(",", 2);
    if (reportStrParts.length < 2) {
      throw new InvalidNativeMessageException("Invalid report: '" + messagePart + "'.");
    }

    // determine position source
    String posSrcStr = reportStrParts[0];
    int posSrc = Integer.parseInt(posSrcStr);

    // -1 may not be used, if value is -1, parse failed
    if (posSrc == -1) {
      throw new InvalidNativeMessageException(
          "Invalid position source, report: '" + messagePart + "'.");
    }

    // position data may not be null or empty
    if (reportStrParts[1] == null || reportStrParts[1].isEmpty()) {
      throw new InvalidNativeMessageException(
          "Invalid position data, report: '" + messagePart + "'.");
    }

    // posSrc determines the format used in the received parameter
    switch (posSrc) {
      case 0: // GPS position:
        // dateTime,lat,lng,speed,heading,altitude,accuracy
        String gpsReportStr = reportStrParts[1];
        // must contain exactly 7 parts
        String[] gpsReportStrParts = gpsReportStr.split(",");
        if (gpsReportStrParts.length != 7) {
          throw new InvalidNativeMessageException(
              "Invalid GPS position data, GPS report: '" + gpsReportStr + "'.");
        }

        String dateTimeStr = gpsReportStrParts[0];
        // double values below contain "." as decimal separator
        String latStr = gpsReportStrParts[1];
        String lngStr = gpsReportStrParts[2];
        String speedStr = gpsReportStrParts[3];
        String headingStr = gpsReportStrParts[4];
        String altitudeStr = gpsReportStrParts[5];
        String accuracyStr = gpsReportStrParts[6];

        try {
          long dateTime = parseDate(dateTimeStr);
          double lat = Double.valueOf(latStr);
          double lng = Double.valueOf(lngStr);
          double speed = Double.valueOf(speedStr);
          double heading = Double.valueOf(headingStr);
          double altitude = Double.valueOf(altitudeStr);
          double accuracy = Double.valueOf(accuracyStr);

          report = new Report();

          report.setDeviceId(-1l);
          report.setCreationTimestamp(dateTime);
          report.setLat(lat);
          report.setLng(lng);
          report.setSpeed(speed);
          report.setHeading(heading);
          report.setAlt(altitude);
          report.setAccuracy(accuracy);
        } catch (Throwable e) {
          throw new InvalidNativeMessageException(
              "Invalid GPS position data, GPS report: '" + gpsReportStr + "'.");
        }
        break;
      default:
        break;
    }

    return report;
  }

  private ExtendedData messagePartToExtendedData(String messagePart)
      throws InvalidNativeMessageException {
    /*
     * - "e:126000001,Sos:FF": the first extended data -- "e:": extended data; split
     * the remaining string by the ',' character: -- "126000000": timestamp --
     * "Sos:FF": the extended data, split by the ':' character: --- "Sos": data name
     * --- "FF": data value, hex
     */

    if (messagePart == null) {
      throw new InvalidNativeMessageException("Parameter messagePart may not be null.");
    }

    ExtendedData extData = null;

    // skip the first two characters("e:"), split by ',', must contain
    // exactly 2 parts (dateTime,dataName:dataValue)
    String[] extendedDataStrParts = messagePart.substring(2).split(",");
    if (extendedDataStrParts.length != 2) {
      throw new InvalidNativeMessageException("Invalid extended data: '" + messagePart + "'.");
    }

    // first part: dateTime
    String dateTimeStr = extendedDataStrParts[0];
    long dateTime = parseDate(dateTimeStr);

    // second part: dataName:dataValue
    String[] dataParts = extendedDataStrParts[1].split(":");
    if (dataParts.length == 0 || dataParts.length > 2) {
      throw new InvalidNativeMessageException(
          "Invalid extended data, data format invalid: '" + messagePart + "'.");
    }

    String dataName = dataParts[0];
    String dataValue = "";
    if (dataParts.length > 1) {
      dataValue = dataParts[1];
    }

    if (dataName == null || dataName.isEmpty()) {
      throw new InvalidNativeMessageException(
          "Invalid extended data, no data name provided: '" + messagePart + "'.");
    }

    // fill the values
    extData = new ExtendedData();

    extData.setDeviceId(-1l);
    extData.setCreationTimestamp(dateTime);
    extData.setDataName(dataName);
    extData.setDataValue(dataValue);

    return extData;
  }

  private long parseDate(String dateTimeStr) {
    if (dateTimeStr.startsWith("ms")) {
      return Long.valueOf(dateTimeStr.substring(2));
    } else {
      return Integer.parseInt(dateTimeStr) * 1000l;// datetime is
      // uploaded in
      // seconds since
      // 1970.01.01.
      // 00:00:00 GMT
    }
  }
}
