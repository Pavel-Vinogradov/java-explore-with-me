package ru.practicum.ewm.request.model;

import lombok.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "event", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester", nullable = false)
    private User requester;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
