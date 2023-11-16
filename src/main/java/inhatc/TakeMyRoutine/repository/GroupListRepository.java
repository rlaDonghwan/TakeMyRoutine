package inhatc.TakeMyRoutine.repository;


import inhatc.TakeMyRoutine.domain.GroupList;
import inhatc.TakeMyRoutine.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupListRepository extends JpaRepository<GroupList, Long> {

    void deleteByTodoIdIn(List<Long> todoIds);

    List<GroupList> findByTodo(Todo todo);

    List<GroupList> findByTodoGroup_Id(Long groupId);
}
