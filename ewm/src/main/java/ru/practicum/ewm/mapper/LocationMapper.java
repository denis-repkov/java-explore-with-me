package ru.practicum.ewm.mapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.practicum.ewm.dto.Location;

@Converter(autoApply = true)
public class LocationMapper implements AttributeConverter<Location, String> {

    @Override
    public String convertToDatabaseColumn(Location location) {
        if (location == null) {
            return null;
        }

        return location.getLat() + "," + location.getLon();
    }

    @Override
    public Location convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        String[] parts = dbData.split(",");
        double lat = Double.parseDouble(parts[0]);
        double lon = Double.parseDouble(parts[1]);
        return new Location(lat, lon);
    }
}