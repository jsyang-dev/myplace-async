package info.myplace.api.place.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class HolidayDto {

  private Long id;

  @NotNull private LocalDate date;

  @NotBlank private String name;

  public HolidayDto(@NotNull LocalDate date, @NotBlank String name) {
    this.date = date;
    this.name = name;
  }
}
