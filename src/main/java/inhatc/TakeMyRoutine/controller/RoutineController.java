package inhatc.TakeMyRoutine.controller;

import inhatc.TakeMyRoutine.dto.TodoRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
//        model.addAttribute("todoRequest", new TodoRequest(userId));
        return "routine";
    }

}
