package info.myplace.api.place.exception;

import org.springframework.http.HttpStatus;

public class HolidayNotFoundException extends GlobalException {
  public HolidayNotFoundException(long id) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, "유효한 Holiday가 존재하지 않습니다. id: " + id);
  }
}
