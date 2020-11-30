package info.myplace.api.place.repository;

import info.myplace.api.place.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {}
