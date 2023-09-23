package dev.shvetsova.ewmc.users.repo;

import dev.shvetsova.ewmc.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAllByIdIn(List<Long> ids, PageRequest page);

    Optional<User> findByEmail(String email);

    void deleteByUid(String userId);

    boolean existsByUid(String userId);

    Optional<User> findByUid(String userId);

    List<User> findAllByUidIn(List<String> userIds);

    Page<User> findAllByUidIn(List<String> ids, PageRequest page);
}
