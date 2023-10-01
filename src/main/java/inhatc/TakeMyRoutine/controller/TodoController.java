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

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    public String todoInsert( String time, @Valid @ModelAttribute TodoRequest todoRequest, BindingResult bindingResult, Model model) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "Todo");
        Long userId = (Long) httpSession.getAttribute("userId");
        todoRequest.setTodoId(userId);

        todoService.join(todoRequest);
        return "redirect:/home";

    }

}
