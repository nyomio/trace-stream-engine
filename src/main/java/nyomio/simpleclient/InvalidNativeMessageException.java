package nyomio.simpleclient;

public class InvalidNativeMessageException extends Exception {

  private static final long serialVersionUID = 7978879431279801678L;

  public InvalidNativeMessageException() {
    super();
  }

  public InvalidNativeMessageException(String message) {
    super(message);
  }

  public InvalidNativeMessageException(String message, Throwable cause) {
    super(message, cause);
  }
}
