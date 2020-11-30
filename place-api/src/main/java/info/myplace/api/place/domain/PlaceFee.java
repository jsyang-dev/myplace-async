package info.myplace.api.place.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceFee extends BaseEntity {

  // 연령대명
  @Column private String ageGroupName;

  // 시작개월
  @Column private Integer startMonth;

  // 종료개월
  @Column private Integer endMonth;

  // 요금
  @Column private Long fee;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "place_id")
  private Place place;
}
