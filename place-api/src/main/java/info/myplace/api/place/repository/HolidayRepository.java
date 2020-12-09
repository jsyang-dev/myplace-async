package info.myplace.api.place.repository;

import info.myplace.api.place.domain.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {}
