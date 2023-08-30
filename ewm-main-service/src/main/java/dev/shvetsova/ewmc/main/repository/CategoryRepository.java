package dev.shvetsova.ewmc.main.repository;

import dev.shvetsova.ewmc.main.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByIdNotAndName(long catId, String name);
}
