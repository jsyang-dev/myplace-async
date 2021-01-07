package info.myplace.api.place.repository;

import info.myplace.api.place.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

  List<Tag> findByNameStartsWith(String keyword);

  int deleteTagById(long id);
}
