package ru.practicum.ewm.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Compilation;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    List<Compilation> findAll();

    @Query(value = "SELECT * FROM compilations c " +
            "WHERE (:pinned IS NULL OR c.pinned = :pinned) OFFSET :from LIMIT :size",
            nativeQuery = true
    )
    List<Compilation> findCompilations(
            @Param("pinned") Boolean pinned,
            @Param("from") int from,
            @Param("size") int size
    );

    Optional<Compilation> findById(int id);

    Compilation save(Compilation request);

    void deleteById(int id);
}