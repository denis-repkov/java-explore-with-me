package ru.practicum.stat.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.stat.server.model.EndpointHit;
import ru.practicum.stat.server.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointHitRepository extends JpaRepository<EndpointHit, Integer> {

    EndpointHit save(EndpointHit hit);

    @Query("SELECT new ru.practicum.stat.server.model.ViewStats(h.app, h.uri, " +
            "CASE WHEN :unique = true THEN COUNT(DISTINCT h.ip) ELSE COUNT(h.ip) END) " +
            "FROM EndpointHit h WHERE (CAST(:start AS timestamp) IS NULL OR h.timestamp >= :start) " +
            "AND (CAST(:end AS timestamp) IS NULL OR h.timestamp <= :end) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY CASE WHEN :unique = true THEN COUNT(DISTINCT h.ip) ELSE COUNT(h.ip) END DESC")
    List<ViewStats> findStats(@Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end,
                              @Param("unique") boolean unique);

    @Query("SELECT new ru.practicum.stat.server.model.ViewStats(h.app, h.uri, " +
            "CASE WHEN :unique = true THEN COUNT(DISTINCT h.ip) ELSE COUNT(h.ip) END) " +
            "FROM EndpointHit h WHERE (CAST(:start AS timestamp) IS NULL OR h.timestamp >= :start) " +
            "AND (CAST(:end AS timestamp) IS NULL OR h.timestamp <= :end) " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY CASE WHEN :unique = true THEN COUNT(DISTINCT h.ip) ELSE COUNT(h.ip) END DESC")
    List<ViewStats> findStatsByUris(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end,
                                    @Param("uris") List<String> uris,
                                    @Param("unique") boolean unique);
}