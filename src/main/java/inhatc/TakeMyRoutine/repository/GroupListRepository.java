package inhatc.TakeMyRoutine.repository;


import inhatc.TakeMyRoutine.domain.GroupList;
import inhatc.TakeMyRoutine.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupListRepository extends JpaRepository<GroupList, Long> {




    void deleteByTodoIdIn(List<Long> todoIds);

    List<GroupList> findByTodo(Todo todo);
    void deleteByIdIn(List<Long> ids);
}
