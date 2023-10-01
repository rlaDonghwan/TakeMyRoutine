package inhatc.TakeMyRoutine.service;

import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.domain.User;
import inhatc.TakeMyRoutine.dto.TodoRequest;
import inhatc.TakeMyRoutine.repository.TodoRepositroy;
import inhatc.TakeMyRoutine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepositroy todoRepositroy;
    private final UserRepository userRepository;

    public void join(TodoRequest todoRequest) {
        // 사용자 아이디로 사용자 엔터티를 가져옴
        User user = userRepository.findById(todoRequest.getTodoId()).orElse(null);

        // 사용자가 존재하면 Todo 엔터티에 추가
        if (user != null) {
            todoRequest.setTodoNickName(user.getNickname());
            Todo todo = todoRequest.toEntity(user);
            todoRepositroy.save(todo);
        }
    }
}
