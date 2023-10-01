package inhatc.TakeMyRoutine.repository;

import inhatc.TakeMyRoutine.domain.Todo;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface TodoRepositroy extends JpaRepository<Todo, Long> {


}
