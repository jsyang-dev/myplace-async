package info.myplace.api.place.exception;

import org.springframework.http.HttpStatus;

public class WebClientFailException extends GlobalException {
  public WebClientFailException(HttpStatus status, String path) {
    super(status, "WebClient 요청이 실패하였습니다. path: " + path);
  }
}
