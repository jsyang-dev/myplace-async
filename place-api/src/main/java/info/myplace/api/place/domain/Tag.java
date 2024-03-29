package info.myplace.api.place.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class Tag extends BaseEntity {

  // 태그명
  @Column private String name;

  @OneToMany(mappedBy = "tag")
  @Builder.Default
  private Set<PlaceTag> placeTags = new HashSet<>();
}
