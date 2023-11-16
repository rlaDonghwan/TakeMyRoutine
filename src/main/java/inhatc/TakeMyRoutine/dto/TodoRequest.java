package inhatc.TakeMyRoutine.dto;

import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.domain.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

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

    private LocalDateTime startTime; // 시간

    private LocalDateTime endTime; // 시간

    @NotEmpty(message = "내용이 비어있습니다. ")
    private String memo;

    private String place;


    public TodoRequest(Long todoId) {
        this.todoId = todoId;
    }


    public TodoRequest(Todo todo) {
        this.todoId = todo.getId();
        this.title = todo.getTitle();
        this.startTime = todo.getStartTime();
        this.endTime = todo.getEndTime();
        this.memo = todo.getMemo();
        this.place = todo.getPlace();
        // 나머지 필드 매핑...

        // 만약 Todo의 User 정보도 매핑해야 한다면
        // this.userId = todo.getUser().getId();
        // this.username = todo.getUser().getUsername();
        // 등의 코드를 추가할 수 있습니다.
    }

}
