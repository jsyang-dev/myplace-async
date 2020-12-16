package info.myplace.api.place.exception;

public class TagNotFoundException extends MyPlaceException {
  public TagNotFoundException(long id) {
    super("유효한 Tag가 존재하지 않습니다. id: " + id);
  }
}
