package info.myplace.api.place.domain;

import info.myplace.api.place.dto.HolidayDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Holiday extends BaseEntity {

  // 일자
  @Column private LocalDate date;

  // 공휴일명
  @Column private String name;

  public Holiday update(HolidayDto holidayDto) {
    this.setDate(holidayDto.getDate());
    this.setName(holidayDto.getName());
    return this;
  }
}
