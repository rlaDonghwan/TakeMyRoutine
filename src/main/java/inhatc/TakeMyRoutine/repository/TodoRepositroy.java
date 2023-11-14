package inhatc.TakeMyRoutine.repository;

import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.domain.TodoGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TodoRepositroy extends JpaRepository<Todo, Long> {

    List<Todo> findByUserId(Long userId);
    Optional<Todo> findById(Long todoId);

    void deleteByIdIn(List<Long> ids);

    Optional<Todo> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    List<Todo> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime);


}


