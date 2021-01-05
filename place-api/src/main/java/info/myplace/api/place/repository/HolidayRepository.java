package info.myplace.api.place.repository;

import info.myplace.api.place.domain.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

  List<Holiday> findByDateBetween(LocalDate startDate, LocalDate endDate);

  void deleteByDateBetween(LocalDate startDate, LocalDate endDate);

  int deleteTagById(long id);
}
