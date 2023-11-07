package inhatc.TakeMyRoutine.repository;

import inhatc.TakeMyRoutine.domain.TodoGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoGroupRepository extends JpaRepository<TodoGroup,Long> {

}
