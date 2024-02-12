package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Request findByRequesterIdAndEventIdAndStatusIsNotIn(long userId, long eventId, List<RequestStatus> requests);

    List<Request> findAllByEventId(long eventId);

    long countByEventIdAndStatusIsNotIn(long eventId, List<RequestStatus> requests);

    List<Request> findAllByRequesterId(long userId);
}
