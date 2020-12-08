package info.myplace.api.place.service;

import info.myplace.api.place.domain.Tag;
import info.myplace.api.place.dto.TagDto;
import info.myplace.api.place.mapper.TagMapper;
import info.myplace.api.place.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  @Override
  public TagDto create(TagDto tagDto) {

    Tag tag = tagMapper.toEntity(tagDto);
    tagRepository.save(tag);

    return tagMapper.toDto(tag);
  }
}
