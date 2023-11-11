package inhatc.TakeMyRoutine.repository;

import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.domain.TodoGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoGroupRepository extends JpaRepository<TodoGroup,Long> {

    Optional<TodoGroup> findById(Long groupId);

    TodoGroup findByGroupName(String updatedGroupName);


    void deleteByIdIn(List<Long> groupId);
}