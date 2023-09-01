package dev.shvetsova.ewmc.event.repo;

import dev.shvetsova.ewmc.event.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByIdNotAndName(long catId, String name);
}
