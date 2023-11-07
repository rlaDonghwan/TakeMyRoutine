package inhatc.TakeMyRoutine.controller;

import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.domain.TodoGroup;
import inhatc.TakeMyRoutine.service.TodoGroupService;
import inhatc.TakeMyRoutine.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/home")
public class TodoGroupController {
    private final TodoGroupService todoGroupService;

    @PostMapping("/insetGroup")
    public ResponseEntity<String> insetGroup(@RequestBody TodoGroup todoGroup) {
        try {
            todoGroupService.insetGroup(todoGroup.getTodos(), todoGroup.getGroupName(), todoGroup.getCategory());
            return new ResponseEntity<>("그룹 추가 성공", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("그룹 추가 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

