package inhatc.TakeMyRoutine.repository;


import inhatc.TakeMyRoutine.domain.GroupList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupListRepository extends JpaRepository<GroupList, Long> {

}
