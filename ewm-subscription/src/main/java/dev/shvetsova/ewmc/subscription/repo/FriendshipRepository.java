package dev.shvetsova.ewmc.subscription.repo;

import dev.shvetsova.ewmc.subscription.model.Friendship;
import dev.shvetsova.ewmc.subscription.model.FriendshipState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    void deleteByFollowerIdAndId(long followerId, long friendId);

    boolean existsByFollowerIdAndFriendIdAndStateNot(long followerId, long friendId, FriendshipState status);

    boolean existsByIdAndFollowerId(long subsId, long followerId);

    @Query("select f.friendId from Friendship f where f.followerId=:userId")
    List<Long> findAllFriends(long userId);

    @Query("select f.followerId from Friendship f where f.friendId=:userId")
    List<Long> findAllFollowers(long userId);

    List<Friendship> findAllByFriendIdAndState(long userId, FriendshipState from);

    List<Friendship> findAllByFriendId(long userId);

    List<Friendship> findAllByFollowerIdAndState(long userId, FriendshipState from);

    List<Friendship> findAllByFollowerId(long userId);
}
