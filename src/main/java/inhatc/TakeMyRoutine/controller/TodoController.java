package inhatc.TakeMyRoutine.controller;

import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.domain.User;
import inhatc.TakeMyRoutine.dto.TodoRequest;
import inhatc.TakeMyRoutine.service.TodoService;
import inhatc.TakeMyRoutine.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/home")
public class TodoController {
    private final TodoService todoService;
    private final HttpSession httpSession;

    @GetMapping("/todoInsert")
    public String todoPage(Model model, HttpSession session) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "Todo");
        log.info("TodoController");

        Long userId = (Long) session.getAttribute("userId");

        // 사용자 정보가 없으면 로그인 페이지로 리다이렉트
        if (userId == null) {
            return "redirect:/home/login";
        }

        // 사용자 정보가 있으면 Todo 페이지로 이동
        model.addAttribute("todoRequest", new TodoRequest(userId));
        return "todoInsert";
    }


    @PostMapping("/todoInsert")
    public String todoInsert(@Valid @ModelAttribute TodoRequest todoRequest, Model model) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "Todo");
        Long userId = (Long) httpSession.getAttribute("userId");
        todoRequest.setTodoId(userId);

        todoService.join(todoRequest);
        return "redirect:/home";

    }


    @PostMapping("/deleteTodos")
    public ResponseEntity<String> deleteTodo(@RequestBody Map<String, List<Long>> requestBody) {
        try {
            // 여러 아이디를 가진 todo들을 삭제
            List<Long> todoIds = requestBody.get("todoIds");
            todoService.completeTodos(todoIds);

            return new ResponseEntity<>("Completed successfully", HttpStatus.OK);
        } catch (Exception e) {
            // 예외 발생 시 로그 출력
            e.printStackTrace();
            return new ResponseEntity<>("Failed to complete todos", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateTodo")
    public ResponseEntity<String> updateTodos(@RequestBody Map<String, Object> requestBody) {
        try {
            // 업데이트할 Todo의 ID 및 새로운 정보 가져오기
            Long todoId = getLongFromObject(requestBody.get("todoId"));
            String updatedTitle = (String) requestBody.get("updatedTitle");
            String updatedMemo = (String) requestBody.get("updatedMemo");
            String updatedPlace = (String) requestBody.get("updatedPlace");

            // 업데이트된 정보로 Todo를 업데이트
            LocalDateTime updatedDateTime = getLocalDateTimeFromObject(requestBody.get("updatedDateTime"));
            todoService.updateTodo(todoId, updatedTitle, updatedMemo, updatedPlace, updatedDateTime);

            return new ResponseEntity<>("Updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            // 예외 발생 시 로그 출력
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update todo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private LocalDateTime getLocalDateTimeFromObject(Object value) {
        if (value instanceof String) {
            return LocalDateTime.parse((String) value);
        } else {
            throw new IllegalArgumentException("Invalid type for updatedDateTime");
        }
    }

    private Long getLongFromObject(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            return Long.parseLong((String) value);
        } else {
            throw new IllegalArgumentException("Invalid type for todoId");
        }
    }









}
