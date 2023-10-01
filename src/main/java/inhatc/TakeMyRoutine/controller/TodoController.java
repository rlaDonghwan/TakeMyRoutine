package inhatc.TakeMyRoutine.controller;

import inhatc.TakeMyRoutine.domain.User;
import inhatc.TakeMyRoutine.dto.TodoRequest;
import inhatc.TakeMyRoutine.service.TodoService;
import inhatc.TakeMyRoutine.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/home")
public class TodoController {
    private final TodoService todoService;
    private final UserService userService;

    @GetMapping("/todoInsert")
    public String todoPage(Model model, HttpSession session) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "Todo");
        log.info("TodoController");

        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("userId");  // 키를 "user"가 아닌 "userId"로 수정

        // 사용자 정보가 없으면 로그인 페이지로 리다이렉트
        if (user == null) {
            return "redirect:/home/login";
        }

        // 사용자 정보가 있으면 Todo 페이지로 이동
        model.addAttribute("todoRequest", new TodoRequest(user));
        return "todoInsert";
    }






    @PostMapping("/todoInsert")
    public String todoInsert(@Valid @ModelAttribute TodoRequest todoRequest, BindingResult bindingResult, Model model) {

        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "Todo");

//        if (bindingResult.hasErrors()) {
//            return "todoInsert";
//        }
        log.info("입력");

        todoService.join(todoRequest);
        return "redirect:/home";
    }

}
