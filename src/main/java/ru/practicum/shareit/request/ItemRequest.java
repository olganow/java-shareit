package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requester;

    @Column(name = "created", nullable = false)
    private Timestamp created = Timestamp.valueOf(LocalDateTime.now());
}