package inhatc.TakeMyRoutine.dto;

import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.domain.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

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

    private Date dateTime;

    @NotEmpty(message = "내용이 비어있습니다.")
    private String memo;

    private String place;

    public TodoRequest(User user) {
        this.todoId = user.getId();
        this.todoNickName = user.getNickname();
    }


    public Todo toEntity(User user) {
        return Todo.builder()
                .user(user)
                .title(this.title)
                .dataTime(this.dateTime)
                .memo(this.memo)
                .place(this.place)
                .build();
    }
}
