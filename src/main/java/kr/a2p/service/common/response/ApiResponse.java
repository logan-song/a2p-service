package kr.a2p.service.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApiResponse<T> {
  private final int status;
  private final String message;
  private final T data;
  private final T error;
  private final LocalDateTime timestamp;

  public static <T> ApiResponse<T> success(T data) {
    return ApiResponse.<T>builder()
        .status(HttpStatus.OK.value())
        .data(data)
        .timestamp(LocalDateTime.now())
        .build();
  }

  public static <T> ApiResponse<T> error(int status, T data) {
    return ApiResponse.<T>builder()
        .status(status)
        .error(data)
        .timestamp(LocalDateTime.now())
        .build();
  }
}
