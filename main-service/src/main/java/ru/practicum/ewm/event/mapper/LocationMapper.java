package ru.practicum.ewm.event.mapper;

import ru.practicum.ewm.event.dto.LocationDto;
import ru.practicum.ewm.event.model.Location;

public class LocationMapper {
    public static Location toLocation(LocationDto locationDto) {
        return new Location(null, locationDto.getLat(), locationDto.getLon());
    }

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }
}
