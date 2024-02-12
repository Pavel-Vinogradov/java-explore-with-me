package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.StateEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiatorId(long userId, PageRequest page);

    @Query("select e from Event e " +
           "where (e.initiator.id in :users or :users is null) " +
           "and (e.state in :states or :states is null) " +
           "and (e.category.id in :idsCategory or :idsCategory is null) " +
           "and (e.eventDate > :rangeStart or cast(:rangeStart as LocalDateTime) is null) " +
           "and (e.eventDate < :rangeEnd or cast(:rangeEnd as LocalDateTime) is null)")
    List<Event> adminSearchEvents(List<Long> users,
                            List<StateEvent> states,
                            List<Long> idsCategory,
                            LocalDateTime rangeStart,
                            LocalDateTime rangeEnd,
                            PageRequest page);

    @Query("select e from Event e " +
           "where (e.state = ?1) " +
           "and ((upper(e.annotation) like upper(concat('%', ?2, '%'))) " +
           "or (upper(e.description) like upper(concat('%', ?2, '%')))) " +
           "and (e.category.id in ?3 or ?3 is null) " +
           "and (e.paid is ?4 or ?4 is null) " +
           "and (e.eventDate > ?5 or cast(?5 as LocalDateTime) is null) " +
           "and (e.eventDate < ?6 or cast(?6 as LocalDateTime) is null)")
    List<Event> publicSearchAllEvents(StateEvent state, String text, List<Integer> categories, Boolean paid,
                                   LocalDateTime start, LocalDateTime end, PageRequest page);
}
