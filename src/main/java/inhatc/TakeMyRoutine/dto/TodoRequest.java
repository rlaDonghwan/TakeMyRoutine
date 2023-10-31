package inhatc.TakeMyRoutine.dto;

import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.domain.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class TodoRequest {

    private Long todoId;
    private String todoNickName;

    @NotEmpty(message = "제목이 비어있습니다.")
    private String title;

    private LocalDateTime dateTime;

    @NotEmpty(message = "내용이 비어있습니다. ")
    private String memo;

    private String place;

    private boolean completed;

    public TodoRequest(Long todoId) {
        this.todoId = todoId;
    }



}
