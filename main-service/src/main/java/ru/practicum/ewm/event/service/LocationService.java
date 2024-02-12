package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.repository.LocationRepository;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationStorage;

    public Location createLocation(Location location) {
        return locationStorage.save(location);
    }
}
