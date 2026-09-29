package kr.a2p.service.common.error;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class A2pExceptionFactory {
  public static A2pException of(HttpStatus status, String errorMessage, String debugMessage) {
    return A2pException.builder()
        .httpStatus(status)
        .errorMessage(errorMessage)
        .debugMessage(debugMessage != null ? debugMessage : errorMessage)
        .build();
  }
}
