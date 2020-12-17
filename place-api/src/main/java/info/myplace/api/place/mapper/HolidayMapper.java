package info.myplace.api.place.mapper;

import info.myplace.api.place.domain.Holiday;
import info.myplace.api.place.dto.HolidayDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HolidayMapper {

  Holiday toEntity(HolidayDto holidayDto);

  HolidayDto toDto(Holiday holiday);
}
