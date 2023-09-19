package dev.shvetsova.ewmc.subscription.repo;

import dev.shvetsova.ewmc.subscription.model.Friendship;
import dev.shvetsova.ewmc.subscription.model.FriendshipState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    void deleteByFollowerIdAndId(String followerId, Long friendId);

    boolean existsByFollowerIdAndFriendIdAndStateNot(String followerId, String friendId, FriendshipState status);

    boolean existsByIdAndFollowerId(long subsId, String followerId);

    @Query("select f.friendId from Friendship f where f.followerId=:userId")
    List<Long> findAllFriends(String userId);

    @Query("select f.followerId from Friendship f where f.friendId=:userId")
    List<Long> findAllFollowers(String userId);

    List<Friendship> findAllByFriendIdAndState(String userId, FriendshipState from);

    List<Friendship> findAllByFriendId(String userId);

    List<Friendship> findAllByFollowerIdAndState(String userId, FriendshipState from);

    List<Friendship> findAllByFollowerId(String userId);

    Optional<Friendship> findAllByFriendIdAndFollowerId(String userId, String followerId);
}
