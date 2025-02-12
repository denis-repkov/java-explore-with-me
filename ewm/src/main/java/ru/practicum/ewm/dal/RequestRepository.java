package ru.practicum.ewm.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Integer> {

    Optional<ParticipationRequest> findById(int id);

    List<ParticipationRequest> findByRequesterId(int id);

    ParticipationRequest save(ParticipationRequest request);

    List<ParticipationRequest> findByRequesterIdAndEventId(int requesterId, int eventId);

    @Query("SELECT r FROM ParticipationRequest r JOIN Event e ON e.id = r.eventId " +
            "WHERE e.initiator.id = :initiatorId AND r.eventId = :eventId")
    List<ParticipationRequest> findByInitiatorIdAndEventId(int initiatorId, int eventId);

    Optional<ParticipationRequest> findByIdAndRequesterId(int id, int requesterId);

    List<ParticipationRequest> findByIdIn(List<Integer> ids);

    List<ParticipationRequest> findByEventIdAndRequesterIdAndIdIn(int eventId, int requesterId, List<Integer> ids);
}