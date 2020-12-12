package info.myplace.api.place.repository;

import info.myplace.api.place.domain.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

  @Query("select h from Holiday h where h.date between :startDate and :endDate")
  List<Holiday> findByPeriod(
      @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
