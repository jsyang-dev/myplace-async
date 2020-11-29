package info.myplace.api.place.domain;

import info.myplace.api.place.constant.DayType;
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
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceHour extends BaseEntity {

  // 시작일
  @Column private LocalDate startDate;

  // 종료일
  @Column private LocalDate endDate;

  // 일자구분(요일 or 공휴일)
  @Column
  @Enumerated(EnumType.STRING)
  private DayType dayType;

  // 영업시작시간
  @Column private LocalTime startTime;

  // 영업종료시간
  @Column private LocalTime endTime;

  // 입장마감시간
  @Column private LocalTime deadlineTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "place_id")
  private Place place;
}
