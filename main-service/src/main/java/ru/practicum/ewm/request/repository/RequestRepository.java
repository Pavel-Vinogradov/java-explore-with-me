package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.enums.RequestStatus;
import ru.practicum.ewm.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Boolean existsByEventIdAndRequesterId(Long eventId, Long userId);

    Optional<Request> findByIdAndRequesterId(Long id, Long requesterId);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByEventIdInAndStatus(List<Long> eventsIds, RequestStatus requestStatus);

    List<Request> findAllByEventId(Long eventId);

    int countByEventIdAndStatus(Long eventId, RequestStatus status);

    Optional<List<Request>> findByEventIdAndIdIn(Long eventId, List<Long> id);
}
