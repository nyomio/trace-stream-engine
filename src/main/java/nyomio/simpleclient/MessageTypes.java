package nyomio.simpleclient;

public enum MessageTypes {
  PAIRING(0), NORMAL_UPLOAD(1), CONFIG(2), DATA(3), EXPIRATION_CHECK(4);

  private int value;

  private MessageTypes(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static MessageTypes getTypeByValue(int value) {
    for (MessageTypes type : MessageTypes.values()) {
      if (type.getValue() == value) {
        return type;
      }
    }
    return null;
  }
}
