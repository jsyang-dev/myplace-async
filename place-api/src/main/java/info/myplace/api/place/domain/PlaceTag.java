package info.myplace.api.place.domain;

import lombok.Builder;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceTag extends BaseEntity {}
