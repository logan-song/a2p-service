package kr.a2p.service.common.response.advise;

import kr.a2p.service.common.response.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice(basePackages = "kr.a2p.bid")
public class SuccessControllerAdvice implements ResponseBodyAdvice<Object> {
  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    // IgnoreApiResponse 제외 모든 응답에 적용
    return true;
  }

  @Override
  public Object beforeBodyWrite(Object body,
                                MethodParameter returnType,
                                MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request,
                                ServerHttpResponse response) {

    // 이미 ApiResponse면 그대로
    if (body instanceof ApiResponse<?>) {
      return body;
    }

    // 모든 나머지는 성공 응답으로 감싸기
    return ApiResponse.success(body);
  }
}
