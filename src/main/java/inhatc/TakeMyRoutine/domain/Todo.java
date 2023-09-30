package inhatc.TakeMyRoutine.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "login_id") //user랑 join하는 todo_id
    private Todo todoId; // 외래키

    @ManyToOne
    @JoinColumn(name = "nickname") //user랑 join하는 todo_id
    private Todo todoNickName; // 외래키

    private String title; // 제목

    private Date dataTime; // 시간

    private String memo; // 내용

    private String place; //약속 장소



}
