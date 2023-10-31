package inhatc.TakeMyRoutine.controller;

import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.dto.TodoRequest;
import inhatc.TakeMyRoutine.service.TodoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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



}