package kr.a2p.service.common.advice;

import jakarta.servlet.http.HttpServletRequest;
import kr.a2p.service.common.error.A2pException;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ViewExceptionHandler {
  @ExceptionHandler(A2pException.class)
  public Object handleException(A2pException e, HttpServletRequest request, Model model) {
    String uri = request.getRequestURI();
    if (uri.startsWith("/api")) {
      return ResponseEntity.status(500).body("API ERROR");
    }

    model.addAttribute("code", e.getErrorCode());
    model.addAttribute("message", e.getMessage());

    return "common/error";
  }

  @ExceptionHandler(Exception.class)
  public Object handleException(Exception e, HttpServletRequest request, Model model) {
    String uri = request.getRequestURI();
    if (uri.startsWith("/api")) {
      return ResponseEntity.status(500).body("API ERROR");
    }

    model.addAttribute("message", e.getMessage());

    return "common/error";
  }
}
