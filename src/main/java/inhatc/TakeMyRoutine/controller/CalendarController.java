package inhatc.TakeMyRoutine.controller;

import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.service.TodoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "http://127.0.0.1:8080")
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/home")
public class CalendarController {

    private final TodoService todoService;

    //캘린더 화면 컨트롤러
    @GetMapping("/calendar")
    public String calendarPage(Model model, HttpSession session) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "calendar");
        //세션에서 userId값을 가져옴
        Long userId = (Long) session.getAttribute("userId");

        // 사용자 정보가 없으면 로그인 페이지로 리다이렉트
        if (userId == null) {
            return "redirect:/home/login";
        }

        return "calendar";
    }
    //-------------------------------------------------------------------------------------------------

    //캘린더 이벤트 출력 컨트롤러
    @GetMapping("/calendar/events")
    @ResponseBody
    public List<Map<String, Object>> getEvents(@RequestParam String start, @RequestParam String end, HttpSession session) {
        // 세션에서 userId 가져오기
        Long sessionUserId = (Long) session.getAttribute("userId");

        // 세션이 없거나 userId가 없으면 빈 리스트 반환
        if (sessionUserId == null) {
            return Collections.emptyList();
        }
        //시작 시작 종료 시간의 포맷을 변경해주고 서울 시간으로 세팅해줌
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        ZonedDateTime startDateUtc = ZonedDateTime.parse(start).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endDateUtc = ZonedDateTime.parse(end).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime startDateKST = startDateUtc.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        ZonedDateTime endDateKST = endDateUtc.withZoneSameInstant(ZoneId.of("Asia/Seoul"));

        // userId를 이용하여 해당 유저의 이벤트만 가져오기
        List<Todo> todos = todoService.getTodosByUserIdAndTime(sessionUserId, startDateKST.toLocalDateTime(), endDateKST.toLocalDateTime());

        return todos.stream()
                .map(todo -> {
                    Map<String, Object> event = new HashMap<>();
                    event.put("id", todo.getId());
                    event.put("title", todo.getTitle());
                    event.put("start", todo.getStartTime().format(formatter));
                    event.put("end", todo.getStartTime().plusHours(1).format(formatter)); // Adjust end time as needed
                    event.put("allDay", false); // Adjust as needed
                    return event;
                })
                .collect(Collectors.toList());
    }
    //-------------------------------------------------------------------------------------------------

    //캘린더 이벤트 추가
    @PostMapping("/calendar/addEvent")
    @ResponseBody
    public String addEvent(@RequestParam String title, @RequestParam String time, @RequestParam String content, @RequestParam String place, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        try {
            todoService.addEvent(userId, title, dateTime, content, place);

            // 이벤트 추가 성공 로그
            log.info("Event added successfully. User ID: {}, Title: {}, Time: {}, Content: {}, Place: {}",
                    userId, title, dateTime, content, place);

            return "Event added successfully!";
        } catch (Exception e) {
            // 에러가 발생한 경우 로그
            log.error("Error adding event. User ID: {}, Title: {}, Time: {}, Content: {}, Place: {}",
                    userId, title, dateTime, content, place, e);

            // 클라이언트에게 에러 메시지 반환
            return "Error adding event. Please check the logs for details.";
        }
    }
    //-------------------------------------------------------------------------------------------------

    //캘린더 이벤트 수정
    @PutMapping("/calendar/updateEvent")
    public ResponseEntity<String> updateEvent(@RequestBody Map<String, Object> requestBody, HttpSession session) {
        try {

            String todoIdString = (String) requestBody.get("todoId");
            Long todoId = Long.valueOf(todoIdString);

            String title = (String) requestBody.get("title");
            String dateStr = (String) requestBody.get("date");
            LocalDateTime updatedDateTime = LocalDateTime.parse(dateStr);

            // 세션에서 userId 가져오기
            Long userId = (Long) session.getAttribute("userId");

            // TodoService를 통해 이벤트 업데이트
            todoService.updateEvent(todoId, title, updatedDateTime, userId);

            return new ResponseEntity<>("Event updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update event", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //-------------------------------------------------------------------------------------------------


    @PostMapping("/calendar/checkGroupExistence")
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

    //이벤트 삭제
    @DeleteMapping("/calendar/deleteEvent")
    public ResponseEntity<String> deleteEvent(@RequestParam Long todoId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        try {
            todoService.deleteEvent(todoId, userId);
            return new ResponseEntity<>("Event deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting event: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //-------------------------------------------------------------------------------------------------

    @PostMapping("/calendar/deleteGroup")
    public ResponseEntity<String> deleteGroup(@RequestBody Map<String, List<Long>> requestBody) {
        try {
            List<Long> todoIds = requestBody.get("todoIds");
            todoService.deleteGroup(todoIds);
            return new ResponseEntity<>("Group deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to delete group", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //로그인 체킹 메서드
    @GetMapping("/calendar/checkSession")
    public ResponseEntity<Boolean> checkSession(HttpSession session) {
        // 세션에서 userId 가져오기
        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            // 세션 아이디가 존재하면 true 반환
            return ResponseEntity.ok(true);
        } else {
            // 세션 아이디가 없으면 false 반환
            return ResponseEntity.ok(false);
        }
    }
    //-------------------------------------------------------------------------------------------------
}

