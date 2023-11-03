package inhatc.TakeMyRoutine.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // User 엔터티와 연관

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL)
    private List<Todo> todos;  // Todo 엔터티와의 일대다 관계

    private LocalDate start;  // 루틴 시작 시간을 저장하는 필드

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;  // 요일을 저장하는 필드


    @Column(name = "routine_group")
    private String groupName;  // 그룹화할 때 사용할 이름을 저장하는 필드

    // 다른 필드 및 메서드들...
}
