package inhatc.TakeMyRoutine.controller;

import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.dto.TodoRequest;
import inhatc.TakeMyRoutine.service.CalendarService;
import inhatc.TakeMyRoutine.service.TodoService;
import jakarta.servlet.http.HttpSession;
import jdk.jfr.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j

@RequestMapping("/home")
public class CalendarController {

    private final TodoService todoService;
    @GetMapping("/calendar")
    public String calendarPage(Model model, HttpSession session) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "calendar");

        Long userId = (Long) session.getAttribute("userId");

        // 사용자 정보가 없으면 로그인 페이지로 리다이렉트
        if (userId == null) {
            return "redirect:/home/login";
        }

        // 사용자 정보가 있으면 Todo 페이지로 이동
        model.addAttribute("todoRequest", new TodoRequest(userId));
        // 사용자 정보가 있으면 Todo 데이터를 가져와서 반환
        return "calendar";

    }

    @PostMapping("/calendar")
    public ResponseEntity<List<Todo>> addCalendar(@ModelAttribute TodoRequest todoRequest, Model model, HttpSession session) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "calendar");
        Long userId = (Long) session.getAttribute("userId");
        todoRequest.setTodoId(userId);

        todoService.join(todoRequest);
        List<Todo> todoList = todoService.getTodoList(userId);
        return ResponseEntity.ok(todoList);

    }

    @PostMapping("/calendar/addEvent")
    public ResponseEntity<Todo> addEvent(@RequestBody Map<String, String> eventData, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        TodoRequest todoRequest = new TodoRequest();
        todoRequest.setTitle(eventData.get("title"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        todoRequest.setDateTime(LocalDateTime.parse(eventData.get("dataTime"), formatter));
        todoRequest.setMemo(eventData.get("description"));
        todoRequest.setPlace(eventData.get("location"));
        todoRequest.setTodoId(userId);

        Todo savedTodo = todoService.join(todoRequest);

        log.info("Saved Todo: {}, {}, {}, {}", savedTodo.getTitle(), savedTodo.getDataTime(), savedTodo.getMemo(), savedTodo.getPlace());

        return ResponseEntity.ok(savedTodo);
    }

    //userId todoId 값을 가져와서 서로 비교해서 이 둘이 데이터베이스에 존재한다면 정보 수정 로직 구현
    @PostMapping("/calendar/updateEvent")
    public ResponseEntity<Todo> updateEvent(@RequestBody Map<String, String> eventData, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Long todoId = Long.parseLong(eventData.get("todoId"));
        Todo existingTodo = todoService.getEventByUserIdAndTodoId(userId, todoId);

        if (existingTodo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 업데이트할 필드만 가져오기
        String title = eventData.get("title");
        String dateTimeString = eventData.get("dataTime");
        String memo = eventData.get("description");
        String place = eventData.get("location");

        // 값이 넘어온 경우에만 업데이트
        if (title != null) {
            existingTodo.setTitle(title);
        }

        if (dateTimeString != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            existingTodo.setDataTime(LocalDateTime.parse(dateTimeString, formatter));
        }

        if (memo != null) {
            existingTodo.setMemo(memo);
        }

        if (place != null) {
            existingTodo.setPlace(place);
        }

        Todo updatedTodo = todoService.updateCalendar(existingTodo);

        log.info("Updated Todo: {}, {}, {}, {}", updatedTodo.getTitle(), updatedTodo.getDataTime(), updatedTodo.getMemo(), updatedTodo.getPlace());

        return ResponseEntity.ok(updatedTodo);
    }







    //값 수정한 후에 데이터 값을 캘린더에 뿌려서 기존에 데이터 베이스에 저장되어있던 값들도 캘린더의 띄우기


}