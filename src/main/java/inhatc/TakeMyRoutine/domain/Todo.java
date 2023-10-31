package inhatc.TakeMyRoutine.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id") // User와 연결되는 외래키
    private User user;  // User 엔터티와 연관

    private String title; // 제목

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime dataTime; // 시간

    private String memo; // 내용

    private String place; // 약속 장소
    private boolean completed;
}
