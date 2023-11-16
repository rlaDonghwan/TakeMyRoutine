package inhatc.TakeMyRoutine.controller;

import inhatc.TakeMyRoutine.domain.GroupList;
import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.domain.TodoGroup;
import inhatc.TakeMyRoutine.domain.User;
import inhatc.TakeMyRoutine.dto.TodoRequest;
import inhatc.TakeMyRoutine.service.TodoGroupService;
import inhatc.TakeMyRoutine.service.TodoService;
import inhatc.TakeMyRoutine.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/home")
public class RoutineController {
    private final TodoGroupService todoGroupService;
    private final UserService userService;
    private final TodoService todoService;

    //루틴 화면 컨트롤러
    @GetMapping("/routine")
    public String routinePage(Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "Routine");

        //세션에서 userId를 받아왹
        User loginUser = userService.getLoginUserById(userId);
        if (userId == null) {
            return "redirect:/home/login";
        }
        // 사용자 정보가 없으면 로그인 페이지로 리다이렉트
        if (loginUser != null) {
            // 현재 로그인한 사용자의 userId
            Long loggedInUserId = loginUser.getId();

            // userId가 현재 로그인한 사용자와 일치하는 그룹들만 가져오기
            List<TodoGroup> todoGroups = todoGroupService.getGroupList()
                    .stream()
                    .filter(group -> group.getUser().getId().equals(loggedInUserId))
                    .collect(Collectors.toList());

            model.addAttribute("nickname", loginUser.getNickname());
            model.addAttribute("todoGroups", todoGroups);
            //-------------------------------------------------
            List<TodoGroup> shareableGroups = todoGroupService.getShareableGroupList();
            model.addAttribute("groupList", shareableGroups);
        }

        log.info("RoutineController");
        return "routine";

    }
    //-------------------------------------------------------------------------------------------------

    //그룹 수정 컨트롤러
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
    //-------------------------------------------------------------------------------------------------

    //그룹 삭제
    @PostMapping("/deleteGroups")
    public ResponseEntity<String> deleteGroups(@RequestBody Map<String, List<Long>> requestBody) {
        try {
            // 여러 아이디를 가진 todo들을 삭제
            List<Long> groupId = requestBody.get("groupId");
            todoGroupService.deleteGroup(groupId);

            return new ResponseEntity<>("Completed successfully", HttpStatus.OK);
        } catch (Exception e) {
            // 예외 발생 시 로그 출력
            e.printStackTrace();
            return new ResponseEntity<>("Failed to complete todos", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //---------------------------------------------------------------------------------------------------------------


    // 그룹을 공유하는 메서드
    @PostMapping("/shareGroups")
    public ResponseEntity<String> shareGroups(@RequestBody Map<String, List<Long>> request) {
        List<Long> groupIds = request.get("groupId");

        if (groupIds != null) {
            todoGroupService.shareGroups(groupIds);
            return new ResponseEntity<>("그룹이 공유되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("그룹 ID가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }
    //---------------------------------------------------------------------------------------------------------------

    @GetMapping("/getTodoListByGroupId")
    public ResponseEntity<List<TodoRequest>> getTodoListByGroupId(@RequestParam Long groupId) {
        List<TodoRequest> todoList = todoService.getTodoListByGroupId(groupId);
        if (todoList != null && !todoList.isEmpty()) {
            List<TodoRequest> todoRequestList = todoList.stream()
                    .map(todo -> new TodoRequest(todo.getTodoId(), todo.getTodoNickName(), todo.getTitle(), todo.getStartTime(), todo.getEndTime(), todo.getMemo(), todo.getPlace()))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(todoRequestList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}
