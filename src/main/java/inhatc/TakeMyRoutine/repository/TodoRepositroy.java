package inhatc.TakeMyRoutine.repository;

import inhatc.TakeMyRoutine.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TodoRepositroy extends JpaRepository<Todo, Long> {

    List<Todo> findByUserId(Long userId);


    void deleteByIdIn(List<Long> ids);

    Optional<Todo> findByIdAndUserId(Long id, Long userId);

    List<Todo> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    void deleteByIdAndUserId(Long id, Long userId);

    List<Todo> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}


