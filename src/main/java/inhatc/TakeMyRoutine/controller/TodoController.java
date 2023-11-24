package inhatc.TakeMyRoutine.controller;

import inhatc.TakeMyRoutine.domain.TodoGroup;
import inhatc.TakeMyRoutine.dto.TodoRequest;
import inhatc.TakeMyRoutine.service.TodoGroupService;
import inhatc.TakeMyRoutine.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/home")
public class TodoController {
    private final TodoService todoService;
    private final TodoGroupService todoGroupService;
    private final HttpSession httpSession;

    //투두리스트 화면 컨트롤러
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
    //-------------------------------------------------------------------------------------------------

    //투두리스트 추가 컨트롤러
    @PostMapping("/todoInsert")
    public String todoInsert(@Valid @ModelAttribute TodoRequest todoRequest, Model model) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "Todo");
        Long userId = (Long) httpSession.getAttribute("userId");
        todoRequest.setTodoId(userId);

        todoService.join(todoRequest);
        return "redirect:/home";
    }
    //-------------------------------------------------------------------------------------------------

    //투두리스트를 수정하는 컨트롤러
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
    //-------------------------------------------------------------------------------------------------

    //그룹에 존재 여부를 확인하는 컨트롤러
    @PostMapping("/checkGroupExistence")
    public ResponseEntity<Map<String, Boolean>> checkGroupExistence(@RequestBody Map<String, List<Long>> requestBody) {
        try {
            List<Long> todoIds = requestBody.get("todoIds");
            boolean exists = todoService.checkGroupExistence(todoIds);

            Map<String, Boolean> response = new HashMap<>();
            response.put("exists", exists);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //-------------------------------------------------------------------------------------------------

    //투두리스트 삭제하는 컨트롤러
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
    //-------------------------------------------------------------------------------------------------

    //그룹 삭제하는 컨트롤러
    @PostMapping("/deleteGroup")
    public ResponseEntity<String> deleteGroup(@RequestBody Map<String, List<Long>> requestBody) {
        try {
            List<Long> todoIds = requestBody.get("todoIds");
            todoService.deleteGroup(todoIds);

            return new ResponseEntity<>("Completed successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to complete todos", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //-------------------------------------------------------------------------------------------------



    //시간 포멧 메서드
    private LocalDateTime getLocalDateTimeFromObject(Object value) {
        if (value instanceof String) {
            return LocalDateTime.parse((String) value);
        } else {
            throw new IllegalArgumentException("Invalid type for updatedDateTime");
        }
    }
    //-------------------------------------------------------------------------------------------------

    //롱 포멧 함수
    private Long getLongFromObject(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            return Long.parseLong((String) value);
        } else {
            throw new IllegalArgumentException("Invalid type for todoId");
        }
    }
    //-------------------------------------------------------------------------------------------------

    //투두 리스트를 그룹화 하는 컨트롤러
    @PostMapping("/insertGroup")
    @ResponseBody
    public List<Map<String, Object>> insertGroup(@RequestBody Map<String, Object> requestBody, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return Collections.emptyList();
        }

        // requestBody에서 todoIds를 추출
        List<Integer> todoIds = (List<Integer>) requestBody.get("todoIds");
        List<Long> todoIdsLong = todoIds.stream().map(Long::valueOf).collect(Collectors.toList());

        String groupTitle = (String) requestBody.get("groupTitle");
        String groupCategory = (String) requestBody.get("groupCategory");

        // userId를 이용하여 해당 유저의 이벤트만 가져오기
        List<TodoGroup> todoGroups = todoGroupService.insertGroup(userId, todoIdsLong, groupTitle, groupCategory);

        return todoGroups.stream()
                .map(todoGroup -> {
                    Map<String, Object> event = new HashMap<>();
                    event.put("id", todoGroup.getId());
                    event.put("title", todoGroup.getGroupName());
                    return event;
                })
                .collect(Collectors.toList());
    }
    //-------------------------------------------------------------------------------------------------

}
