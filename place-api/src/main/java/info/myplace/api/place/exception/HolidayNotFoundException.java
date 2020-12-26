package info.myplace.api.place.exception;

public class HolidayNotFoundException extends MyPlaceException {
    public HolidayNotFoundException(long id) {
        super("유효한 Holiday가 존재하지 않습니다. id: " + id);
    }
}
