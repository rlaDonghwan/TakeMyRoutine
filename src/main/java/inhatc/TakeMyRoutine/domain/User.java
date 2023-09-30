package inhatc.TakeMyRoutine.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.engine.internal.ImmutableEntityEntry;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)


    private Long id;
    @Column(name="login_id")
    private String loginId;
    private String password;
    private String nickname;

}