package inhatc.TakeMyRoutine.controller;

import inhatc.TakeMyRoutine.domain.TodoGroup;
import inhatc.TakeMyRoutine.dto.TodoRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/home")
public class RoutineController {
    @GetMapping("/routine")
    public String todoPage(Model model, HttpSession session) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "Routine");
        log.info("RoutineController");

        Long userId = (Long) session.getAttribute("userId");

        // 사용자 정보가 없으면 로그인 페이지로 리다이렉트
        if (userId == null) {
            return "redirect:/home/routine";
        }

        // 사용자 정보가 있으면 Todo 페이지로 이동
        model.addAttribute("todoRequest", new TodoRequest(userId));
        return "routine";
    }

    // HomeController 또는 해당 컨트롤러에 메서드 추가
    @GetMapping("/home/routineList")
    public String getRoutineList(Model model) {
//        // 백엔드에서 투두 그룹 목록을 가져오는 로직 필요
//        List<TodoGroup> todoGroups = todoGroupService.getAllTodoGroups(); // 예시: todoGroupService는 TodoGroup과 관련된 비즈니스 로직을 처리하는 서비스 클래스
//
//        model.addAttribute("todoGroups", todoGroups);
//
//        // 나머지 코드...

        return "routineList"; // 뷰의 이름 (Thymeleaf 템플릿의 파일명)
    }


}
