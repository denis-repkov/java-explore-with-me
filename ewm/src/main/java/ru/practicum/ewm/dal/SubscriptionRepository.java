package ru.practicum.ewm.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Subscription;
import ru.practicum.ewm.model.SubscriptionId;
import ru.practicum.ewm.model.UserShort;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    Subscription save(Subscription subscription);

    boolean existsById(SubscriptionId id);

    void deleteById(SubscriptionId id);

    @Query("SELECT u from UserShort u JOIN Subscription s ON u.id = s.id.subscriberId WHERE s.id.userId = :userId")
    List<UserShort> findSubscribers(@Param("userId") int userId);

    @Query("""
                SELECT COUNT(s) > 0 FROM Subscription s
                WHERE s.id.userId IN (
                    SELECT sub.id.subscriberId FROM Subscription sub WHERE sub.id.userId = :userId
                ) AND s.id.subscriberId = :subscriberId
            """)
    boolean isSubscriberOfSubscribers(@Param("userId") int userId, @Param("subscriberId") int subscriberId);
}