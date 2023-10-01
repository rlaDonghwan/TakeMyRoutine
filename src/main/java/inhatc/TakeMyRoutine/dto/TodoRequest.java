package inhatc.TakeMyRoutine.dto;

import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.domain.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TodoRequest {

    private Long todoId;
    private String todoNickName;

    @NotEmpty(message = "제목이 비어있습니다.")
    private String title;

    private LocalDateTime dateTime;

    @NotEmpty(message = "내용이 비어있습니다.")
    private String memo;

    private String place;

    // 기존의 User 필드 삭제
    // 생성자 및 toEntity 메서드 수정

    public TodoRequest(Long todoId) {
        this.todoId = todoId;
    }

}
