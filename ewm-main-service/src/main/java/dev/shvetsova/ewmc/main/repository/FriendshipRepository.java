package dev.shvetsova.ewmc.main.repository;

import dev.shvetsova.ewmc.main.model.Friendship;
import dev.shvetsova.ewmc.main.enums.FriendshipState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long>, QuerydslPredicateExecutor<Friendship> {
    void deleteByFollowerIdAndId(long followerId, long friendId);

    boolean existsByFollowerIdAndFriendIdAndStateNot(long followerId, long friendId, FriendshipState status);

    boolean existsByIdAndFollowerId(long subsId, long followerId);
}
