package inhatc.TakeMyRoutine.dto;

import inhatc.TakeMyRoutine.domain.Todo;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class TodoRequest {

    private Long todoId;

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
    }

}
