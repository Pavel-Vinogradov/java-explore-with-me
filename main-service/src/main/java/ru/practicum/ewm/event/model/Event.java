package ru.practicum.ewm.event.model;

import lombok.*;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 2000)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", nullable = false)
    private Category category;

    @Column(nullable = false, length = 7000)
    private String description;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location", nullable = false)
    private Location location;

    @Column
    private boolean paid;

    @Column(name = "participant_limit")
    private int participantLimit;

    @Column(name = "confirmed_requests")
    private int confirmedRequests;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator", nullable = false)
    private User initiator;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Column(length = 120)
    private String title;

    @Column
    @Enumerated(EnumType.STRING)
    private StateEvent state;

    @Column
    private int views;
}
