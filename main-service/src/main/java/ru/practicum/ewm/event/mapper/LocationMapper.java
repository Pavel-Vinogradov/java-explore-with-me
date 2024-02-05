package ru.practicum.ewm.event.mapper;

import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.dto.LocationDto;

public class LocationMapper {
    public static Location toLocation(LocationDto locationDto) {
        return new Location(null, locationDto.getLat(), locationDto.getLon());
    }

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }
}
