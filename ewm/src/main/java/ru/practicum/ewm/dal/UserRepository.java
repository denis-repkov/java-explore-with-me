package ru.practicum.ewm.dal;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.UserShort;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT e FROM User e WHERE (:ids IS NULL OR e.id IN :ids)")
    Page<User> findAll(List<Integer> ids, Pageable pageable);

    Optional<User> findById(int id);

    @Query("SELECT e FROM UserShort e WHERE e.id = :id")
    Optional<UserShort> findShortUser(int id);

    User save(User request);

    void deleteById(int id);

    @Query("""
                SELECT u
                FROM UserShort u
                JOIN Subscription s1 ON u.id = s1.id.subscriberId
                JOIN Subscription s2 ON s1.id.userId = s2.id.subscriberId
                WHERE s2.id.userId = :userId
                AND s1.id.subscriberId NOT IN (
                    SELECT s.id.subscriberId FROM Subscription s WHERE s.id.userId = :userId
                )
            """)
    List<UserShort> getRecommendations(int userId);
}