package inhatc.TakeMyRoutine.repository;

import inhatc.TakeMyRoutine.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TodoRepositroy extends JpaRepository<Todo, Long> {

    List<Todo> findByUserId(Long userId);

    @Query("SELECT t FROM Todo t WHERE t.id IN :ids")
    List<Todo> findAllByIds(@Param("ids") List<Todo> ids);



    void deleteByIdIn(List<Long> ids);

    Optional<Todo> findByIdAndUserId(Long id, Long userId);

    List<Todo> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    void deleteByIdAndUserId(Long id, Long userId);

    List<Todo> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime);


}


