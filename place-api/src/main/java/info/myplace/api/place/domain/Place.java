package info.myplace.api.place.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.geo.Point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Place extends BaseEntity {

  // 장소명
  @Column private String name;

  // 좌표(위도, 경도)
  @Column private Point point;

  // 이미지 URL
  @Column private String imageUrl;

  // 추천수
  @Column private Integer recommendCount;

  // 조회수
  @Column private Integer readCount;

  // 설명
  @Column(columnDefinition = "text")
  private String detail;

  // 특이사항
  @Column(columnDefinition = "text")
  private String special;

  @OneToMany(mappedBy = "place")
  @Builder.Default
  private Set<PlaceTag> placeTags = new HashSet<>();

  @OneToMany(mappedBy = "place")
  @Builder.Default
  private Set<PlaceHour> placeHours = new HashSet<>();

  @OneToMany(mappedBy = "place")
  @Builder.Default
  private Set<PlaceFee> placeFees = new HashSet<>();
}
