package inhatc.TakeMyRoutine.repository;


import inhatc.TakeMyRoutine.domain.GroupList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupListRepository extends JpaRepository<GroupList, Long> {

    Optional<GroupList> findByTodoId(Long todoId);

    List<GroupList> findByTodoGroup_Id(Long groupId);

    void deleteByIdIn(List<Long> todoIds);
}
