package inhatc.TakeMyRoutine.controller;

import inhatc.TakeMyRoutine.domain.TodoGroup;
import inhatc.TakeMyRoutine.dto.TodoRequest;
import inhatc.TakeMyRoutine.service.TodoGroupService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/home")
public class RoutineController {
    private final TodoGroupService todoGroupService;
    @GetMapping("/routine")
    public String routinePage(Model model, HttpSession session) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "Routine");
        log.info("RoutineController");

        Long userId = (Long) session.getAttribute("userId");

        // 사용자 정보가 없으면 로그인 페이지로 리다이렉트
        if (userId == null) {
            return "redirect:/login"; // 로그인 페이지로 리다이렉트하도록 수정
        }

        // 사용자 정보가 있으면 Todo 페이지로 이동
        model.addAttribute("todoRequest", new TodoRequest(userId));

        List<TodoGroup> todoGroups = todoGroupService.getGroupList();
        model.addAttribute("todoGroups", todoGroups);
        return "routine";

    }

    @PutMapping("/updateGroup")
    @ResponseBody
    public ResponseEntity<String> updateGroup(@RequestBody Map<String, String> updateGroupData) {
        try {
            Long groupId = Long.parseLong(updateGroupData.get("groupId"));
            String updatedGroupName = updateGroupData.get("updatedGroupName");
            String updatedGroupCategory = updateGroupData.get("updatedGroupCategory");

            todoGroupService.updateGroup(groupId, updatedGroupName, updatedGroupCategory);

            return ResponseEntity.ok("그룹이 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    @PostMapping("/deleteGroups")
    public ResponseEntity<String> deleteTodo(@RequestBody Map<String, List<Long>> requestBody) {
        try {
            // 여러 아이디를 가진 todo들을 삭제
            List<Long> groupId = requestBody.get("groupId");
            todoGroupService.completeTodos(groupId);

            return new ResponseEntity<>("Completed successfully", HttpStatus.OK);
        } catch (Exception e) {
            // 예외 발생 시 로그 출력
            e.printStackTrace();
            return new ResponseEntity<>("Failed to complete todos", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }






}
