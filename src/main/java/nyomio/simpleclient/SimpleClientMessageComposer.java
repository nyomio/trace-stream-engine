package nyomio.simpleclient;

import org.springframework.stereotype.Component;

@Component
public class SimpleClientMessageComposer {

  public static final String PROTOCOL_VERSION = "0.3";

  public String getResponseString(String protocolVersion, int serverMessageType, String seqNum,
      boolean isSuccess,
      String serverMessage) {
    String successStr = "0";
    if (isSuccess) {
      successStr = "1";
    }
    if (serverMessage == null) {
      serverMessage = "";
    }
    return protocolVersion + ";" + serverMessageType + ";" + seqNum + ";" + successStr + ";"
        + serverMessage
        + ";\n";
  }

  public String getNormalUploadSuccess(String clientMessageSeqNrStr, int processedReportCount,
      int processedExtendedDataCount) {
    // response to a normal client nyomio.data upload
    // 0.1;1;0000;1;r:2;e:1;\n
    return getResponseString(PROTOCOL_VERSION, MessageTypes.NORMAL_UPLOAD.ordinal(),
        clientMessageSeqNrStr, true,
        "r:" + processedReportCount + ";e:" + processedExtendedDataCount + ";");
  }

  public byte[] getNormalUploadExpirationFailure(String clientMessageSeqNrStr) {
    // response to an expired normal client nyomio.data upload
    // 0.1;1;0000;0;1\n
    return getResponseString(PROTOCOL_VERSION, MessageTypes.NORMAL_UPLOAD.ordinal(),
        clientMessageSeqNrStr, false,
        NormalUploadFailureType.EXPIRED.ordinal() + "").getBytes();
  }

  public byte[] getNormalUploadOverLimitFailure(String clientMessageSeqNrStr) {
    // response to an expired normal client nyomio.data upload
    // 0.1;1;0000;0;1\n
    return getResponseString(PROTOCOL_VERSION, MessageTypes.NORMAL_UPLOAD.ordinal(),
        clientMessageSeqNrStr, false,
        NormalUploadFailureType.OVER_LIMIT.ordinal() + "").getBytes();
  }

  public byte[] getNormalUploadFailure(String clientMessageSeqNrStr) {
    // response to a general failed normal client nyomio.data upload
    // 0.1;1;0000;0;0\n
    return getResponseString(PROTOCOL_VERSION, MessageTypes.NORMAL_UPLOAD.ordinal(),
        clientMessageSeqNrStr, false,
        NormalUploadFailureType.GENERAL.ordinal() + "").getBytes();
  }

  public byte[] getResponsePairSucceed(String clientMessageSeqNrStr, boolean isSuccess,
      String nativeId) {
    return getResponseString(PROTOCOL_VERSION, MessageTypes.PAIRING.ordinal(),
        clientMessageSeqNrStr, isSuccess,
        nativeId).getBytes();
  }

  public byte[] getExpirationCheckAnswer(boolean isExpired, int clientMessageSeqNrStr) {
    // 0.1;4;0000;1;0;\n
    String expiredString = "0";
    if (isExpired) {
      expiredString = "1";
    }
    return getResponseString(PROTOCOL_VERSION, MessageTypes.EXPIRATION_CHECK.ordinal(),
        clientMessageSeqNrStr + "",
        true, expiredString).getBytes();
  }

}
