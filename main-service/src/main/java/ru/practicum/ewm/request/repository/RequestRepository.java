package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.enums.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {


    int countByEventIdAndStatus(Long eventId, RequestStatus status);

    Boolean existsByEventIdAndRequesterId(Long eventId, Long userId);

    Optional<Request> findByIdAndRequesterId(Long id, Long requesterId);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByEventIdInAndStatus(List<Long> eventsIds, RequestStatus requestStatus);

    List<Request> findAllByEventId(Long eventId);

    Optional<List<Request>> findByEventIdAndIdIn(Long eventId, List<Long> id);
}
