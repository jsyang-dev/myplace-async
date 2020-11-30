package info.myplace.api.place.mapper;

import info.myplace.api.place.domain.Tag;
import info.myplace.api.place.dto.TagDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

  Tag toEntity(TagDto tagDto);

  TagDto toDto(Tag tag);
}
