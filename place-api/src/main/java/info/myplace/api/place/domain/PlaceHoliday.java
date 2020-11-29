package info.myplace.api.place.domain;

import info.myplace.api.place.constant.HolidayType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceHoliday extends BaseEntity {

  // 휴무일명
  @Column private String name;

  // 휴무일구분: 일자, 요일, 기간
  @Column
  @Enumerated(EnumType.STRING)
  private HolidayType holidayType;

  // 일자
  @Column private LocalDate date;

  // 요일
  @Column private DayOfWeek dayOfWeek;

  // 시작일
  @Column private LocalDate startDate;

  // 종료일
  @Column private LocalDate endDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "place_id")
  private Place place;
}
