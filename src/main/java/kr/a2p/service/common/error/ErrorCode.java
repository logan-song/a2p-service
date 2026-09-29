package kr.a2p.service.common.error;

public enum ErrorCode {
  // 공통
  BAD_REQUEST("");

  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }

  public String code() {
    return name();
  }

  public String message() {
    return message;
  }
}
