package inhatc.TakeMyRoutine.service;

import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.domain.User;
import inhatc.TakeMyRoutine.dto.TodoRequest;
import inhatc.TakeMyRoutine.repository.TodoRepositroy;
import inhatc.TakeMyRoutine.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jdk.jfr.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepositroy todoRepositroy;
    private final UserRepository userRepository;
    private final HttpSession httpSession;  // HttpSession 주입

    public Todo join(TodoRequest todoRequest) {
        // 세션에서 userId 가져오기
        Long userId = (Long) httpSession.getAttribute("userId");

        // userId로 User 엔터티 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));


        // Todo 엔터티 생성 및 값 설정
        Todo todo = new Todo();

        todo.setUser(user);
        todo.setTitle(todoRequest.getTitle());
        todo.setDataTime(todoRequest.getDateTime());
        todo.setMemo(todoRequest.getMemo());
        todo.setPlace(todoRequest.getPlace());

        // Todo 저장
        todoRepositroy.save(todo);
        return todo;
    }

    public List<Todo> getTodoList(Long userId) {
        // TodoRepository를 사용하여 해당 사용자의 Todo 리스트를 가져오는 로직 작성
        return todoRepositroy.findByUserId(userId);
    }

    public void updateTodo(Long todoId, String updatedTitle, String updatedMemo, String updatedPlace, LocalDateTime updatedDateTime) {
        // 세션에서 userId 가져오기
        Long userId = (Long) httpSession.getAttribute("userId");

        // Todo 엔터티 조회
        Optional<Todo> optionalTodo = todoRepositroy.findByIdAndUserId(todoId, userId);

        optionalTodo.ifPresent(todo -> {
            // Todo를 찾았으면 업데이트
            todo.setTitle(updatedTitle);
            todo.setMemo(updatedMemo);
            todo.setPlace(updatedPlace);
            todo.setDataTime(updatedDateTime);  // 수정된 부분

            // 업데이트된 Todo를 저장
            todoRepositroy.save(todo);
        });
    }


    public void completeTodos(List<Long> todoIds) {
        todoRepositroy.deleteByIdIn(todoIds);
    }


    public Todo getEventByUserIdAndTodoId(Long userId, Long todoId) {
        return todoRepositroy.findByIdAndUserId(todoId, userId).orElse(null);
    }

    public Todo updateCalendar(Todo existingTodo) {
        // 이벤트 정보를 업데이트
        // 이 부분에서 다른 로직이 필요하다면 추가하셔도 됩니다.
        existingTodo.setTitle(existingTodo.getTitle());
        existingTodo.setDataTime(existingTodo.getDataTime());
        existingTodo.setMemo(existingTodo.getMemo());
        existingTodo.setPlace(existingTodo.getPlace());

        // 데이터베이스에 업데이트된 이벤트 저장
        return todoRepositroy.save(existingTodo);
    }
}