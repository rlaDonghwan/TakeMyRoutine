package inhatc.TakeMyRoutine.repository;

import inhatc.TakeMyRoutine.domain.TodoGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoGroupRepository extends JpaRepository<TodoGroup,Long> {

    Optional<TodoGroup> findById(Long groupId);
    List<TodoGroup> findByShareTrue();

    void deleteByIdIn(List<Long> groupIds);

    List<TodoGroup> findByGroupLists_Todo_IdIn(List<Long> todoIds);

}
