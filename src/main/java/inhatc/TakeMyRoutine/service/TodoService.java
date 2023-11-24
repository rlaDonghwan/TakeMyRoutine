package inhatc.TakeMyRoutine.service;

import inhatc.TakeMyRoutine.domain.GroupList;
import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.domain.TodoGroup;
import inhatc.TakeMyRoutine.domain.User;
import inhatc.TakeMyRoutine.dto.TodoRequest;
import inhatc.TakeMyRoutine.repository.GroupListRepository;
import inhatc.TakeMyRoutine.repository.TodoGroupRepository;
import inhatc.TakeMyRoutine.repository.TodoRepositroy;
import inhatc.TakeMyRoutine.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepositroy todoRepositroy;
    private final UserRepository userRepository;
    private final TodoGroupRepository todoGroupRepository;
    private final GroupListRepository groupListRepository;
    private final HttpSession httpSession;  // HttpSession 주입


    //값을 추가하는 서비스
    public Todo join(TodoRequest todoRequest) {
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

        todo.setEndTime(todoRequest.getStartTime().plusHours(1));

        // Todo 저장
        todoRepositroy.save(todo);
        return todo;
    }
    //-------------------------------------------------------------------------------------------------

    //홈 화면 리스트에 출력된 리스트를 불러오는 서비스
    public List<Todo> getTodoList(Long userId) {
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
        log.info("Event added successfully. User: {}, Title: {}, Time: {}, Content: {}, Place: {}",
                userId, title, dateTime, content, place);
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
    //------------------------------------------------------------------------------------------------

    //그룹이 존재하는지 확인하는 메서드
    public boolean checkGroupExistence(List<Long> todoIds) {
        return todoIds.stream()
                .map(todoRepositroy::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .anyMatch(todo -> !groupListRepository.findByTodo(todo).isEmpty());
    }
    //------------------------------------------------------------------------------------------------

    //그룹을 삭제하는 메서드
    public void deleteGroup(List<Long> todoIds) {
        // 여기서 해당 todoIds를 참조하는 TodoGroup을 찾아서 삭제하는 로직을 추가합니다.
        List<TodoGroup> todoGroups = todoGroupRepository.findByGroupLists_Todo_IdIn(todoIds);
        todoGroupRepository.deleteAll(todoGroups);

        todoGroupRepository.deleteByIdIn(todoIds);
        System.out.println(todoIds);
        // 나머지 삭제 로직은 유지합니다.
        todoRepositroy.deleteByIdIn(todoIds);
        groupListRepository.deleteByTodoIdIn(todoIds);
    }
    //------------------------------------------------------------------------------------------------



    //그룹화 된 투두리스트 불러오는 메서드
    public List<TodoRequest> getTodoListByGroupId(Long groupId) {
        List<GroupList> groupLists = groupListRepository.findByTodoGroup_Id(groupId); //그룹 아이디로 그룹화된걸 찾음

        List<Long> todoIds = groupLists.stream() //그룹화된 Todoid를 찾아서 리스트에 넣음
                .map(GroupList::getTodo)
                .map(Todo::getId)
                .collect(Collectors.toList());

        if (!todoIds.isEmpty()) { //리스트가 존재하면
            List<Todo> todos = todoRepositroy.findAllById(todoIds); //TodoRequest(DTO)에 접근하여 값을 가져옴
            return todos.stream()
                    .map(TodoRequest::new)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
    //------------------------------------------------------------------------------------------------

    // 다운로드한 Todo를 현재 로그인한 사용자의 Todo 리스트에 추가하는 메서드
    public void downloadTodoList(List<Long> todoIds, Long userId) {
        // 기존 Todo들을 ID로 조회
        List<Todo> todosToDownload = todoRepositroy.findAllById(todoIds);

        // 새로운 Todo들을 저장하기 위한 리스트
        List<Todo> newTodos = new ArrayList<>();

        // 사용자 ID로 사용자 객체를 찾음
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // 각 Todo를 복제하여 새로운 Todo를 생성하고 사용자를 설정
            for (Todo todo : todosToDownload) {
                Todo newTodo = new Todo();
                newTodo.setTitle(todo.getTitle());
                newTodo.setStartTime(todo.getStartTime());
                newTodo.setEndTime(todo.getEndTime());
                newTodo.setMemo(todo.getMemo());
                newTodo.setPlace(todo.getPlace());
                newTodo.setUser(user);

                newTodos.add(newTodo);
            }
            // 새로운 Todo들을 저장
            todoRepositroy.saveAll(newTodos);

        } else {
            // 사용자를 찾지 못한 경우에 대한 처리
            // (예: 에러 로깅 또는 예외 처리)
            log.error("사용자를 찾을 수 없습니다. 사용자 ID: {}", userId);
            // 또는 특정 예외를 던지는 등의 처리
        }
    }
    //------------------------------------------------------------------------------------------------

}




