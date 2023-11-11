package inhatc.TakeMyRoutine.service;

import inhatc.TakeMyRoutine.domain.GroupList;
import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.domain.User;
import inhatc.TakeMyRoutine.dto.TodoRequest;
import inhatc.TakeMyRoutine.repository.GroupListRepository;
import inhatc.TakeMyRoutine.repository.TodoGroupRepository;
import inhatc.TakeMyRoutine.repository.TodoRepositroy;
import inhatc.TakeMyRoutine.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepositroy todoRepositroy;
    private final UserRepository userRepository;
    private final TodoGroupRepository todoGroupRepository;
    private final GroupListRepository groupListRepository;
    private final HttpSession httpSession;  // HttpSession 주입

    //값을 추가하는 서비스
    public Todo join(TodoRequest todoRequest) {
        // 세션에서 userId 가져오기
        Long userId = (Long) httpSession.getAttribute("userId");

        // userId로 User 엔터티 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Todo 엔터티 생성 및 값 설정
        Todo todo = new Todo();

        todo.setUser(user);
        todo.setTitle(todoRequest.getTitle());
        todo.setStartTime(todoRequest.getStartTime());
        todo.setMemo(todoRequest.getMemo());
        todo.setPlace(todoRequest.getPlace());

        // startTime에 1시간 추가하여 endTime 설정
        todo.setEndTime(todoRequest.getStartTime().plusHours(1));

        // Todo 저장
        todoRepositroy.save(todo);
        return todo;
    }
    //-------------------------------------------------------------------------------------------------
    //홈 화면 리스트에 출력된 리스트를 불러오는 서비스
    public List<Todo> getTodoList(Long userId) {
        // TodoRepository를 사용하여 해당 사용자의 Todo 리스트를 가져오는 로직 작성
        return todoRepositroy.findByUserId(userId);
    }
    //-------------------------------------------------------------------------------------------------

    //캘린더에 사용자 아이디 시간 값들을 불러오는 서비스
    public List<Todo> getTodosByUserIdAndTime(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return todoRepositroy.findByUserIdAndStartTimeBetween(userId, startDateTime, endDateTime);
    }
    //-------------------------------------------------------------------------------------------------

    //투두리스트를 수정하는 서비스
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

            // startTime 업데이트
            todo.setStartTime(updatedDateTime);

            // endTime 업데이트 (startTime에서 1시간 더하기)
            LocalDateTime updatedEndTime = updatedDateTime.plusHours(1);
            todo.setEndTime(updatedEndTime);

            // 업데이트된 Todo를 저장
            todoRepositroy.save(todo);
        });
    }
    //-------------------------------------------------------------------------------------------------

    //두투리스트 삭제
    public void completeTodos(List<Long> todoIds) {
        todoRepositroy.deleteByIdIn(todoIds);
    }
    //-------------------------------------------------------------------------------------------------


    //캘린더 이벤트 추가
    public void addEvent(Long userId, String title, LocalDateTime dateTime, String content, String place) {
        // 여기에 데이터베이스에 이벤트를 추가하는 로직을 넣어야 해
        // 예를 들어, Todo 엔터티를 생성하고 저장하는 식으로 구현 가능

        // User 엔터티를 가져오는 코드
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Todo todo = Todo.builder()
                .user(user)  // User 객체로 설정
                .title(title)
                .startTime(dateTime)
                .endTime(dateTime.plusHours(1))  // 임의로 1시간 더한 것, 조절 가능
                .memo(content)
                .place(place)
                .build();

        todoRepositroy.save(todo);
    }
    //-------------------------------------------------------------------------------------------------

    //캘린더 이벤트 수정
    public void updateEvent(Long todoId, String updatedTitle, LocalDateTime updatedDateTime, Long userId) {

        // Todo 엔터티 조회
        Optional<Todo> optionalTodo = todoRepositroy.findByIdAndUserId(todoId, userId);

        optionalTodo.ifPresent(todo -> {
            // Todo를 찾았으면 업데이트
            todo.setTitle(updatedTitle);
            todo.setStartTime(updatedDateTime);

            // endTime을 한 시간 후로 설정
            LocalDateTime updatedEndTime = updatedDateTime.plusHours(1);
            todo.setEndTime(updatedEndTime);

            // 업데이트된 Todo를 저장
            todoRepositroy.save(todo);
        });
    }
    //-------------------------------------------------------------------------------------------------

    //캘린더 이벤트 삭제
    public void deleteEvent(Long todoId, Long userId) {
        todoRepositroy.deleteByIdAndUserId(todoId, userId);
    }
    //-------------------------------------------------------------------------------------------------




}