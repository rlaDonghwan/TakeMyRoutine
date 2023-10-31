package inhatc.TakeMyRoutine.service;

import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.domain.User;
import inhatc.TakeMyRoutine.dto.TodoRequest;
import inhatc.TakeMyRoutine.repository.TodoRepositroy;
import inhatc.TakeMyRoutine.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final TodoRepositroy todoRepositroy;
    private final UserRepository userRepository;
    private final HttpSession httpSession;  // HttpSession 주입

    public Todo join(TodoRequest todoRequest) {
        // 세션에서 userId 가져오기
        Long userId = (Long) httpSession.getAttribute("userId");

        // userId로 User 엔터티 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // 로그 추가 시작

        // 로그 추가 끝
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




}
