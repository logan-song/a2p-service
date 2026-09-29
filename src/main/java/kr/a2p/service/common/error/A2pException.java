package kr.a2p.service.common.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class A2pException extends RuntimeException {
  private final ErrorCode errorCode;
  private final HttpStatus httpStatus;
  private final String errorMessage;
  private final String debugMessage;
  private final Throwable cause;

  @Builder
  private A2pException(Throwable cause, ErrorCode errorCode, HttpStatus httpStatus, String errorMessage, String debugMessage) {
    super(errorMessage, cause);
    this.errorCode = errorCode;
    this.httpStatus = httpStatus;
    this.errorMessage = errorMessage;
    this.debugMessage = debugMessage;
    this.cause = cause;
  }
}
