package inhatc.TakeMyRoutine.dto;

import inhatc.TakeMyRoutine.domain.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequest {

    @NotEmpty(message = "아이디가 비어있습니다.")
    private String loginId;

    @NotEmpty(message = "아이디가 비어있습니다.")
    private String password;

    private String passwordCheck;

    @NotEmpty(message = "닉네임이 비어있습니다.")
    private String nickname;


    public User toEntity() {
        return User.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .build();
    }


}
