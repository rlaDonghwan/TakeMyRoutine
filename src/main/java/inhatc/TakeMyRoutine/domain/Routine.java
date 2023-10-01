package inhatc.TakeMyRoutine.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "login_id")
    private User user;  // User 엔터티와 연관

    private String title;
    private String memo;
    private String place;

    private Date dateTime;
    private Date start;
    private Date cycle;
}
