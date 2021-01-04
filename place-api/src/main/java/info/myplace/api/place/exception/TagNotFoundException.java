package info.myplace.api.place.exception;

import org.springframework.http.HttpStatus;

public class TagNotFoundException extends GlobalException {
  public TagNotFoundException(long id) {
    super(HttpStatus.NOT_FOUND, "유효한 Tag가 존재하지 않습니다. id: " + id);
  }
}
