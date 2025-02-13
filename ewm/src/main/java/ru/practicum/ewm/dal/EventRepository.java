package ru.practicum.ewm.dal;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT e FROM Event e " +
            "WHERE (:users IS NULL OR e.initiator.id IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (e.eventDate BETWEEN COALESCE(:rangeStart, e.eventDate) AND COALESCE(:rangeEnd, e.eventDate))")
    Page<Event> findFilteredEvents(
            @Param("users") List<Integer> users,
            @Param("states") List<EventStatus> states,
            @Param("categories") List<Integer> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable
    );

    @Query("SELECT e FROM Event e " +
            "WHERE e.state = 'PUBLISHED' " +
            "AND (LOWER(e.annotation) LIKE LOWER(CONCAT('%',:text,'%')) " +
            "     OR LOWER(e.description) LIKE LOWER(CONCAT('%',:text,'%')) " +
            "     OR :text IS NULL) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (CAST(:rangeStart AS timestamp) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (CAST(:rangeEnd AS timestamp) IS NULL OR e.eventDate <= :rangeEnd) " +
            "AND (:onlyAvailable = FALSE OR e.participantLimit = 0 OR e.confirmedRequests < e.participantLimit) " +
            "ORDER BY " +
            "   CASE WHEN :sort = 'EVENT_DATE' THEN e.eventDate END ASC, " +
            "   CASE WHEN :sort = 'VIEWS' THEN e.views END DESC")
    Page<Event> findFilteredEvents(
            @Param("text") String text,
            @Param("categories") List<Integer> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            @Param("onlyAvailable") boolean onlyAvailable,
            @Param("sort") String sort,
            Pageable pageable
    );

    Optional<Event> findById(int id);

    List<Event> findByIdIn(List<Integer> ids);

    Optional<Event> findByIdAndState(int id, EventStatus state);

    Page<Event> findByInitiator_Id(int initiatorId, Pageable pageable);

    Optional<Event> findByInitiator_IdAndId(int initiatorId, int id);

    Event save(Event request);

    void deleteById(int id);

    @Query("""
                SELECT e FROM Event e
                JOIN ParticipationRequest pr ON pr.eventId = e.id
                JOIN Subscription s ON s.id.subscriberId = pr.requesterId
                WHERE s.id.userId = :userId
                AND pr.status = ru.practicum.ewm.model.RequestStatus.CONFIRMED
                ORDER BY e.eventDate DESC
            """)
    List<Event> getRecommendations(int userId);
}